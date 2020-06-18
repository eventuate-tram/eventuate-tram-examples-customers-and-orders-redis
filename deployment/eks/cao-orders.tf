resource "kubernetes_service" "order_service" {
  metadata {
    name      = "order-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {
    selector = {
      k8s-app = "order-service"
    }
    port {
      port        = 8080
      target_port = 8080
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_deployment" "order_service" {
  depends_on = [
    aws_db_instance.mysql_instance,
    kubernetes_stateful_set.mysql,
    aws_elasticache_cluster.redis_instance,
    kubernetes_deployment.redis,
    kubernetes_deployment.cdc_service
  ]

  metadata {
    name      = "order-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
    labels = {
      k8s-app = "order-service"
    }
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        k8s-app = "order-service"
      }
    }
    template {
      metadata {
        labels = {
          k8s-app = "order-service"
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
          command = ["sh", "-c", "until nslookup ${local.cache_endpoint}; do echo waiting for ${local.cache_endpoint}; sleep 2; done;"]
        }
        init_container {
          name    = "init-cdc"
          image   = "busybox:1.31"
          command = ["sh", "-c", "until nslookup ${local.cdc_endpoint}; do echo waiting for ${local.cdc_endpoint}; sleep 2; done;"]
        }

        container {
          image             = "eventuateexamples/eventuate-tram-examples-customers-and-orders-redis-order-service:latest"
          name              = "order-service"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 8080
          }

          dynamic "env" {
            for_each = local.app_env
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
            period_seconds    = 20
            failure_threshold = 14
          }
          readiness_probe {
            http_get {
              path = "/actuator/health"
              port = 8080
            }
            period_seconds    = 20
            failure_threshold = 14
          }
        }
      }
    }
  }
}
