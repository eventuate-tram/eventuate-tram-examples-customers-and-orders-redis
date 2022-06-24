resource "kubernetes_service" "redis" {
  count = var.use_rds_and_elastic_cache ? 0 : 1

  metadata {
    name      = "redis"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {
    selector = {
      k8s-app = "redis"
    }
    port {
      port        = 6379
      target_port = 6379
    }
  }
}

resource "kubernetes_deployment" "redis" {
  count = var.use_rds_and_elastic_cache ? 0 : 1

  metadata {
    name      = "redis"
    namespace = kubernetes_namespace.cao.metadata.0.name
    labels = {
      k8s-app = "redis"
    }
  }

  spec {
    replicas = 1
    selector {
      match_labels = {
        k8s-app = "redis"
      }
    }
    template {
      metadata {
        labels = {
          k8s-app = "redis"
        }
      }

      spec {
        container {
          image             = "redis:5.0.7"
          name              = "redis"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 6379
          }
        }
      }
    }
  }
}