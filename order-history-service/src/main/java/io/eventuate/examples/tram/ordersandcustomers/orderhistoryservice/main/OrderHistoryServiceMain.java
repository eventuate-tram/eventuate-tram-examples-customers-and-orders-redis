package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.main;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.messaging.OrderHistoryServiceMessagingConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.web.OrderHistoryServiceWebConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import({OrderHistoryServiceWebConfiguration.class, OrderHistoryServiceMessagingConfiguration.class})
public class OrderHistoryServiceMain {

  public static void main(String[] args) {
    SpringApplication.run(OrderHistoryServiceMain.class, args);
  }
}
