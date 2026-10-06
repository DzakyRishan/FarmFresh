package com.farmfresh.service;

import com.farmfresh.model.Product;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.SessionScope;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@SessionScope
public class CartService {
    
    public static class CartItem {
        private Integer productId;
        private String productName;
        private Integer quantity;
        private Double price;
        
        public CartItem() {}
        
        public CartItem(Integer productId, String productName, Integer quantity, Double price) {
            this.productId = productId;
            this.productName = productName;
            this.quantity = quantity;
            this.price = price;
        }
        
        public Integer getProductId() { return productId; }
        public void setProductId(Integer productId) { this.productId = productId; }
        
        public String getProductName() { return productName; }
        public void setProductName(String productName) { this.productName = productName; }
        
        public Integer getQuantity() { return quantity; }
        public void setQuantity(Integer quantity) { this.quantity = quantity; }
        
        public Double getPrice() { return price; }
        public void setPrice(Double price) { this.price = price; }
        
        public Double getSubtotal() {
            return quantity * price;
        }
    }
    
    private List<CartItem> cartItems = new ArrayList<>();
    
    public void addToCart(Product product, Integer quantity) {
        // Check if product already in cart
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(product.getId())) {
                // Update quantity
                item.setQuantity(item.getQuantity() + quantity);
                return;
            }
        }
        
        // Add new item to cart
        cartItems.add(new CartItem(
            product.getId(), 
            product.getName(), 
            quantity, 
            product.getPrice()
        ));
    }
    
    public void removeFromCart(Integer productId) {
        Iterator<CartItem> iterator = cartItems.iterator();
        while (iterator.hasNext()) {
            CartItem item = iterator.next();
            if (item.getProductId().equals(productId)) {
                iterator.remove();
                break;
            }
        }
    }
    
    public void updateQuantity(Integer productId, Integer newQuantity) {
        for (CartItem item : cartItems) {
            if (item.getProductId().equals(productId)) {
                item.setQuantity(newQuantity);
                return;
            }
        }
    }
    
    public void clearCart() {
        cartItems.clear();
    }
    
    public List<CartItem> getCartItems() {
        return new ArrayList<>(cartItems);
    }
    
    public Double getTotal() {
        double total = 0;
        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }
        return total;
    }
    
    public Integer getItemCount() {
        return cartItems.size();
    }
    
    public boolean isEmpty() {
        return cartItems.isEmpty();
    }
}