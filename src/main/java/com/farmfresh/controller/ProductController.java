package com.farmfresh.controller;

import com.farmfresh.model.Order;
import com.farmfresh.model.Product;
import com.farmfresh.model.User;
import com.farmfresh.service.OrderService;
import com.farmfresh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/farmer")
public class ProductController {
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private OrderService orderService;
    
    // Dashboard Petani
    @GetMapping("/dashboard")
    public String farmerDashboard(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        List<Product> products = productService.getProductsByFarmer(user.getId());
        List<Order> orders = orderService.getFarmerOrders(user.getId());
        
        // Calculate stats
        long pendingOrders = orders.stream()
            .filter(o -> "PENDING".equals(o.getStatus()))
            .count();
        
        double totalRevenue = orders.stream()
            .filter(o -> "DELIVERED".equals(o.getStatus()))
            .mapToDouble(Order::getTotalAmount)
            .sum();
        
        // Get recent orders (last 5)
        List<Order> recentOrders = orders.stream()
            .sorted((o1, o2) -> o2.getOrderDate().compareTo(o1.getOrderDate()))
            .limit(5)
            .collect(Collectors.toList());
        
        model.addAttribute("products", products);
        model.addAttribute("orders", orders);
        model.addAttribute("recentOrders", recentOrders);
        model.addAttribute("pendingOrders", pendingOrders);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("user", user);
        
        return "farmer/dashboard";
    }
    
    // Add Product Page
    @GetMapping("/product/add")
    public String addProductPage(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }
        model.addAttribute("user", user);
        return "farmer/add-product";
    }
    
    // Add Product Process
    @PostMapping("/product/add")
    public String addProduct(@RequestParam String name,
                           @RequestParam String description,
                           @RequestParam Double price,
                           @RequestParam Integer stock,
                           @RequestParam String category,
                           @RequestParam(required = false) String imageUrl,
                           HttpSession session,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        Product product = new Product();
        product.setFarmerId(user.getId());
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        product.setImageUrl(imageUrl);
        
        Product savedProduct = productService.saveProduct(product);
        
        if (savedProduct != null) {
            redirectAttributes.addFlashAttribute("success", "Produk berhasil ditambahkan!");
            return "redirect:/farmer/dashboard";
        } else {
            model.addAttribute("error", "Gagal menambahkan produk");
            model.addAttribute("user", user);
            return "farmer/add-product";
        }
    }
    
    // Edit Product Page
    @GetMapping("/product/edit/{id}")
    public String editProductPage(@PathVariable Integer id, HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        Product product = productService.getProductById(id)
            .orElse(null);
        
        if (product == null) {
            return "redirect:/farmer/dashboard";
        }
        
        if (!product.getFarmerId().equals(user.getId())) {
            return "redirect:/farmer/dashboard";
        }
        
        model.addAttribute("product", product);
        model.addAttribute("user", user);
        return "farmer/edit-product";
    }
    
    // Edit Product Process
    @PostMapping("/product/edit/{id}")
    public String editProduct(@PathVariable Integer id,
                            @RequestParam String name,
                            @RequestParam String description,
                            @RequestParam Double price,
                            @RequestParam Integer stock,
                            @RequestParam String category,
                            @RequestParam(required = false) String imageUrl,
                            HttpSession session,
                            RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        Product product = productService.getProductById(id)
            .orElse(null);
        
        if (product == null || !product.getFarmerId().equals(user.getId())) {
            return "redirect:/farmer/dashboard";
        }
        
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setStock(stock);
        product.setCategory(category);
        if (imageUrl != null && !imageUrl.trim().isEmpty()) {
            product.setImageUrl(imageUrl);
        }
        
        Product updatedProduct = productService.updateProduct(product);
        
        if (updatedProduct != null) {
            redirectAttributes.addFlashAttribute("success", "Produk berhasil diupdate!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Gagal mengupdate produk");
        }
        
        return "redirect:/farmer/dashboard";
    }
    
    // Delete Product
    @GetMapping("/product/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, 
                               HttpSession session,
                               RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        Product product = productService.getProductById(id)
            .orElse(null);
        
        if (product == null || !product.getFarmerId().equals(user.getId())) {
            return "redirect:/farmer/dashboard";
        }
        
        productService.deleteProduct(id);
        redirectAttributes.addFlashAttribute("success", "Produk berhasil dihapus!");
        
        return "redirect:/farmer/dashboard";
    }
    
    // Update Product Stock
    @PostMapping("/product/{id}/stock")
    public String updateStock(@PathVariable Integer id,
                             @RequestParam Integer quantity,
                             HttpSession session,
                             RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null || !"FARMER".equals(user.getRole())) {
            return "redirect:/login";
        }
        
        Product product = productService.getProductById(id)
            .orElse(null);
        
        if (product == null || !product.getFarmerId().equals(user.getId())) {
            return "redirect:/farmer/dashboard";
        }
        
        if (productService.reduceStock(id, quantity)) {
            redirectAttributes.addFlashAttribute("success", "Stok berhasil dikurangi!");
        } else {
            redirectAttributes.addFlashAttribute("error", "Gagal mengurangi stok");
        }
        
        return "redirect:/farmer/dashboard";
    }
}