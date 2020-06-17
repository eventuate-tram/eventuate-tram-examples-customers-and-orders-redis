resource "kubernetes_service" "customer_service" {
  metadata {
    name      = "customer-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {
    selector = {
      k8s-app = "customer-service"
    }
    port {
      port        = 8080
      target_port = 8080
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_deployment" "customer_service" {
  metadata {
    name      = "customer-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
    labels = {
      k8s-app = "customer-service"
    }
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        k8s-app = "customer-service"
      }
    }
    template {
      metadata {
        labels = {
          k8s-app = "customer-service"
        }
      }

      spec {
        container {
          image             = "eventuateexamples/eventuate-tram-examples-customers-and-orders-redis-customer-service:latest"
          name              = "customer-service"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 8080
          }

          dynamic "env" {
            for_each = {
              for key, value in local.cao_container_env :
              key => value
              if var.use_rds_and_elastic_cache == "false"
            }
            content {
              name  = env.key
              value = env.value
            }
          }
          dynamic "env" {
            for_each = {
              for key, value in local.cao_managed_env :
              key => value
              if var.use_rds_and_elastic_cache == "true"
            }
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
