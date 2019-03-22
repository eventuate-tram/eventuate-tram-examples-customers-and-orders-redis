package io.eventuate.examples.tram.ordersandcustomers.orderhistory.common;

import io.eventuate.examples.tram.ordersandcustomers.commondomain.Money;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RedisHash
public class CustomerView {

  @Id
  private Long id;

  private Map<Long, OrderInfo> orders = new HashMap<>();
  private String name;
  private Money creditLimit;

  public CustomerView() {
  }

  public CustomerView(Long id) {
    this.id = id;
  }

  public CustomerView(Long id, String name, Money creditLimit) {
    this.id = id;
    this.name = name;
    this.creditLimit = creditLimit;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return id;
  }

  public Map<Long, OrderInfo> getOrders() {
    return orders;
  }

  public void addOrder(OrderInfo order) {
    orders.put(order.getOrderId(), order);
  }

  public void addOrders(Map<Long, OrderInfo> orders) {
    this.orders.putAll(orders);
  }
  public void addOrders(List<OrderInfo> orders) {
    orders.forEach(order -> this.orders.put(order.getOrderId(), order));
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public void setCreditLimit(Money creditLimit) {
    this.creditLimit = creditLimit;
  }

  public Money getCreditLimit() {
    return creditLimit;
  }
}
