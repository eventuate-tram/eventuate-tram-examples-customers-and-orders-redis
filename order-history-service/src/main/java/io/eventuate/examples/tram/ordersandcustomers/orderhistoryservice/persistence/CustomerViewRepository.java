package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.persistence;

import io.eventuate.examples.tram.ordersandcustomers.orderhistory.common.CustomerView;
import org.springframework.data.repository.CrudRepository;

public interface CustomerViewRepository extends CrudRepository<CustomerView, Long>, CustomerViewRepositoryCustom {
}
