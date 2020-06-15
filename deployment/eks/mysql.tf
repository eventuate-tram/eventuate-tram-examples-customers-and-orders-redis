resource "aws_db_instance" "mysql_instance" {
  name                    = "eventuate"
  identifier              = "eventuate-rds"
  allocated_storage       = 20
  storage_type            = "gp2"
  port                    = 3306
  instance_class          = "db.t2.micro"
  engine                  = "MySQL"
  engine_version          = "5.7.22"
  availability_zone       = data.aws_availability_zones.available.names[0]
  username                = var.rds_username
  password                = var.rds_pwd
  db_subnet_group_name    = aws_db_subnet_group.rds-subnet.name
  skip_final_snapshot     = "true"
  publicly_accessible     = "true"
  parameter_group_name    = aws_db_parameter_group.mysql_parameter_group.name
  vpc_security_group_ids  = [aws_security_group.eventuate-rds.id]
  backup_retention_period = "1"
  apply_immediately       = "true"

  provisioner "local-exec" {
    command = <<EOF
        mysqlsh --user=${aws_db_instance.mysql_instance.username} --password=${aws_db_instance.mysql_instance.password} --host ${aws_db_instance.mysql_instance.address} --sql  < 1.initialize-database.sql;
        mysqlsh --user=${aws_db_instance.mysql_instance.username} --password=${aws_db_instance.mysql_instance.password} --host ${aws_db_instance.mysql_instance.address} --sql  < 2.initialize-database.sql
        EOF
  }
}

resource "aws_db_parameter_group" "mysql_parameter_group" {
  family = "mysql5.7"
  name   = "eventuate-mysql-cdc"

  parameter {
    name  = "binlog_format"
    value = "ROW"
  }
}

resource "aws_security_group" "eventuate-rds" {
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
  subnet_ids = module.vpc.public_subnets
}