package com.example.orderService;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.orderService.model.Order;
import com.example.orderService.model.OrderDTO;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

  private final OrderService orderService;

  public OrderController(OrderService orderService) {
    this.orderService = orderService;
  }

  @PostMapping
  public ResponseEntity<Order> createOrder(@RequestBody OrderDTO orderRequest) {
    Order order = orderService.placeOrder(orderRequest);
    return new ResponseEntity<>(order, HttpStatus.CREATED);
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<Order> getOrder(@PathVariable("orderId") Long orderId) {
    Optional<Order> order = orderService.getOrder(orderId);
    return order
      .map(ResponseEntity::ok)
      .orElseGet(() -> ResponseEntity.ok().build());
  }

}
