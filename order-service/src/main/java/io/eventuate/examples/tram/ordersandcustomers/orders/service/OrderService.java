package io.eventuate.examples.tram.ordersandcustomers.orders.service;

import io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order;
import io.eventuate.examples.tram.ordersandcustomers.orders.domain.OrderRepository;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderDetails;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderRejectedEvent;
import io.eventuate.tram.events.publisher.DomainEventPublisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import static java.util.Collections.singletonList;

public class OrderService {

  @Autowired
  private DomainEventPublisher domainEventPublisher;

  @Autowired
  private OrderRepository orderRepository;

  @Transactional
  public Order createOrder(OrderDetails orderDetails) {
    Order order = Order.createOrder(orderDetails);
    orderRepository.save(order);
    publishOrderCreatedEvent(order);
    return order;
  }

  private void publishOrderCreatedEvent(Order order) {
    OrderCreatedEvent orderCreatedEvent = new OrderCreatedEvent(order.getOrderDetails());
    domainEventPublisher.publish(Order.class, order.getId(), singletonList(orderCreatedEvent));
  }

  public Order findOrder(Long orderId) {
    return orderRepository.findById(orderId).orElseThrow(() -> new IllegalArgumentException("Order does not exist"));
  }

  public void approveOrder(Long orderId) {
    Order order = findOrder(orderId);
    order.noteCreditReserved();
    publishOrderApprovedEvent(orderId, order);
  }

  private void publishOrderApprovedEvent(Long orderId, Order order) {
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderApprovedEvent(order.getOrderDetails())));
  }

  public void rejectOrder(Long orderId) {
    Order order = findOrder(orderId);
    order.noteCreditReservationFailed();
    publishOrderRejectedEvent(orderId, order);
  }

  private void publishOrderRejectedEvent(Long orderId, Order order) {
    domainEventPublisher.publish(Order.class,
            orderId, singletonList(new OrderRejectedEvent(order.getOrderDetails())));
  }

}
