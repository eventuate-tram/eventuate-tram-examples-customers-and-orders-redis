package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderState;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

public class OrderInfo {

  private OrderState state;
  private Long orderId;
  private Money orderTotal;

  public OrderInfo() {
  }

  public OrderInfo(Long orderId) {
    this.orderId = orderId;
  }

  public OrderInfo(Long orderId, OrderState state) {
    this.orderId = orderId;
    this.state = state;
  }


  public OrderInfo(Long orderId, Money orderTotal) {
    this.orderId = orderId;
    this.orderTotal = orderTotal;
    this.state = OrderState.PENDING;
  }

  public Long getOrderId() {
    return orderId;
  }
  public Money getOrderTotal() {
    return orderTotal;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public void setOrderTotal(Money orderTotal) {
    this.orderTotal = orderTotal;
  }

  public OrderState getState() {
    return state;
  }

  public void setState(OrderState state) {
    this.state = state;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj);
  }
}
