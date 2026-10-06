package com.farmfresh.service;

import com.farmfresh.model.*;
import com.farmfresh.repository.OrderItemRepository;
import com.farmfresh.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private OrderItemRepository orderItemRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private CartService cartService;

    // =========================
    // GET ORDER (DENGAN ITEMS)
    // =========================
    public Order getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order tidak ditemukan"));
        order.setItems(orderItemRepository.findByOrderId(order.getId()));
        return order;
    }

    // =========================
    // PLACE ORDER
    // =========================
    @Transactional
    public Order placeOrder(Integer customerId, String shippingAddress) {

        List<CartService.CartItem> cartItems = cartService.getCartItems();
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Keranjang belanja kosong");
        }

        Double total = cartService.getTotal();

        Order order = new Order(customerId, total, shippingAddress);
        order.setOrderDate(LocalDateTime.now());
        order = orderRepository.save(order);

        for (CartService.CartItem cartItem : cartItems) {

            boolean stockReduced = productService.reduceStock(
                    cartItem.getProductId(),
                    cartItem.getQuantity()
            );

            if (!stockReduced) {
                throw new RuntimeException(
                        "Stok produk " + cartItem.getProductName() + " tidak mencukupi"
                );
            }

            OrderItem orderItem = new OrderItem(
                    order.getId(),
                    cartItem.getProductId(),
                    cartItem.getQuantity(),
                    cartItem.getPrice()
            );
            orderItemRepository.save(orderItem);
        }

        cartService.clearCart();
        return order;
    }

    // =========================
    // CUSTOMER ORDERS
    // =========================
    public List<Order> getCustomerOrders(Integer customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        orders.forEach(o ->
                o.setItems(orderItemRepository.findByOrderId(o.getId()))
        );
        return orders;
    }

    // =========================
    // FARMER ORDERS
    // =========================
    public List<Order> getFarmerOrders(Integer farmerId) {
        List<Order> orders = orderRepository.findOrdersByFarmerId(farmerId);
        orders.forEach(o ->
                o.setItems(orderItemRepository.findByOrderId(o.getId()))
        );
        return orders;
    }

    // =========================
    // UPDATE STATUS (ROLE BASED)
    // =========================
    @Transactional
    public void updateOrderStatus(Integer orderId, String newStatus, User user) {

        Order order = getOrderById(orderId);
        String currentStatus = order.getStatus();

        // 🔥 OOP POLYMORPHISM
        if (!user.canUpdateOrderStatus(currentStatus, newStatus)) {
            throw new RuntimeException("Perubahan status tidak valid");
        }

        order.setStatus(newStatus);
        orderRepository.save(order);
    }


    // =========================
    // CANCEL ORDER (CUSTOMER)
    // =========================
    @Transactional
    public void cancelOrder(Integer orderId, User user) {

        Order order = getOrderById(orderId);

        // hanya pemilik order
        if (!order.getCustomerId().equals(user.getId())) {
            throw new RuntimeException("Akses ditolak");
        }

        // 🔥 OOP VALIDATION
        if (!user.canCancelOrder(order.getStatus())) {
            throw new RuntimeException("Pesanan tidak bisa dibatalkan");
        }

        order.setStatus("CANCELLED");
        orderRepository.save(order);
    }

    // =========================
    // COMPLETE ORDER (CUSTOMER)
    // =========================
    @Transactional
    public void completeOrder(Integer orderId, User user) {

        Order order = getOrderById(orderId);

        // hanya pemilik order
        if (!order.getCustomerId().equals(user.getId())) {
            throw new RuntimeException("Akses ditolak");
        }

        // 🔥 POLYMORPHISM
        if (!user.canCompleteOrder(order.getStatus())) {
            throw new RuntimeException("Pesanan belum bisa diselesaikan");
        }

        order.setStatus("DELIVERED");
        orderRepository.save(order);
    }

    // =========================
    // DASHBOARD
    // =========================
    public long countTotalOrders(Integer farmerId) {
        return orderRepository.countOrdersByFarmer(farmerId);
    }

    public long countOrdersByStatus(Integer farmerId, String status) {
        return orderRepository.countOrdersByFarmerAndStatus(farmerId, status);
    }

    // =========================
    // FILTER
    // =========================
    public List<Order> filterOrders(
            Integer farmerId,
            String status,
            LocalDateTime startDate,
            LocalDateTime endDate,
            Integer orderId
    ) {

        if (orderId != null) {
            return orderRepository.findByFarmerAndOrderId(farmerId, orderId);
        }

        return orderRepository.findWithFilter(
                farmerId,
                status,
                startDate,
                endDate,
                null
        );
    }
}