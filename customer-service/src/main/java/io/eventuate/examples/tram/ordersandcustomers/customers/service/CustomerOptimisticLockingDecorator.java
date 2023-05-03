package io.eventuate.examples.tram.ordersandcustomers.customers.service;

import io.eventuate.tram.consumer.common.MessageHandlerDecorator;
import io.eventuate.tram.consumer.common.MessageHandlerDecoratorChain;
import io.eventuate.tram.messaging.common.SubscriberIdAndMessage;
import org.springframework.core.Ordered;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

public class CustomerOptimisticLockingDecorator implements MessageHandlerDecorator, Ordered {

  @Override
  @Retryable(value = ObjectOptimisticLockingFailureException.class,
          maxAttempts = 10,
          backoff = @Backoff(delay = 100))
  public void accept(SubscriberIdAndMessage subscriberIdAndMessage, MessageHandlerDecoratorChain messageHandlerDecoratorChain) {
    messageHandlerDecoratorChain.invokeNext(subscriberIdAndMessage);
  }

  @Override
  public int getOrder() {
    return 150;
  }
}
