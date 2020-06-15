# Input variable definitions

variable "region" {
  default = "us-east-2"
}

variable "eks_cluster_name" {
  description = "Kubernetes Cluster Name"
  default     = "eventuate-redis-example"
}

variable "vpc_name" {
  description = "Name of VPC"
  type        = string
  default     = "example-vpc"
}

variable "vpc_cidr" {
  description = "CIDR block for VPC"
  type        = string
  default     = "10.0.0.0/16"
}

variable "vpc_public_subnets" {
  description = "Public subnets for VPC"
  type        = list(string)
  default     = ["10.0.101.0/24", "10.0.102.0/24", "10.0.103.0/24"]
}

variable "eks_tags" {
  description = "Tags to apply to resources created by EKS module"
  type        = map(string)
  default = {
    Terraform   = "true"
    Environment = "dev"
  }
}

variable "enable_dns_hostnames" {
  default = "true"
}

variable "rds_username" {
  default = "admin"
}

variable "rds_pwd" {
  default = "Eventuate123"
}

variable "prefix" {
  default = "dev"
}

variable "docdb_username" {
  default = "eventuateadmin"
}

variable "enable_codepipeline" {
  type    = string
  default = "false"
}

variable "docker_hub_image_prefix" {
  type    = string
  default = "eventuateexamples/eventuate-tram-examples-customers-and-orders-"
}

variable "map_accounts" {
  description = "Additional AWS account numbers to add to the aws-auth configmap. See examples/basic/variables.tf for example format."
  type        = list(string)
  default     = []
}

variable "map_roles" {
  description = "Additional IAM roles to add to the aws-auth configmap."
  type = list(object({
    rolearn  = string
    username = string
    groups   = list(string)
  }))
  default = []
}

variable "map_users" {
  description = "Additional IAM users to add to the aws-auth configmap."
  type = list(object({
    userarn  = string
    username = string
    groups   = list(string)
  }))
  default = []
}
