package com.example.integrationtests.db;

import com.example.integrationtests.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    //No integration test required for simple JPA derived query methods
    List<Order> findByCustomerId(Long customerId);

    //No integration test required for simple JPA derived query methods
    List<Order> findByOrderNumber(String orderNumber);

    /*
    Add integration test for complex sql queries like below
    @Query(
    "SELECT new CustomerOrderDTO( " +
    "c.customerId, c.name, o.orderId, o.orderDate, o.totalAmount) " +
    "FROM Customer c  JOIN c.orders o " +
    "WHERE c.customerId = :customerId " +
    "ORDER BY o.orderDate DESC")
    List<CustomerOrderDTO> findOrdersByCustomerId( @Param("customerId") Long customerId );
     */

    /*
    Add integration test for complex JPA derived queries like below
    List<Order> findByCustomerCustomerIdAndOrderDateAfterAndTotalAmountGreaterThanOrderByOrderDateDesc(Long customerId, LocalDate orderDate, Double minAmount);
     */
}
