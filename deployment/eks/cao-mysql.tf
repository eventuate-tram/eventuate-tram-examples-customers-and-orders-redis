resource "kubernetes_service" "mysql" {
  count = var.use_rds_and_elastic_cache ? 0 : 1

  metadata {
    name      = "mysql"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {
    selector = {
      k8s-app = "mysql"
    }
    port {
      port        = 3306
      target_port = 3306
    }
    type = "LoadBalancer"
  }
}

resource "kubernetes_stateful_set" "mysql" {
  count = var.use_rds_and_elastic_cache ? 0 : 1

  metadata {
    labels = {
      k8s-app = "mysql"
    }
    name      = "mysql"
    namespace = kubernetes_namespace.cao.metadata.0.name
  }

  spec {

    replicas = 1
    selector {
      match_labels = {
        k8s-app = "mysql"
      }
    }
    service_name = "mysql"
    template {
      metadata {
        labels = {
          k8s-app = "mysql"
        }
      }

      spec {
        termination_grace_period_seconds = 10
        container {
          name              = "mysql"
          image             = "eventuateio/eventuate-mysql"
          image_pull_policy = "IfNotPresent"
          port {
            container_port = 3306
          }

          env {
            name  = "MYSQL_ROOT_PASSWORD"
            value = "rootpassword"
          }
          env {
            name  = "MYSQL_USER"
            value = "mysqluser"
          }
          env {
            name  = "MYSQL_PASSWORD"
            value = "mysqlpw"
          }

          volume_mount {
            name       = "mysql-persistent-storage"
            mount_path = "/var/lib/mysql"
            sub_path   = "mysql"
          }
        }
      }
    }

    volume_claim_template {
      metadata {
        name      = "mysql-persistent-storage"
        namespace = kubernetes_namespace.cao.metadata.0.name
      }

      spec {
        access_modes       = ["ReadWriteOnce"]
        storage_class_name = "standard"

        resources {
          requests = {
            storage = "256Mi"
          }
        }
      }
    }
  }
}