package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerOptimisticLockingDecorator;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerServiceEventSubscriber;
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
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@Import({TramEventsPublisherConfiguration.class,
        TramMessageProducerJdbcConfiguration.class,
        TramEventSubscriberConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableRetry
public class CustomerCommonConfiguration {

  @Bean
  public CustomerServiceEventSubscriber customerServiceEventSubscriber() {
    return new CustomerServiceEventSubscriber();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(DomainEventDispatcherFactory domainEventDispatcherFactory,
                                                     CustomerServiceEventSubscriber customerServiceEventSubscriber) {
    return domainEventDispatcherFactory.make("customerServiceEventSubscriber", customerServiceEventSubscriber.domainEventHandlers());
  }

  @Bean
  public CustomerService customerService() {
    return new CustomerService();
  }

  @Bean
  public CustomerOptimisticLockingDecorator customerOptimisticLockingDecorator() {
    return new CustomerOptimisticLockingDecorator();
  }
}
