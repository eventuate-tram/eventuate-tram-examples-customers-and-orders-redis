resource "kubernetes_service" "zipkin" {
  metadata {
    name      = "zipkin"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {
    selector = {
      k8s-app = "zipkin"
    }
    port {
      port        = 9411
      target_port = 9411
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_deployment" "zipkin" {
  metadata {
    name      = "zipkin"
    namespace = kubernetes_namespace.cao.metadata.0.name
    labels = {
      k8s-app = "zipkin"
    }
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        k8s-app = "zipkin"
      }
    }
    template {
      metadata {
        labels = {
          k8s-app = "zipkin"
        }
      }

      spec {
        container {
          image             = "openzipkin/zipkin:2.21"
          name              = "zipkin"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 9411
          }
          env {
            name  = "JAVA_OPTS"
            value = "-Xmx64m"
          }
          liveness_probe {
            http_get {
              path = "/health"
              port = 9411
            }
            initial_delay_seconds = 60
            period_seconds        = 20
          }
          readiness_probe {
            http_get {
              path = "/health"
              port = 9411
            }
            initial_delay_seconds = 60
            period_seconds        = 20
          }
        }
      }
    }
  }
}