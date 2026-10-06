package com.farmfresh.repository;

import com.farmfresh.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByCustomerId(Integer customerId);
    List<Order> findByStatus(String status);
    
    @Query("SELECT DISTINCT o FROM Order o " +
           "JOIN OrderItem oi ON o.id = oi.orderId " +
           "JOIN Product p ON oi.productId = p.id " +
           "WHERE p.farmerId = :farmerId")
    List<Order> findOrdersByFarmerId(@Param("farmerId") Integer farmerId);
    
    @Query(
       "SELECT DISTINCT o " +
       "FROM Order o " +
       "WHERE o.id IN ( " +
       "   SELECT oi.order.id " +
       "   FROM OrderItem oi " +
       "   WHERE oi.product.farmerId = :farmerId " +
       ") " +
       "AND (:status IS NULL OR o.status = :status) " +
       "AND (:startDate IS NULL OR o.orderDate >= :startDate) " +
       "AND (:endDate IS NULL OR o.orderDate <= :endDate) " +
       "AND (:orderId IS NULL OR o.id = :orderId) " +
       "ORDER BY o.orderDate DESC"
       )
       List<Order> findWithFilter(
              @Param("farmerId") Integer farmerId,
              @Param("status") String status,
              @Param("startDate") LocalDateTime startDate,
              @Param("endDate") LocalDateTime endDate,
              @Param("orderId") Integer orderId
       );



    @Query(
       "SELECT COUNT(DISTINCT o) " +
       "FROM Order o " +
       "JOIN o.items oi " +
       "JOIN oi.product p " +
       "WHERE p.farmerId = :farmerId"
       )
       long countOrdersByFarmer(
       @Param("farmerId") Integer farmerId
       );


       @Query(
       "SELECT COUNT(DISTINCT o) " +
       "FROM Order o " +
       "JOIN o.items oi " +
       "JOIN oi.product p " +
       "WHERE p.farmerId = :farmerId " +
       "AND o.status = :status"
       )
       long countOrdersByFarmerAndStatus(
       @Param("farmerId") Integer farmerId,
       @Param("status") String status
       );

       @Query(
       "SELECT DISTINCT o FROM Order o " +
       "JOIN OrderItem oi ON o.id = oi.orderId " +
       "JOIN Product p ON oi.productId = p.id " +
       "WHERE p.farmerId = :farmerId " +
       "AND o.id = :orderId"
       )
       List<Order> findByFarmerAndOrderId(
              @Param("farmerId") Integer farmerId,
              @Param("orderId") Integer orderId
       );



    @Query("SELECT o FROM Order o WHERE o.customerId = :customerId ORDER BY o.orderDate DESC")
    List<Order> findCustomerOrdersSorted(@Param("customerId") Integer customerId);
    
    @Query("SELECT o FROM Order o WHERE o.status = :status AND o.customerId = :customerId ORDER BY o.orderDate DESC")
    List<Order> findCustomerOrdersByStatus(@Param("customerId") Integer customerId, 
                                          @Param("status") String status);

}