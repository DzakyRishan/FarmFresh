package com.farmfresh.controller;

import com.farmfresh.model.Product;
import com.farmfresh.model.User;
import com.farmfresh.service.CartService;
import com.farmfresh.service.ProductService;
import com.farmfresh.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private CartService cartService;

    // =========================
    // HOME PAGE
    // =========================
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        model.addAttribute("user", session.getAttribute("user"));
        return "index";
    }

    // =========================
    // LOGIN
    // =========================
    @GetMapping("/login")
    public String loginPage(HttpSession session) {
        if (session.getAttribute("user") != null) {
            return "redirect:/";
        }
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        Model model,
                        RedirectAttributes redirectAttributes) {

        Optional<User> user = userService.login(username, password);
        if (user.isPresent()) {
            session.setAttribute("user", user.get());
            redirectAttributes.addFlashAttribute("success", "Login berhasil!");
            return "redirect:/";
        }

        model.addAttribute("error", "Username atau password salah!");
        return "login";
    }

    // =========================
    // REGISTER
    // =========================
    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String username,
                           @RequestParam String email,
                           @RequestParam String password,
                           @RequestParam String fullName,
                           @RequestParam String role,
                           @RequestParam(required = false) String address,
                           @RequestParam(required = false) String phone,
                           Model model,
                           RedirectAttributes redirectAttributes) {
        try {
            User user = new User(username, email, password, role, fullName);
            user.setAddress(address);
            user.setPhone(phone);
            userService.registerUser(user);

            redirectAttributes.addFlashAttribute("success", "Registrasi berhasil! Silakan login.");
            return "redirect:/login";

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("username", username);
            model.addAttribute("email", email);
            model.addAttribute("fullName", fullName);
            return "register";
        }
    }

    // =========================
    // LOGOUT
    // =========================
    @GetMapping("/logout")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Logout berhasil!");
        return "redirect:/";
    }

    // =========================
    // PRODUCT DETAIL
    // =========================
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable Integer id, Model model, HttpSession session) {
        Optional<Product> product = productService.getProductById(id);
        if (product.isPresent()) {
            model.addAttribute("product", product.get());
            model.addAttribute("user", session.getAttribute("user"));
            return "product-detail";
        }
        return "redirect:/";
    }

    // =========================
    // SEARCH
    // =========================
   @GetMapping("/search")
    public String searchProducts(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String sort,
            Model model,
            HttpSession session) {

        List<Product> products;

        if (category != null && !category.isBlank()) {
            products = productService.getProductsByCategory(category);
        } else if (keyword != null && !keyword.isBlank()) {
            products = productService.searchProducts(keyword);
        } else {
            products = productService.getAllProducts();
        }

        // ===== SORTING =====
       if (sort != null && !sort.isBlank()) {
            switch (sort) {
                case "price_asc":
                    products = products.stream()
                        .sorted((a, b) -> a.getPrice().compareTo(b.getPrice()))
                        .collect(Collectors.toList());
                    break;

                case "price_desc":
                    products = products.stream()
                        .sorted((a, b) -> b.getPrice().compareTo(a.getPrice()))
                        .collect(Collectors.toList());
                    break;

                case "name_asc":
                    products = products.stream()
                        .sorted((a, b) -> a.getName().compareToIgnoreCase(b.getName()))
                        .collect(Collectors.toList());
                    break;

                case "latest":
                    products = products.stream()
                        .sorted((a, b) -> b.getId().compareTo(a.getId()))
                        .collect(Collectors.toList());
                    break;
            }
        }


        model.addAttribute("products", products);
        model.addAttribute("selectedCategory", category);
        model.addAttribute("user", session.getAttribute("user"));

        return "search"; // atau products.html
    }


    // =========================
    // CART COUNT (GLOBAL)
    // =========================
    @ModelAttribute("cartCount")
    public Integer cartCount(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null && "CUSTOMER".equals(user.getRole())) {
            return cartService.getItemCount();
        }
        return 0;
    }

    // =========================
    // STATIC PAGES
    // =========================
    @GetMapping("/about")
    public String about(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        return "about";
    }

    @GetMapping("/contact")
    public String contact(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        return "contact";
    }

    // =========================
    // ERROR PAGE
    // =========================
    @GetMapping("/error")
    public String errorPage(HttpSession session, Model model) {
        model.addAttribute("user", session.getAttribute("user"));
        return "error/404";
    }
}