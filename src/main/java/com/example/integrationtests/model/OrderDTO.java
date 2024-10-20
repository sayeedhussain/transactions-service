package com.example.integrationtests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@Getter
public class OrderDTO {
    private Long customerId;
    private List<OrderItemDTO> items;

    public BigDecimal getAmount() {
        return items.stream()
                .map(OrderItemDTO::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}

