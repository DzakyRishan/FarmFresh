package com.farmfresh.service;

import com.farmfresh.model.Customer;
import com.farmfresh.model.Farmer;
import com.farmfresh.model.User;
import com.farmfresh.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Deteksi apakah string sudah berupa hash BCrypt
    private boolean isBCrypt(String value) {
        return value != null && (value.startsWith("$2a$")
                || value.startsWith("$2b$") || value.startsWith("$2y$"));
    }

    // =========================
    // REGISTER
    // =========================
    public User registerUser(User user) {

        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username sudah digunakan");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email sudah terdaftar");
        }

        // Hash password sebelum simpan
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return userRepository.save(user);
    }

    // =========================
    // LOGIN (dengan migrasi transparan dari plaintext lama)
    // =========================
    public Optional<User> login(String username, String password) {

        Optional<User> userOpt = userRepository.findByUsername(username);

        if (userOpt.isEmpty()) {
            return Optional.empty();
        }

        User found = userOpt.get();
        String stored = found.getPassword();
        boolean matches;

        if (isBCrypt(stored)) {
            // password sudah ter-hash: cocokkan via BCrypt
            matches = passwordEncoder.matches(password, stored);
        } else {
            // password lama masih plaintext: bandingkan langsung,
            // lalu upgrade ke BCrypt supaya tidak lagi plaintext
            matches = stored != null && stored.equals(password);
            if (matches) {
                found.setPassword(passwordEncoder.encode(password));
                userRepository.save(found);
            }
        }

        if (matches) {
            // 🔥 CONVERT KE SUBCLASS (POLYMORPHISM)
            return Optional.of(resolveUserRole(found));
        }

        return Optional.empty();
    }

    // =========================
    // 🔥 ROLE RESOLVER (OOP FACTORY)
    // =========================
    public User resolveUserRole(User user) {

        if (user == null) return null;

        if ("CUSTOMER".equals(user.getRole())) {
            Customer customer = new Customer();
            copyUser(user, customer);
            return customer;
        }

        if ("FARMER".equals(user.getRole())) {
            Farmer farmer = new Farmer();
            copyUser(user, farmer);
            return farmer;
        }

        return user;
    }

    // COPY FIELD DARI ENTITY KE SUBCLASS
    private void copyUser(User src, User dest) {
        dest.setId(src.getId());
        dest.setUsername(src.getUsername());
        dest.setEmail(src.getEmail());
        dest.setPassword(src.getPassword());
        dest.setFullName(src.getFullName());
        dest.setAddress(src.getAddress());
        dest.setPhone(src.getPhone());
        dest.setCreatedAt(src.getCreatedAt());
        dest.setUpdatedAt(src.getUpdatedAt());
    }

    // =========================
    // CRUD USER
    // =========================
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public List<User> getUsersByRole(String role) {
        return userRepository.findByRole(role);
    }

    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(Integer id) {
        userRepository.deleteById(id);
    }

    // =========================
    // PASSWORD
    // =========================
    public boolean changePassword(Integer userId, String oldPassword, String newPassword) {

        Optional<User> userOpt = userRepository.findById(userId);

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            String stored = user.getPassword();
            boolean oldMatches = isBCrypt(stored)
                    ? passwordEncoder.matches(oldPassword, stored)
                    : (stored != null && stored.equals(oldPassword));

            if (oldMatches) {
                user.setPassword(passwordEncoder.encode(newPassword));
                userRepository.save(user);
                return true;
            }
        }

        return false;
    }

    // =========================
    // FINDER
    // =========================
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}