package com.example.integrationtests.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@AllArgsConstructor
@Getter
public class OrderItemDTO {
    private String name;
    private Integer qty;
    private BigDecimal price;

    public BigDecimal getAmount() {
        return price.multiply(BigDecimal.valueOf(qty));
    }
}
