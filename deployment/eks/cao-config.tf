locals {

  rdb_endpoint   = var.use_rds_and_elastic_cache ? aws_db_instance.mysql_instance[0].endpoint : kubernetes_service.mysql[0].metadata.0.name
  redis_endpoint = var.use_rds_and_elastic_cache ? aws_elasticache_cluster.redis_instance[0].cache_nodes.0.address : kubernetes_service.redis[0].metadata.0.name
  cdc_endpoint   = kubernetes_service.cdc_service.metadata.0.name

  rdb_host = var.use_rds_and_elastic_cache ? split(":", aws_db_instance.mysql_instance[0].endpoint)[0] : kubernetes_service.mysql[0].metadata.0.name

  app_env = {
    SPRING_DATASOURCE_URL               = "jdbc:mysql://${local.rdb_endpoint}/eventuate?useSSL=false",
    SPRING_DATASOURCE_USERNAME          = var.rds_username,
    SPRING_DATASOURCE_PASSWORD          = var.rds_pwd,
    SPRING_DATASOURCE_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver",
    EVENTUATE_REDIS_SERVERS             = "${local.redis_endpoint}:6379",
    EVENTUATE_REDIS_PARTITIONS          = 2,
    SPRING_PROFILES_ACTIVE              = "Redis",
    SPRING_SLEUTH_ENABLED               = "true",
    SPRING_SLEUTH_SAMPLER_PROBABILITY   = 1,
    SPRING_ZIPKIN_BASE_URL              = "http://zipkin:9411/",
    JAVA_OPTS                           = "-Xmx128m"
  }

  eventuate_env = {
    EVENTUATELOCAL_CDC_DB_USER_NAME                              = var.use_rds_and_elastic_cache ? var.rds_username : "root",
    EVENTUATELOCAL_CDC_DB_PASSWORD                               = var.use_rds_and_elastic_cache ? var.rds_pwd : "rootpassword",
    EVENTUATELOCAL_CDC_MYSQL_BINLOG_CLIENT_UNIQUE_ID             = 1234567890,
    EVENTUATELOCAL_CDC_READER_NAME                               = "MySqlReader",
    EVENTUATELOCAL_CDC_OFFSET_STORE_KEY                          = "MySqlBinlog",
    EVENTUATELOCAL_CDC_READ_OLD_DEBEZIUM_DB_OFFSET_STORAGE_TOPIC = "false"
  }

  cdc_env = merge(
    local.app_env,
    local.eventuate_env
  )
}
