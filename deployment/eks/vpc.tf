provider "aws" {
  region = var.region
}

data "aws_availability_zones" "available" {}

module "vpc" {
  source = "terraform-aws-modules/vpc/aws"
  name   = "vpc-eventuate"

  cidr           = var.vpc_cidr
  azs            = data.aws_availability_zones.available.names
  public_subnets = var.vpc_public_subnets

  enable_dns_hostnames = var.enable_dns_hostnames

  tags = {
    "kubernetes.io/cluster/${var.eks_cluster_name}" = "shared"
  }

  public_subnet_tags = {
    "kubernetes.io/cluster/${var.eks_cluster_name}" = "shared"
    "kubernetes.io/role/elb"                        = "1"
  }

}
