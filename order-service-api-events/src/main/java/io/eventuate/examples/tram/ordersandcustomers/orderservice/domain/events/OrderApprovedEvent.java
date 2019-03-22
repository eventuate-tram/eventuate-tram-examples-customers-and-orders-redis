package io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events;

import io.eventuate.tram.events.common.DomainEvent;

public class OrderApprovedEvent implements DomainEvent {

  private OrderDetails orderDetails;

  public OrderApprovedEvent() {
  }

  public OrderApprovedEvent(OrderDetails orderDetails) {
    this.orderDetails = orderDetails;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
