# 🌱 FarmFresh Market

> **Farm-to-consumer e-commerce platform** yang menghubungkan petani langsung dengan konsumen — memotong rantai distribusi tradisional (petani → tengkulak → pasar → konsumen) menjadi **petani → konsumen**.

<p align="left">
  <img src="https://img.shields.io/badge/Java-11-orange?logo=openjdk&logoColor=white" alt="Java 11">
  <img src="https://img.shields.io/badge/Spring%20Boot-2.7.0-brightgreen?logo=springboot&logoColor=white" alt="Spring Boot 2.7.0">
  <img src="https://img.shields.io/badge/Thymeleaf-005F0F?logo=thymeleaf&logoColor=white" alt="Thymeleaf">
  <img src="https://img.shields.io/badge/MySQL-4479A1?logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Maven-C71A36?logo=apachemaven&logoColor=white" alt="Maven">
</p>

---

## 📋 Tentang Proyek

**FarmFresh Market** adalah aplikasi web e-commerce hasil pertanian. Masalah yang diselesaikan: pada rantai distribusi konvensional, petani menjual lewat tengkulak dan pasar sebelum sampai ke konsumen — margin petani kecil, harga konsumen tinggi. FarmFresh menghilangkan perantara itu: petani mendaftarkan dan menjual produknya sendiri, konsumen membeli langsung dari sumbernya.

Aplikasi punya **dua peran pengguna** dengan dashboard masing-masing: **Petani** (penjual) dan **Konsumen** (pembeli).

---

## 🚀 Fitur

### 👨‍🌾 Petani (Penjual)
- Dashboard manajemen produk
- CRUD produk (tambah, edit, hapus) + upload foto
- Kelola stok
- Lihat & kelola pesanan masuk
- Update status pesanan

### 🛒 Konsumen (Pembeli)
- Registrasi & login
- Jelajah katalog + pencarian & filter kategori
- Detail produk + ulasan/rating
- Keranjang belanja
- Checkout & riwayat pesanan
- Kelola profil

---

## 🛠️ Tech Stack

| Lapisan | Teknologi |
|---|---|
| **Bahasa** | Java 11 |
| **Framework** | Spring Boot 2.7.0 (MVC) |
| **View / Templating** | Thymeleaf + Layout Dialect |
| **Data Access** | Spring Data JPA (Hibernate 5.6) |
| **Database** | MySQL / MariaDB |
| **Keamanan** | BCrypt password hashing (`spring-security-crypto`) |
| **Build Tool** | Maven |
| **Lainnya** | Lombok, Spring Boot DevTools |

Arsitektur mengikuti pola **MVC berlapis**: `Controller → Service → Repository → Entity`.

---

## 📁 Struktur Proyek

```
src/main/
├── java/com/farmfresh/
│   ├── config/        # Konfigurasi (BCrypt bean, resource handler)
│   ├── controller/    # HomeController, ProductController, CartController, OrderController
│   ├── service/       # UserService, ProductService, CartService, OrderService
│   ├── repository/    # Spring Data JPA repositories
│   ├── model/         # Entity: User, Product, Category, Order, OrderItem, Review
│   └── FarmFreshApplication.java
└── resources/
    ├── templates/     # View Thymeleaf (index, login, register, product, dll)
    ├── static/        # CSS, JS, gambar produk
    ├── schema.sql     # DDL (sinkron dengan entity JPA)
    ├── data.sql       # Seed data (idempotent — aman di-load ulang)
    └── application.properties
```

---

## ⚙️ Cara Menjalankan

### Prasyarat
- **JDK 11+**
- **Maven 3.9+**
- **MySQL / MariaDB** aktif (mis. via XAMPP)

### Langkah

1. **Clone repo**
   ```bash
   git clone https://github.com/DzakyRishan/FarmFresh.git
   cd FarmFresh
   ```

2. **Buat database**
   ```sql
   CREATE DATABASE farmfresh;
   ```

3. **Set kredensial database** lewat environment variable (atau sesuaikan `application.properties`):
   ```bash
   # Windows (PowerShell)
   $env:DB_USERNAME="root"
   $env:DB_PASSWORD="password_anda"
   ```

4. **Jalankan aplikasi**
   ```bash
   mvn spring-boot:run
   ```

5. **Buka di browser**

   👉 http://localhost:8080/farmfresh/

   > Skema tabel dibuat otomatis oleh Hibernate (`ddl-auto=update`). Untuk mengisi data contoh, jalankan `schema.sql` lalu `data.sql` ke database.

### 🔑 Akun Demo

Semua akun seed memakai password: **`password123`**

| Peran | Username |
|---|---|
| Konsumen | `customer_andi` |
| Petani | `petani_budi` |

> Password tersimpan ter-hash (BCrypt) di database.

---

## 🔒 Catatan Keamanan

- Password **tidak** disimpan plaintext — di-hash dengan **BCrypt**.
- Kredensial database diambil dari **environment variable**, tidak di-hardcode ke dalam repo.

---

## 📄 Lisensi

Proyek ini dibuat untuk keperluan pembelajaran & portofolio.
