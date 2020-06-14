provider "kubernetes" {
  host                   = data.aws_eks_cluster.cluster.endpoint
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.cluster.certificate_authority.0.data)
  token                  = data.aws_eks_cluster_auth.cluster.token
  load_config_file       = false
  version                = "~> 1.11"
}

module "eks" {
  source = "terraform-aws-modules/eks/aws"

  cluster_name = var.eks_cluster_name

  vpc_id  = module.vpc.vpc_id
  subnets = module.vpc.public_subnets

  tags = var.eks_tags

  worker_groups = [
    {
      name                          = "eventuate-node-group"
      instance_type                 = "t2.micro"
      additional_userdata           = "echo foo bar"
      asg_desired_capacity          = 6
      additional_security_group_ids = [aws_security_group.eventuate-node-group.id]
    }
  ]

  map_accounts = var.map_accounts
  map_roles    = var.map_roles
  map_users    = var.map_users

}

data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}
