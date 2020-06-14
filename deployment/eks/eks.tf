module "eks" {
  source       = "terraform-aws-modules/eks/aws"
  
  cluster_name = var.eks_cluster_name
  
  vpc_id 	   = module.vpc.vpc_id
  subnets      = module.vpc.public_subnets

  tags 		   = var.eks_tags

  worker_groups = [
    {
      name                          = "eventuate-node-group"
      instance_type                 = "t2.micro"
      additional_userdata           = "echo foo bar"
      asg_desired_capacity          = 2
      additional_security_group_ids = [aws_security_group.eventuate-node-group.id]
    }
  ]
}

data "aws_eks_cluster" "cluster" {
  name = module.eks.cluster_id
}

data "aws_eks_cluster_auth" "cluster" {
  name = module.eks.cluster_id
}
