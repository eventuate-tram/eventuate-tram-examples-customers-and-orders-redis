package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence;

import io.eventuate.tram.redis.common.CommonRedisConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
@Import(CommonRedisConfiguration.class)
public class OrderHistoryServicePersistenceConfiguration {

}
