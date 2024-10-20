package com.example.integrationtests.db;

import com.example.integrationtests.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //No integration test required for simple derived query methods
    List<Order> findByCustomerId(Long customerId);

    //No integration test required for simple derived query methods
    List<Order> findByOrderNumber(String orderNumber);
}
