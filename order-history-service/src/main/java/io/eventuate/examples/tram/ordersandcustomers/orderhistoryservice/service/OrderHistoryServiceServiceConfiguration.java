package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence.OrderHistoryServicePersistenceConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({OrderHistoryServicePersistenceConfiguration.class})
public class OrderHistoryServiceServiceConfiguration {

  @Bean
  public OrderHistoryViewService orderHistoryViewService(CustomerViewRepository customerViewRepository) {
    return new OrderHistoryViewService(customerViewRepository);
  }
}
