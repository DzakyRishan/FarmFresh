package com.farmfresh.controller;

import com.farmfresh.model.Product;
import com.farmfresh.model.User;
import com.farmfresh.service.CartService;
import com.farmfresh.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Optional;

@Controller
@RequestMapping("/cart")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @Autowired
    private ProductService productService;
    
    // View Cart
    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        model.addAttribute("user", user);
        model.addAttribute("cartItems", cartService.getCartItems());
        model.addAttribute("total", cartService.getTotal());
        model.addAttribute("itemCount", cartService.getItemCount());
        
        return "customer/cart";
    }
    
    // Add to Cart
    @PostMapping("/add")
    public String addToCart(
            @RequestParam Integer productId,
            @RequestParam(defaultValue = "1") Integer quantity,
            HttpSession session,
            RedirectAttributes redirectAttributes,
            @RequestHeader(value = "Referer", required = false) String referer
    ) {
        System.out.println("🔥 ADD TO CART FIX DIPANGGIL 🔥");

        User user = (User) session.getAttribute("user");
        if (user == null || !"CUSTOMER".equals(user.getRole())) {
            redirectAttributes.addFlashAttribute("error", "Silakan login sebagai customer");
            return "redirect:/login";
        }

        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isEmpty()) {
            redirectAttributes.addFlashAttribute("error", "Produk tidak ditemukan");
            return "redirect:" + (referer != null ? referer : "/search");
        }

        Product product = productOpt.get();

        if (product.getStock() < quantity) {
            redirectAttributes.addFlashAttribute("error", "Stok tidak mencukupi");
            return "redirect:" + (referer != null ? referer : "/search");
        }

        cartService.addToCart(product, quantity);

        redirectAttributes.addFlashAttribute("success", "Produk berhasil ditambahkan ke keranjang");

        // ⬅️ INI KUNCI UTAMA
        return "redirect:" + (referer != null ? referer : "/search");
    }

    
    // Update Quantity
    @PostMapping("/update")
    public String updateQuantity(@RequestParam Integer productId,
                                @RequestParam Integer quantity,
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isPresent() && productOpt.get().getStock() >= quantity) {
            cartService.updateQuantity(productId, quantity);
            redirectAttributes.addFlashAttribute("success", "Jumlah produk berhasil diupdate");
        } else {
            redirectAttributes.addFlashAttribute("error", "Stok tidak mencukupi");
        }
        
        return "redirect:/cart";
    }
    
    // Remove from Cart
    @GetMapping("/remove/{productId}")
    public String removeFromCart(@PathVariable Integer productId, 
                                HttpSession session,
                                RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        cartService.removeFromCart(productId);
        redirectAttributes.addFlashAttribute("success", "Produk dihapus dari keranjang");
        
        return "redirect:/cart";
    }
    
    // Clear Cart
    @GetMapping("/clear")
    public String clearCart(HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            return "redirect:/login";
        }
        
        cartService.clearCart();
        redirectAttributes.addFlashAttribute("success", "Keranjang berhasil dikosongkan");
        
        return "redirect:/cart";
    }
}