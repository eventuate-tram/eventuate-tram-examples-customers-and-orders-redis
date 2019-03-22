package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.tram.consumer.jdbc.TramConsumerJdbcConfiguration;
import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({TramConsumerRedisConfiguration.class, OrderCommonConfiguration.class, TramConsumerJdbcConfiguration.class})
public class OrderRedisConfiguration {
}
