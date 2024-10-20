package com.example.integrationtests;

import com.example.integrationtests.model.OrderDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<String> createOrder(@RequestBody OrderDTO orderRequest) {
        orderService.placeOrder(orderRequest);
        String responseMessage = "Order has been successfully created.";
        return new ResponseEntity<>(responseMessage, HttpStatus.CREATED);
    }
}

