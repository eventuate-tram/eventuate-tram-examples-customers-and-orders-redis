resource "kubernetes_service" "order-history-service" {
  metadata {
    name      = "order-history-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {
    selector = {
      k8s-app = "order-history-service"
    }
    port {
      port        = 8080
      target_port = 8080
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_deployment" "order-history-service" {
  metadata {
    name      = "order-history-service"
    namespace = kubernetes_namespace.cao.metadata.0.name
    labels = {
      k8s-app = "order-history-service"
    }
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        k8s-app = "order-history-service"
      }
    }
    template {
      metadata {
        labels = {
          k8s-app = "order-history-service"
        }
      }

      spec {
        container {
          image             = "eventuateexamples/eventuate-tram-examples-customers-and-orders-redis-order-history-service:latest"
          name              = "order-history-service"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 8080
          }
          liveness_probe {
            http_get {
              path = "/health"
              port = 8080
            }
            period_seconds    = 20
            failure_threshold = 14
          }
          readiness_probe {
            http_get {
              path = "/health"
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
