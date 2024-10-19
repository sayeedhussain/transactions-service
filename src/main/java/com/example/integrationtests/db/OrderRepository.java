package com.example.integrationtests.db;

import com.example.integrationtests.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
