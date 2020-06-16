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
    type = "LoadBalancer"
  }
}

resource "kubernetes_deployment" "cdc_service" {
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
        container {
          image             = "eventuateio/eventuate-cdc-service:0.4.0.RELEASE"
          name              = "cdc-service"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 8080
          }
          liveness_probe {
            http_get {
              path = "/health"
              port = 8080
            }
            initial_delay_seconds = 120
            period_seconds        = 20
          }
          readiness_probe {
            http_get {
              path = "/health"
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