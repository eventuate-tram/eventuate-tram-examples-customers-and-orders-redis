package io.eventuate.examples.tram.ordersandcustomers.customers;

import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerOptimisticLockingDecorator;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerServiceEventSubscriber;
import io.eventuate.tram.events.publisher.TramEventsPublisherConfiguration;
import io.eventuate.tram.events.subscriber.DomainEventDispatcher;
import io.eventuate.tram.messaging.consumer.MessageConsumer;
import io.eventuate.tram.messaging.producer.jdbc.TramMessageProducerJdbcConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.retry.annotation.EnableRetry;

@Configuration
@Import({TramEventsPublisherConfiguration.class,
        TramMessageProducerJdbcConfiguration.class})
@EnableJpaRepositories
@EnableAutoConfiguration
@EnableRetry
public class CustomerCommonConfiguration {

  @Bean
  public CustomerServiceEventSubscriber customerServiceEventSubscriber() {
    return new CustomerServiceEventSubscriber();
  }

  @Bean
  public DomainEventDispatcher domainEventDispatcher(CustomerServiceEventSubscriber customerServiceEventSubscriber, MessageConsumer messageConsumer) {
    return new DomainEventDispatcher("customerServiceEventSubscriber", customerServiceEventSubscriber.domainEventHandlers(), messageConsumer);
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
