package com.example.orderService.db;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.orderService.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

  /*
   * INFO: Add one happy path integration test for each repository to save and fetch entity. Refer OrderRepositoryTest for example. We have used @SpringbootTest instead of @DataJPATest to ensure all
   * beans are created to simulate the real application behaviour.
   */

  /*
   * INFO: No integration test required for simple JPA derived query methods.
   */
  List<Order> findByCustomerId(Long customerId);


  /*
   * INFO: No integration test required for simple JPA derived query methods.
   */
  List<Order> findByOrderNumber(String orderNumber);

  /*
   * INFO: Add one happy path integration test for complex sql queries like below (sample code).
   *
   * @Query( "SELECT new CustomerOrderDTO( " + "c.customerId, c.name, o.orderId, o.orderDate, o.totalAmount) " + "FROM Customer c  JOIN c.orders o " + "WHERE c.customerId = :customerId " +
   * "ORDER BY o.orderDate DESC") List<CustomerOrderDTO> findOrdersByCustomerId( @Param("customerId") Long customerId );
   */

  /*
   * INFO: Add one happy path integration test for complex JPA derived queries like below (sample code). > 5 filter criteria can be considered complex. Below method has 8 i.e (6 params) +
   * (paymentMethod != null) + (isGift == true)
   *
   * List<Order> findByCustomerIdAndOrderStatusInAndPaymentMethodIsNotNullAndIsGiftTrueAndTotalAmountBetweenAndCreatedAtBetween( String customerId, List<OrderStatus> statuses, BigDecimal minAmount,
   * BigDecimal maxAmount, LocalDateTime startDate, LocalDateTime endDate );
   */
}
