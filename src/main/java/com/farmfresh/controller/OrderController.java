package com.farmfresh.controller;

import com.farmfresh.model.Order;
import com.farmfresh.model.User;
import com.farmfresh.service.CartService;
import com.farmfresh.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/orders")
public class OrderController {
    
    @Autowired
    private OrderService orderService;
    
    @Autowired
    private CartService cartService;
    
    // Customer Orders
    @GetMapping("/my-orders")
    public String myOrders(@RequestParam(required = false) String status,
                          HttpSession session, 
                          Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        List<Order> orders;
        if (status != null && !status.isEmpty()) {
            orders = orderService.getCustomerOrders(user.getId()).stream()
                .filter(order -> status.equals(order.getStatus()))
                .collect(Collectors.toList());
        } else {
            orders = orderService.getCustomerOrders(user.getId());
        }
        
        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("status", status);
        
        return "customer/orders";
    }
    
    // Checkout Page
    @GetMapping("/checkout")
    public String checkoutPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        if (cartService.getItemCount() == 0) {
            return "redirect:/cart?error=Cart is empty";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        
        return "customer/checkout";
    }
    
    // Place Order
    @PostMapping("/place")
    public String placeOrder(@RequestParam String shippingAddress,
                            HttpSession session,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        try {
            Order order = orderService.placeOrder(user.getId(), shippingAddress);
            model.addAttribute("order", order);
            model.addAttribute("success", "Order placed successfully!");
            return "customer/order-success";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/cart";
        }
    }
    
    // Farmer View Orders
    @GetMapping("/farmer")
    public String farmerOrders(
            @RequestParam(required = false) String status,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate,

            @RequestParam(required = false) String keyword,

            HttpSession session,
            Model model
    ) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }

        // 🔧 KONVERSI TANGGAL (INI KUNCI)
        LocalDateTime startDateTime = null;
        LocalDateTime endDateTime = null;

        if (startDate != null) {
            startDateTime = startDate.atStartOfDay(); // 00:00
        }
        if (endDate != null) {
            endDateTime = endDate.atTime(23, 59, 59); // 23:59
        }

        Integer orderId = null;
        if (keyword != null && keyword.matches("\\d+")) {
            orderId = Integer.parseInt(keyword);
        }

        List<Order> orders = orderService.filterOrders(
                user.getId(),
                status,
                startDateTime,
                endDateTime,
                orderId
        );

        model.addAttribute("user", user);
        model.addAttribute("orders", orders);
        model.addAttribute("status", status);
        model.addAttribute("startDate", startDate);
        model.addAttribute("endDate", endDate);
        model.addAttribute("keyword", keyword);

        long totalOrders = orderService.countTotalOrders(user.getId());
        long pendingOrders = orderService.countOrdersByStatus(user.getId(), "PENDING");
        long shippedOrders = orderService.countOrdersByStatus(user.getId(), "SHIPPED");
        long deliveredOrders = orderService.countOrdersByStatus(user.getId(), "DELIVERED");
        long processedOrders = orderService.countOrdersByStatus(user.getId(), "PROCESSED");

        model.addAttribute("totalOrders", totalOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("shippedOrders", shippedOrders);
        model.addAttribute("deliveredOrders", deliveredOrders);
        model.addAttribute("processedOrders", processedOrders);

        return "farmer/orders";
    }


    
    // Update Order Status
    @PostMapping("/{orderId}/status")
    public String updateStatus(
            @PathVariable Integer orderId,
            @RequestParam String status,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        User user = (User) session.getAttribute("user");

        if (user == null) return "redirect:/login";

        // 🔒 HANYA FARMER
        if (!"FARMER".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("error", "Akses ditolak");
            return "redirect:/orders/my-orders";
        }

        try {
            orderService.updateOrderStatus(orderId, status, user);
            redirectAttributes.addFlashAttribute("success", "Status berhasil diupdate");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/orders/farmer";
    }
    
    // View Order Detail
    @GetMapping("/{orderId}/detail")
    public String viewOrderDetail(@PathVariable Integer orderId,
                                HttpSession session,
                                Model model) {

        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            Order order = orderService.getOrderById(orderId);

            if ("CUSTOMER".equals(user.getRole())) {
                if (!order.getCustomerId().equals(user.getId())) {
                    return "redirect:/orders/my-orders";
                }
            }

            // FARMER: minimal boleh lihat (lebih ketat bisa ditambah nanti)
            model.addAttribute("order", order);
            model.addAttribute("user", user);
            return "customer/order-detail";

        } catch (Exception e) {
            return "redirect:/orders/my-orders";
        }
    }

    @PostMapping("/{id}/complete")
    public String completeOrder(@PathVariable Integer id,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) return "redirect:/login";

        try {
            orderService.completeOrder(id, user);
            redirectAttributes.addFlashAttribute(
                "success", "Pesanan ditandai selesai"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                "error", e.getMessage()
            );
        }

        return "redirect:/orders/my-orders";
    }

    
    // Cancel Order
    @GetMapping("/{orderId}/cancel")
    public String cancelOrder(@PathVariable Integer orderId,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {

        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }

        try {
            orderService.cancelOrder(orderId, user);
            redirectAttributes.addFlashAttribute(
                "success", "Pesanan berhasil dibatalkan"
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                "error", e.getMessage()
            );
        }

        return "redirect:/orders/my-orders";
    }


}