package com.example.integrationtests.mq;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

  @Bean
  public DirectExchange orderExchange() {
    return new DirectExchange("order.exchange", true, false);
  }

  @Bean
  public Queue orderShippedQueue() {
    return new Queue("order.shipped.queue", true);
  }

  @Bean
  public Queue orderPlacedQueue() {
    return new Queue("order.placed.queue", true);
  }

  @Bean
  public Binding bindingShippedQueue(Queue orderShippedQueue, DirectExchange orderExchange) {
    return BindingBuilder.bind(orderShippedQueue).to(orderExchange).with("order.shipped");
  }

  @Bean
  public Binding bindingPlacedQueue(Queue orderPlacedQueue, DirectExchange orderExchange) {
    return BindingBuilder.bind(orderPlacedQueue).to(orderExchange).with("order.placed");
  }

  @Bean
  public Jackson2JsonMessageConverter converter() {
    return new Jackson2JsonMessageConverter();
  }

}
