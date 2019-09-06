package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.tram.jdbcredis.TramJdbcRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramJdbcRedisConfiguration.class, OrderCommonConfiguration.class})
public class OrderRedisConfiguration {
}
