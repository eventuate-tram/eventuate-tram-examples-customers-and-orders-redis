package io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.messaging;

import io.eventuate.examples.tram.ordersandcustomers.customerservice.domain.events.CustomerCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderhistoryservice.service.OrderHistoryViewService;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderApprovedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderCreatedEvent;
import io.eventuate.examples.tram.ordersandcustomers.orderservice.domain.events.OrderRejectedEvent;
import io.eventuate.tram.events.common.DomainEvent;
import io.eventuate.tram.events.subscriber.DomainEventEnvelope;
import io.eventuate.tram.events.subscriber.DomainEventHandlers;
import io.eventuate.tram.events.subscriber.DomainEventHandlersBuilder;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderHistoryServiceEventSubscriber {

  @Autowired
  private OrderHistoryViewService orderHistoryViewService;

  public DomainEventHandlers domainEventHandlers() {
    return DomainEventHandlersBuilder
            .forAggregateType("io.eventuate.examples.tram.ordersandcustomers.customers.domain.Customer")
            .onEvent(CustomerCreatedEvent.class, this::customerCreatedEventHandler)
            .andForAggregateType("io.eventuate.examples.tram.ordersandcustomers.orders.domain.Order")
            .onEvent(OrderCreatedEvent.class, this::orderCreatedEventHandler)
            .onEvent(OrderApprovedEvent.class, this::orderApprovedEventHandler)
            .onEvent(OrderRejectedEvent.class, this::orderRejectedEventHandler)
            .build();
  }

  private void customerCreatedEventHandler(DomainEventEnvelope<CustomerCreatedEvent> domainEventEnvelope) {
    CustomerCreatedEvent customerCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.createCustomer(aggregateIdAsLong(domainEventEnvelope),
            customerCreatedEvent.getName(), customerCreatedEvent.getCreditLimit());
  }

  private void orderCreatedEventHandler(DomainEventEnvelope<OrderCreatedEvent> domainEventEnvelope) {
    OrderCreatedEvent orderCreatedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.addOrder(orderCreatedEvent.getOrderDetails().getCustomerId(),
            aggregateIdAsLong(domainEventEnvelope), orderCreatedEvent.getOrderDetails().getOrderTotal());
  }

  private void orderApprovedEventHandler(DomainEventEnvelope<OrderApprovedEvent> domainEventEnvelope) {
    OrderApprovedEvent orderApprovedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.approveOrder(orderApprovedEvent.getOrderDetails().getCustomerId(),
            aggregateIdAsLong(domainEventEnvelope));
  }

  private void orderRejectedEventHandler(DomainEventEnvelope<OrderRejectedEvent> domainEventEnvelope) {
    OrderRejectedEvent orderRejectedEvent = domainEventEnvelope.getEvent();
    orderHistoryViewService.rejectOrder(orderRejectedEvent.getOrderDetails().getCustomerId(),
            aggregateIdAsLong(domainEventEnvelope));
  }

  private <T extends DomainEvent> long aggregateIdAsLong(DomainEventEnvelope<T> domainEventEnvelope) {
    return Long.parseLong(domainEventEnvelope.getAggregateId());
  }


}
