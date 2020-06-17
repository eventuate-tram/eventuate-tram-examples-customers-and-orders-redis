resource "aws_elasticache_cluster" "redis_instance" {
  count = var.use_rds_and_elastic_cache ? 1 : 0

  cluster_id           = "eventuate-cache"
  engine               = "redis"
  node_type            = "cache.t2.micro"
  num_cache_nodes      = 1
  parameter_group_name = "default.redis5.0"
  engine_version       = "5.0.6"
  port                 = 6379

  security_group_ids = [
    aws_security_group.eventuate-cache[count.index].id
  ]
  subnet_group_name = aws_elasticache_subnet_group.cache-subnet-group[count.index].name

}

resource "aws_security_group" "eventuate-cache" {
  count = var.use_rds_and_elastic_cache ? 1 : 0

  name        = "eventuate-cache-sg"
  description = "Cache security group"

  vpc_id = module.vpc.vpc_id

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    self      = true
  }

  ingress {
    from_port = 6379
    to_port   = 6379
    protocol  = "tcp"

    security_groups = [aws_security_group.eventuate-sg.id]

    cidr_blocks = [
      var.vpc_cidr,
      "0.0.0.0/0"
    ]
  }

  egress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"

    cidr_blocks = [
      "0.0.0.0/0",
    ]
  }

  tags = {
    Name = "eventuate-cache"
  }

}

resource "aws_elasticache_subnet_group" "cache-subnet-group" {
  count = var.use_rds_and_elastic_cache ? 1 : 0

  name       = "eventuate-cache-subnet-group"
  subnet_ids = module.vpc.public_subnets
}