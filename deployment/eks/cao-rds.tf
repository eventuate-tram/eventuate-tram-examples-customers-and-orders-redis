resource "aws_db_instance" "mysql_instance" {
  count = var.use_rds_and_elastic_cache ? 1 : 0

  identifier           = "eventuate-rds"
  allocated_storage    = 5
  storage_type         = "gp2"
  port                 = 3306
  instance_class       = "db.t2.micro"
  engine               = "MySQL"
  engine_version       = "5.7.22"
  availability_zone    = data.aws_availability_zones.available.names[0]
  username             = var.rds_username
  password             = var.rds_pwd
  db_subnet_group_name = aws_db_subnet_group.rds-subnet[count.index].name
  skip_final_snapshot  = "true"
  publicly_accessible  = "true"
  parameter_group_name = aws_db_parameter_group.mysql_parameter_group[count.index].name
  vpc_security_group_ids = [
    aws_security_group.eventuate-rds[count.index].id
  ]
  backup_retention_period = "1"
  apply_immediately       = "true"

  provisioner "local-exec" {
    command = <<EOF
              curl -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/0.9.0.RELEASE/mysql/1.initialize-database.sql | \
                mysqlsh --user=${aws_db_instance.mysql_instance[count.index].username} \
                        --password=${aws_db_instance.mysql_instance[count.index].password} \
                        --host ${aws_db_instance.mysql_instance[count.index].address} \
                        --sql
              curl -s https://raw.githubusercontent.com/eventuate-foundation/eventuate-common/0.9.0.RELEASE/mysql/2.initialize-database.sql | \
                mysqlsh --user=${aws_db_instance.mysql_instance[count.index].username} \
                        --password=${aws_db_instance.mysql_instance[count.index].password} \
                        --host ${aws_db_instance.mysql_instance[count.index].address} \
                        --sql
              EOF
  }
}

resource "aws_db_parameter_group" "mysql_parameter_group" {
  count = var.use_rds_and_elastic_cache ? 1 : 0

  family = "mysql5.7"
  name   = "eventuate-mysql-cdc"

  parameter {
    name  = "binlog_format"
    value = "ROW"
  }
}

resource "aws_security_group" "eventuate-rds" {
  count = var.use_rds_and_elastic_cache ? 1 : 0

  name        = "eventuate-rds-sg"
  description = "RDS security group"

  vpc_id = module.vpc.vpc_id

  ingress {
    from_port = 0
    to_port   = 0
    protocol  = "-1"
    self      = true
  }

  ingress {
    from_port = 3306
    to_port   = 3306
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
    Name = "eventuate-rds"
  }

}

resource "aws_db_subnet_group" "rds-subnet" {
  count      = var.use_rds_and_elastic_cache ? 1 : 0
  subnet_ids = module.vpc.public_subnets
}