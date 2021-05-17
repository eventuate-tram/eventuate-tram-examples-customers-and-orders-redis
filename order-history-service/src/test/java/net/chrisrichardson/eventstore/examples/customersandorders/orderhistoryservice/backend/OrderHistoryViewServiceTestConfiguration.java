package net.chrisrichardson.eventstore.examples.customersandorders.orderhistoryservice.backend;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryServiceServiceConfiguration;
import io.eventuate.util.spring.swagger.CommonSwaggerConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableAutoConfiguration(exclude = CommonSwaggerConfiguration.class)
@Import(OrderHistoryServiceServiceConfiguration.class)
public class OrderHistoryViewServiceTestConfiguration {
}
