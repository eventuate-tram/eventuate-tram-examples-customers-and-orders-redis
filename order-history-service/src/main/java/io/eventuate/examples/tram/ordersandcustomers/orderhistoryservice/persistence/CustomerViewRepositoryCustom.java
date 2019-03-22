package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderState;

public interface CustomerViewRepositoryCustom {
  void createOrUpdateCustomerView(CustomerView customerView);
  void addOrder(long customerId, OrderInfo order);

  void updateState(Long customerId, Long orderId, OrderState state);
}
