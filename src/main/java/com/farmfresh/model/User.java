package com.farmfresh.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Integer id;

    @Column(unique = true, nullable = false, length = 50)
    protected String username;

    @Column(unique = true, nullable = false, length = 100)
    protected String email;

    @Column(nullable = false, length = 255)
    protected String password;

    @Column(nullable = false, length = 10)
    protected String role = "CUSTOMER";

    @Column(name = "full_name", nullable = false, length = 100)
    protected String fullName;

    @Column(length = 500)
    protected String address;

    @Column(length = 20)
    protected String phone;

    @Column(name = "created_at")
    protected LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    protected LocalDateTime updatedAt = LocalDateTime.now();

    // TAMBAHKAN INI DI User.java

    public void setId(Integer id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }


    // =====================
    // CONSTRUCTOR
    // =====================
    public User() {}

    public User(String username, String email, String password, String role, String fullName) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.role = role;
        this.fullName = fullName;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // =====================
    // GETTER & SETTER
    // =====================
    public Integer getId() { return id; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getFullName() { return fullName; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    public void setAddress(String address) { this.address = address; }
    public void setPhone(String phone) { this.phone = phone; }

    // =====================
    // 🔥 METHOD UNTUK INHERITANCE (OOP)
    // =====================

    // default: user tidak boleh cancel
    public boolean canCancelOrder(String status) {
        return false;
    }

    // default: user tidak boleh complete
    public boolean canCompleteOrder(String status) {
        return false;
    }

    // default: user tidak boleh update status
    public boolean canUpdateOrderStatus(String from, String to) {
        return false;
    }

    // =====================
    // JPA CALLBACK
    // =====================
    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
