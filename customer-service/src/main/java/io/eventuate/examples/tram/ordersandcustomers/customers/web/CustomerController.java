package io.eventuate.examples.tram.ordersandcustomers.customers.web;

import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.service.CustomerService;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerRequest;
import io.eventuate.examples.tram.ordersandcustomers.customers.webapi.CreateCustomerResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {
  
  private Logger logger = LoggerFactory.getLogger(getClass());

  private CustomerService customerService;

  @Autowired
  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @RequestMapping(value = "/customers", method = RequestMethod.POST)
  public CreateCustomerResponse createCustomer(@RequestBody CreateCustomerRequest createCustomerRequest) {
    logger.info("Create customer {} with credit limit of {}", createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
    Customer customer = customerService.createCustomer(createCustomerRequest.getName(), createCustomerRequest.getCreditLimit());
    return new CreateCustomerResponse(customer.getId());
  }
}
