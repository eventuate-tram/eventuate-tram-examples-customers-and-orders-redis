locals {
  cao_container_endpoints = {
    SPRING_DATASOURCE_URL   = "jdbc:mysql://mysql/eventuate",
    EVENTUATE_REDIS_SERVERS = "redis:6379"
  }

  cao_managed_endpoints = {
    SPRING_DATASOURCE_URL   = "jdbc:mysql://${aws_db_instance.mysql_instance.endpoint}/eventuate",
    EVENTUATE_REDIS_SERVERS = "redis:6379"
  }

  cao_env = {
    SPRING_DATASOURCE_USERNAME          = var.rds_username,
    SPRING_DATASOURCE_PASSWORD          = var.rds_pwd,
    SPRING_DATASOURCE_DRIVER_CLASS_NAME = "com.mysql.jdbc.Driver",
    EVENTUATE_REDIS_PARTITIONS          = 2,
    SPRING_PROFILES_ACTIVE              = "Redis",
    SPRING_SLEUTH_ENABLED               = "true",
    SPRING_SLEUTH_SAMPLER_PROBABILITY   = 1,
    SPRING_ZIPKIN_BASE_URL              = "http://zipkin:9411/",
    JAVA_OPTS                           = "-Xmx128m"
  }

  eventuatelocal_cdc_env = {
    EVENTUATELOCAL_CDC_DB_USER_NAME                              = "root",
    EVENTUATELOCAL_CDC_DB_PASSWORD                               = "rootpassword",
    EVENTUATELOCAL_CDC_MYSQL_BINLOG_CLIENT_UNIQUE_ID             = 1234567890,
    EVENTUATELOCAL_CDC_READER_NAME                               = "MySqlReader",
    EVENTUATELOCAL_CDC_OFFSET_STORE_KEY                          = "MySqlBinlog",
    EVENTUATELOCAL_CDC_READ_OLD_DEBEZIUM_DB_OFFSET_STORAGE_TOPIC = "false"
  }
}

locals {
  cao_container_env = merge(
    local.cao_container_endpoints,
    local.cao_env
  )

  cao_managed_env = merge(
    local.cao_managed_endpoints,
    local.cao_env
  )

  cdc_container_env = merge(
    local.cao_container_endpoints,
    local.cao_env,
    local.eventuatelocal_cdc_env
  )

  cdc_managed_env = merge(
    local.cao_managed_endpoints,
    local.cao_env,
    local.eventuatelocal_cdc_env
  )
}

resource "kubernetes_config_map" "customer-service-container" {
  count = var.use_rds_and_elastic_cache ? 0 : 1
  metadata {
    name = "customer-service"
  }

  data = local.cao_container_env
}

resource "kubernetes_config_map" "customer-service-managed" {
  count = var.use_rds_and_elastic_cache ? 1 : 0
  metadata {
    name = "customer-service"
  }

  data = local.cao_managed_env
}

resource "kubernetes_config_map" "order-service-container" {
  count = var.use_rds_and_elastic_cache ? 0 : 1
  metadata {
    name = "order-service"
  }

  data = local.cao_container_env
}

resource "kubernetes_config_map" "order-service-managed" {
  count = var.use_rds_and_elastic_cache ? 1 : 0
  metadata {
    name = "order-service"
  }

  data = local.cao_managed_env
}

resource "kubernetes_config_map" "order-history-service-container" {
  count = var.use_rds_and_elastic_cache ? 0 : 1
  metadata {
    name = "order-history-service"
  }

  data = local.cao_container_env
}

resource "kubernetes_config_map" "order-history-service-managed" {
  count = var.use_rds_and_elastic_cache ? 1 : 0
  metadata {
    name = "order-history-service"
  }

  data = local.cao_managed_env
}

resource "kubernetes_config_map" "cdc-service-container" {
  count = var.use_rds_and_elastic_cache ? 0 : 1
  metadata {
    name = "cdc-service"
  }

  data = local.cdc_container_env
}

resource "kubernetes_config_map" "cdc-service-managed" {
  count = var.use_rds_and_elastic_cache ? 1 : 0
  metadata {
    name = "cdc-service"
  }

  data = local.cdc_managed_env
}