package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.tram.jdbcredis.TramJdbcRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramJdbcRedisConfiguration.class, CustomerCommonConfiguration.class})
public class CustomerRedisConfiguration {
}
