package com.example.integrationtests.db;

import com.example.integrationtests.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    /*
    Add integration test for each repository to save and fetch entity.
    Refer OrderRepositoryTest for example. We have used @SpringbootTest instead of @DataJPATest which
    minimizes bean creation in favor of speed and therefore is less close to real application behaviour
     */

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
    Add integration test for complex JPA derived queries like below. Greater than 5 filter criteria can be considered complex.
    Below method has 8 i.e (6 params) + (paymentMethod != null) + (isGift == true)

    List<Order> findByCustomerIdAndOrderStatusInAndPaymentMethodIsNotNullAndIsGiftTrueAndTotalAmountBetweenAndCreatedAtBetween(
        String customerId,
        List<OrderStatus> statuses,
        BigDecimal minAmount,
        BigDecimal maxAmount,
        LocalDateTime startDate,
        LocalDateTime endDate
    );
     */
}
