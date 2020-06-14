resource "aws_security_group" "eventuate-node-group" {
  name_prefix = "eventuate-node-group"

  vpc_id = module.vpc.vpc_id

  ingress {
    from_port = 22
    to_port   = 22
    protocol  = "tcp"

    cidr_blocks = [var.vpc_cidr]
  }
}
