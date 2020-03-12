package io.eventuate.examples.tram.ordersandcustomers.orders;

import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderService;
import io.eventuate.examples.tram.ordersandcustomers.orders.service.OrderServiceEventSubscriber;
import io.eventuate.tram.spring.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.events.subscriber.DomainEventDispatcherFactory;
import io.eventuate.tram.spring.events.subscriber.TramEventSubscriberConfiguration;
import io.eventuate.tram.spring.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories
@EnableAutoConfiguration
@Import({TramEventsPublisherConfiguration.class,
        TramMessageProducerJdbcConfiguration.class,
        TramEventSubscriberConfiguration.class})
public class OrderCommonConfiguration {

  @Bean
  public OrderService orderService() {
    return new OrderService();
  }


  @Bean
  public OrderServiceEventSubscriber OrderServiceEventSubscriber() {
    return new OrderServiceEventSubscriber();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(OrderServiceEventSubscriber orderServiceEventSubscriber,
                                                     DomainEventDispatcherFactory domainEventDispatcherFactory) {
    return domainEventDispatcherFactory.make("orderServiceEventSubscriber",
            orderServiceEventSubscriber.domainEventHandlers());
  }
}
