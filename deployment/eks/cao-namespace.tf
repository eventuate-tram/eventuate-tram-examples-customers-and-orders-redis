# Namespace
resource "kubernetes_namespace" "cao" {
  metadata {
    annotations = {
      name = var.cao_namespace
    }
    name = var.cao_namespace
  }
}