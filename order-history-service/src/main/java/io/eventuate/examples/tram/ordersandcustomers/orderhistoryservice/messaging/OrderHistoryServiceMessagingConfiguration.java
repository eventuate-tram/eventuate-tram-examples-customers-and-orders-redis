package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.messaging;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryServiceServiceConfiguration;
import io.eventuate.tram.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import io.eventuate.tram.consumer.redis.TramConsumerRedisConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryServiceServiceConfiguration.class, TramConsumerRedisConfiguration.class, TramNoopDuplicateMessageDetectorConfiguration.class})
public class OrderHistoryServiceMessagingConfiguration {

  @Bean
  public OrderHistoryServiceEventSubscriber orderHistoryServiceEventSubscriber() {
    return new OrderHistoryServiceEventSubscriber();
  }

  @Bean
  public DomainEventDispatcher orderHistoryServiceEventDispatcher(OrderHistoryServiceEventSubscriber orderHistoryServiceEventSubscriber,
                                                                       MessageConsumer messageConsumer) {

    return new DomainEventDispatcher("orderHistoryServiceEvents",
            orderHistoryServiceEventSubscriber.domainEventHandlers(), messageConsumer);
  }

}
