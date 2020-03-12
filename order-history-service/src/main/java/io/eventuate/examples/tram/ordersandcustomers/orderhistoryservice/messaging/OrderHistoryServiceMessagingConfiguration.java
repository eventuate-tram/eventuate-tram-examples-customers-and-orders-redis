package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.messaging;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryServiceServiceConfiguration;
import io.eventuate.tram.spring.consumer.common.TramNoopDuplicateMessageDetectorConfiguration;
import io.eventuate.tram.consumer.redis.EventuateTramRedisMessageConsumerConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryServiceServiceConfiguration.class,
        EventuateTramRedisMessageConsumerConfiguration.class,
        TramNoopDuplicateMessageDetectorConfiguration.class,
        TramEventSubscriberConfiguration.class})
public class OrderHistoryServiceMessagingConfiguration {

  @Bean
  public OrderHistoryServiceEventSubscriber orderHistoryServiceEventSubscriber() {
    return new OrderHistoryServiceEventSubscriber();
  }

  @Bean
  public DomainEventDispatcher orderHistoryServiceEventDispatcher(DomainEventDispatcherFactory domainEventDispatcherFactory,
                                                                  OrderHistoryServiceEventSubscriber orderHistoryServiceEventSubscriber) {

    return domainEventDispatcherFactory.make("orderHistoryServiceEvents",
            orderHistoryServiceEventSubscriber.domainEventHandlers());
  }

}
