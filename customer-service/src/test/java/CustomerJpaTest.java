import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.customers.CustomerCommonConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.customers.CustomerRedisConfiguration;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer;
import io.eventuate.examples.tram.ordersandcustomers.customers.domain.CustomerRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CustomerJpaTest.CustomerJpaTestConfiguration.class)
@ActiveProfiles("Redis")
public class CustomerJpaTest {

  private Logger logger = LoggerFactory.getLogger(getClass());

  @Configuration
  @EnableAutoConfiguration
  @Import({CustomerCommonConfiguration.class, CustomerRedisConfiguration.class})
  static public class CustomerJpaTestConfiguration {
  }

  @Autowired
  private CustomerRepository customerRepository;

  @Autowired
  private TransactionTemplate transactionTemplate;

  @Test
  public void shouldSaveAndLoadCustomer() {

    Money creditLimit = new Money("12345.00");

    long customerId = transactionTemplate.execute((ts) -> {
      Customer customer = new Customer("Fred", creditLimit);
      customerRepository.save(customer);
      return customer.getId();
    });

    logger.info("CustomerId={}", customerId);

    transactionTemplate.execute((ts) -> {
      Customer customer = customerRepository.findById(customerId).get();

      assertNotNull(customer);
      assertEquals("Fred", customer.getName());
      assertEquals(creditLimit, customer.getCreditLimit());

      customer.reserveCredit(101L, new Money(12));
      return null;
    });

    transactionTemplate.execute((ts) -> {
      Customer customer = customerRepository.findById(customerId).get();

      assertNotNull(customer);
      assertEquals("Fred", customer.getName());
      assertEquals(creditLimit, customer.getCreditLimit());

      customer.reserveCredit(102L, new Money(12));
      return null;
    });

  }

}
