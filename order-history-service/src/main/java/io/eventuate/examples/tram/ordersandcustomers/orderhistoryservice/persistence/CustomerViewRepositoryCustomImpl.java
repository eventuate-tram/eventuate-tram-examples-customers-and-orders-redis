package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.OrderInfo;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.PartialUpdate;
import org.springframework.data.redis.core.RedisKeyValueTemplate;

public class CustomerViewRepositoryCustomImpl implements CustomerViewRepositoryCustom {

  @Autowired
  private RedisKeyValueTemplate redisKeyValueTemplate;

  @Override
  public void createOrUpdateCustomerView(CustomerView customerView) {
    redisKeyValueTemplate.update(new PartialUpdate<>(customerView.getId(), customerView));
  }

  @Override
  public void addOrder(long customerId, OrderInfo order) {
    redisKeyValueTemplate.update(new PartialUpdate<>(customerId, CustomerView.class)
            .set(keyForOrder(order.getOrderId()), order));

  }

  private String keyForOrder(Long orderId) {
    return String.format("orders.[%s]", orderId);
  }

  @Override
  public void updateState(Long customerId, Long orderId, OrderState state) {
    redisKeyValueTemplate.update(new PartialUpdate<>(customerId, CustomerView.class)
            .set(keyForOrder(orderId) + ".state", state));


  }
}
