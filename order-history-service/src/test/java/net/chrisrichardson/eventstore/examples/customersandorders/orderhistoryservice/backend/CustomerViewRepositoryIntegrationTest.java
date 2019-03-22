package net.chrisrichardson.eventstore.examples.customersandorders.orderhistoryservice.backend;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence.CustomerViewRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence.OrderHistoryServicePersistenceConfiguration;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = OrderHistoryServicePersistenceConfiguration.class,
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class CustomerViewRepositoryIntegrationTest {

  @Autowired
  private CustomerViewRepository customerViewRepository;

  private final Money creditLimit = new Money(2000);
  private final String customerName = "Fred";
  private final List<OrderInfo> originalOrders = Arrays.asList(new OrderInfo(10L, new Money(10)),
          new OrderInfo(20L, new Money(20)));
  private final OrderInfo orderInfo1 = new OrderInfo(30L, new Money(30));

  @Test
  public void shouldCreateAndFindCustomer() {

    CustomerView customer = makeCustomer();

    customerViewRepository.createOrUpdateCustomerView(customer);

    assertCustomerHasOrders(customer, Collections.emptyList());
  }

  @Test
  public void testOrderMerging() {

    CustomerView customer = createOrUpdateCustomerWithOrders(originalOrders);
    Long customerId = customer.getId();

    List<OrderInfo> addedOrders = Collections.singletonList(orderInfo1);
    addOrders(customerId, addedOrders);

    List<OrderInfo> mergedOrders = new LinkedList<>();
    mergedOrders.addAll(originalOrders);
    mergedOrders.addAll(addedOrders);

    assertCustomerHasOrders(customer, mergedOrders);

    // Test duplicate event
  }

  @Test
  public void testDuplicateCustomerCreatedEvent() {
    CustomerView customer = createOrUpdateCustomerWithOrders(Collections.emptyList());
    addOrders(customer.getId(), originalOrders);
    customerViewRepository.createOrUpdateCustomerView(customer);
    assertCustomerHasOrders(customer, originalOrders);
  }

  @Test
  public void testOrderCreatedIfDoesNotExistWhenMerging() {

    List<OrderInfo> addedOrders = Collections.singletonList(orderInfo1);
    CustomerView customer = createOrUpdateCustomerWithOrders(addedOrders);

    assertCustomerHasOrders(customer, addedOrders);
  }

  private CustomerView makeCustomer() {
    return new CustomerView(System.nanoTime(), customerName, creditLimit);
  }

  private CustomerView createOrUpdateCustomerWithOrders(List<OrderInfo> orders) {
    CustomerView customerView = makeCustomer();
    customerView.addOrders(orders);
    customerViewRepository.createOrUpdateCustomerView(customerView);
    return customerView;
  }

  private void addOrders(long customerId, Collection<OrderInfo> orders) {
    orders.forEach((order) -> customerViewRepository.addOrder(customerId, order));
  }

  private void assertCustomerHasOrders(CustomerView expected, List<OrderInfo> expectedOrders) {
    CustomerView customerView = customerViewRepository.findById(expected.getId()).get();
    Assert.assertEquals(expectedOrders.stream().collect(Collectors.toMap(OrderInfo::getOrderId, (x) -> x)), customerView.getOrders());
    assertEquals(expected.getId(), customerView.getId());
    assertEquals(expected.getName(), customerView.getName());
    assertEquals(expected.getCreditLimit(), customerView.getCreditLimit());
  }
}
