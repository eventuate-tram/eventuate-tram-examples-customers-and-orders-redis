package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.web;

import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryServiceServiceConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan
@Import({OrderHistoryServiceServiceConfiguration.class})
public class OrderHistoryServiceWebConfiguration {


}
