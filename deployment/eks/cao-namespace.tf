# Namespace
resource "kubernetes_namespace" "cao" {
  metadata {
    annotations = {
      name = "eventuate-tram-examples-customers-and-orders"
    }
    name = "eventuate-tram-examples-customers-and-orders"
  }
}