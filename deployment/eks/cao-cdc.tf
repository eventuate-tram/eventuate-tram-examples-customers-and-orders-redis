resource "kubernetes_service" "cdc_service" {
  metadata {
    name      = "cdc-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {
    selector = {
      k8s-app = "cdc-service"
    }
    port {
      port        = 8080
      target_port = 8080
    }
  }
}

resource "kubernetes_deployment" "cdc_service" {
  depends_on = [
    aws_db_instance.mysql_instance,
    kubernetes_stateful_set.mysql,
    aws_elasticache_cluster.redis_instance,
    kubernetes_deployment.redis
  ]

  metadata {
    name      = "cdc-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
    labels = {
      k8s-app = "cdc-service"
    }
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        k8s-app = "cdc-service"
      }
    }
    template {
      metadata {
        labels = {
          k8s-app = "cdc-service"
        }
      }

      spec {
        init_container {
          name    = "init-rdb"
          image   = "busybox:1.31"
          command = ["sh", "-c", "until nslookup ${local.rdb_host}; do echo waiting for ${local.rdb_host}; sleep 2; done;"]
        }
        init_container {
          name    = "init-cache"
          image   = "busybox:1.31"
          command = ["sh", "-c", "until nslookup ${local.redis_endpoint}; do echo waiting for ${local.redis_endpoint}; sleep 2; done;"]
        }

        container {
          image             = "eventuateio/eventuate-cdc-service:latest"
          name              = "cdc-service"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 8080
          }

          dynamic "env" {
            for_each = local.cdc_env
            content {
              name  = env.key
              value = env.value
            }
          }

          liveness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 120
            period_seconds        = 20
          }
          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            initial_delay_seconds = 120
            period_seconds        = 20
          }
        }
      }
    }
  }
}