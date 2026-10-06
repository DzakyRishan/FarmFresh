-- ============================================
-- DATA SQL - FARMFRESH MARKET
-- Insert Sample Data untuk E-Commerce Sayur Segar
-- ============================================
-- CATATAN: DB dipilih dari koneksi (app / argumen mysql), bukan di-hardcode di sini.
-- Semua INSERT memakai INSERT IGNORE sehingga aman dijalankan berulang (idempotent).

-- -- ============================================
-- -- HAPUS DATA LAMA (Jika ada)
-- -- ============================================
-- DELETE FROM reviews;
-- DELETE FROM order_items;
-- DELETE FROM orders;
-- DELETE FROM products;
-- DELETE FROM categories;
-- DELETE FROM users;
-- ALTER TABLE users AUTO_INCREMENT = 1;
-- ALTER TABLE products AUTO_INCREMENT = 1;
-- ALTER TABLE categories AUTO_INCREMENT = 1;
-- ALTER TABLE orders AUTO_INCREMENT = 1;
-- ALTER TABLE reviews AUTO_INCREMENT = 1;

-- ============================================
-- INSERT USERS (Pengguna)
-- ============================================
INSERT IGNORE INTO users (username, email, password, role, full_name, address, phone, created_at) VALUES
-- =================== PETANI ===================
('petani_budi', 'budi.santoso@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FARMER', 'Budi Santoso', 'Jl. Kebun Sayur No. 123, Bandung, Jawa Barat', '081234567891', '2024-01-15 09:30:00'),
('petani_siti', 'siti.rahayu@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FARMER', 'Siti Rahayu', 'Komplek Pertanian No. 45, Bogor, Jawa Barat', '081298765432', '2024-01-20 14:15:00'),
('petani_joko', 'joko.widodo@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FARMER', 'Joko Widodo', 'Perumahan Tani Makmur No. 78, Jakarta Selatan', '081112223334', '2024-02-05 10:45:00'),
('petani_eko', 'eko.prasetyo@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FARMER', 'Eko Prasetyo', 'Jl. Sawah Luwuk No. 12, Sukabumi, Jawa Barat', '081334455667', '2024-02-10 16:20:00'),

-- =================== CUSTOMER ===================
('customer_andi', 'andi.setiawan@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CUSTOMER', 'Andi Setiawan', 'Apartemen Green Garden Tower A Lt. 8 No. 801, Jakarta Pusat', '081556677889', '2024-01-25 11:10:00'),
('customer_sari', 'sari.dewi@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CUSTOMER', 'Sari Dewi', 'Jl. Konsumen Sejahtera No. 20, Bandung, Jawa Barat', '081778899001', '2024-02-01 13:45:00'),
('customer_rizky', 'rizky.ramadhan@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CUSTOMER', 'Rizky Ramadhan', 'Perumahan Bumi Asri Blok C No. 5, Surabaya, Jawa Timur', '081990011223', '2024-02-08 15:30:00'),
('customer_dewi', 'dewi.lestari@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CUSTOMER', 'Dewi Lestari', 'Jl. Mawar No. 33, Yogyakarta', '082112233445', '2024-02-15 09:15:00'),
('customer_fajar', 'fajar.nugroho@email.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'CUSTOMER', 'Fajar Nugroho', 'Komplek Bumi Permai No. 17, Semarang, Jawa Tengah', '082334455667', '2024-02-20 17:40:00'),

-- =================== ADMIN ===================
('admin_farm', 'admin@farmfreshmarket.com', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'FARMER', 'Admin FarmFresh', 'Gedung Agribisnis Lt. 5, Jl. Sudirman No. 1, Jakarta', '02112345678', '2024-01-10 08:00:00');

-- ============================================
-- INSERT CATEGORIES (Kategori Produk)
-- INSERT IGNORE: aman kalau schema.sql sudah menyisipkan kategori
-- ============================================
INSERT IGNORE INTO categories (name, description, created_at) VALUES
('SAYURAN', 'Berbagai macam sayuran segar langsung dari kebun. Dipanen setiap pagi untuk menjaga kesegaran.', '2024-01-10 08:30:00'),
('BUAH', 'Buah-buahan segar hasil panen lokal. Manis dan kaya vitamin.', '2024-01-10 08:30:00'),
('BUMBU', 'Bumbu dapur fresh untuk masakan lezat. Tanpa pengawet.', '2024-01-10 08:30:00'),
('ORGANIK', 'Produk pertanian organik bebas pestisida. Sehat dan ramah lingkungan.', '2024-01-10 08:30:00'),
('HERBAL', 'Tanaman herbal untuk kesehatan dan pengobatan tradisional.', '2024-01-10 08:30:00'),
('UMBI', 'Umbi-umbian segar sumber karbohidrat sehat.', '2024-01-10 08:30:00');

-- ============================================
-- INSERT PRODUCTS (Produk Sayuran dan Buah)
-- ============================================
INSERT IGNORE INTO products (farmer_id, name, description, price, stock, category, image_url, created_at) VALUES
-- ============ PRODUK DARI PETANI BUDI (id:1) ============
(1, 'Bayam Hijau Segar', 'Bayam hijau organik dipanen pagi hari. Kaya zat besi, vitamin A, C, dan K. Cocok untuk sop, tumis, atau jus sehat.', 6000.00, 85, 'SAYURAN', 'https://images.unsplash.com/photo-1576045057995-568f588f82fb?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-01 07:30:00'),
(1, 'Kangkung Air', 'Kangkung segar dari sawah sendiri. Daun hijau segar, batang renyah. Perfect untuk cah kangkung atau plecing.', 5500.00, 120, 'SAYURAN', 'https://images.unsplash.com/photo-1540420773420-3366772f4999?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-02 08:15:00'),
(1, 'Cabai Rawit Setan', 'Cabai rawit merah super pedas. Cocok untuk sambal, bumbu rendang, atau pelengkap mie ayam. Tingkat kepedasan level 9/10.', 28000.00, 45, 'BUMBU', 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-03 09:45:00'),
(1, 'Daun Bawang Lokal', 'Daun bawang fresh untuk taburan sop, bakso, atau martabak telur. Aromatik dan segar.', 7500.00, 95, 'BUMBU', 'https://images.unsplash.com/photo-1596040033221-a1f4f8a7c6a2?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-04 10:20:00'),
(1, 'Sawi Hijau', 'Sawi hijau segar untuk capcay, mie ayam, atau tumis. Daun hijau lebar dan batang putih renyah.', 7000.00, 110, 'SAYURAN', NULL, '2024-02-05 11:00:00'),

-- ============ PRODUK DARI PETANI SITI (id:2) ============
(2, 'Wortel Manis Import', 'Wortel impor kualitas premium. Manis, renyah, kaya beta-karoten. Cocok untuk jus, sop, atau lalapan.', 12000.00, 65, 'SAYURAN', 'https://images.unsplash.com/photo-1598170845058-78131a90f4bf?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-06 07:45:00'),
(2, 'Tomat Merah Segar', 'Tomat merah segar untuk salad, sambal, atau masakan. Daging tebal, biji sedikit, kaya antioksidan lycopene.', 15000.00, 80, 'SAYURAN', 'https://images.unsplash.com/photo-1592924357228-91a4daadcfea?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', '2024-02-07 08:30:00'),
(2, 'Brokoli Organik', 'Brokoli organik fresh dari perkebunan sendiri. Kaya serat, vitamin C, dan antioksidan. Cocok untuk menu diet.', 25000.00, 40, 'ORGANIK', 'https://images.unsplash.com/photo-1459411621453-7b03977f4bfc?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-08 09:15:00'),
(2, 'Bawang Merah Lokal', 'Bawang merah kualitas terbaik untuk bumbu dasar masakan Indonesia. Aroma kuat, rasa khas.', 22000.00, 150, 'BUMBU', 'https://images.unsplash.com/photo-1562887189-e5d078343de4?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-09 10:00:00'),
(2, 'Bawang Putih', 'Bawang putih fresh untuk bumbu masakan. Siung besar, aroma kuat, khasiat kesehatan tinggi.', 18000.00, 200, 'BUMBU', NULL, '2024-02-10 10:45:00'),

-- ============ PRODUK DARI PETANI JOKO (id:3) ============
(3, 'Selada Hijau Romaine', 'Selada hijau romaine segar untuk burger, salad Caesar, atau lalapan. Daun crunchy dan segar.', 10000.00, 55, 'SAYURAN', 'https://images.unsplash.com/photo-1540420773420-3366772f4999?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', '2024-02-11 07:20:00'),
(3, 'Pakcoy Baby', 'Pakcoy baby organik untuk capcay atau tumis. Ukuran kecil, tekstur lembut, rasa manis.', 12000.00, 70, 'ORGANIK', 'https://images.unsplash.com/photo-1566385101042-1a0f0c126a96?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-12 08:10:00'),
(3, 'Jahe Merah Emprit', 'Jahe merah segar untuk wedang jahe, bumbu masakan, atau jamu. Rasa pedas kuat, khasiat menghangatkan badan.', 15000.00, 60, 'HERBAL', 'https://images.unsplash.com/photo-1574323347407-f5e1ad6d020b?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-13 09:00:00'),
(3, 'Kunyit Segar', 'Kunyit fresh untuk jamu kunyit asam atau bumbu kuning. Warna orange pekat, aroma khas.', 13000.00, 50, 'HERBAL', 'https://images.unsplash.com/photo-1587049633312-d628ae50a8ae?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-14 09:50:00'),
(3, 'Lengkuas', 'Lengkuas segar untuk bumbu rendang, sayur lodeh, atau tom yum. Aroma khas, rasa sedikit pedas.', 9000.00, 75, 'BUMBU', NULL, '2024-02-15 10:40:00'),

-- ============ PRODUK DARI PETANI EKO (id:4) ============
(4, 'Kentang Segar', 'Kentang fresh untuk perkedel, sop, atau kentang goreng. Ukuran seragam, tekstur lembut.', 14000.00, 90, 'UMBI', 'https://images.unsplash.com/photo-1518977676601-b53f82aba655?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-16 07:35:00'),
(4, 'Ubi Jalar Orange', 'Ubi jalar orange manis alami. Kaya vitamin A, cocok untuk kolak, kue, atau dikukus.', 11000.00, 85, 'UMBI', 'https://images.unsplash.com/photo-1566389021047-39a4d2d98de0?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80', '2024-02-17 08:25:00'),
(4, 'Serai Wangi', 'Serai fresh untuk bumbu sop, tom yum, atau wedang serai. Aroma wangi khas.', 6000.00, 100, 'HERBAL', 'https://images.unsplash.com/photo-1562887189-e5d078343de4?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=60', '2024-02-18 09:15:00'),
(4, 'Daun Jeruk Purut', 'Daun jeruk purut untuk bumbu pecel, soto, atau kari. Aroma citrus segar.', 5000.00, 120, 'BUMBU', NULL, '2024-02-19 10:05:00'),
(4, 'Cabe Merah Keriting', 'Cabe merah keriting untuk sambal goreng atau bumbu rendang. Tidak terlalu pedas, warna merah cerah.', 20000.00, 65, 'BUMBU', NULL, '2024-02-20 10:55:00'),

-- ============ PRODUK LAINNYA ============
(1, 'Kacang Panjang', 'Kacang panjang segar untuk tumis atau lalapan. Polong panjang, biji kecil.', 8000.00, 95, 'SAYURAN', NULL, '2024-02-21 11:30:00'),
(2, 'Terong Ungu', 'Terong ungu fresh untuk balado, sambal terong, atau lalapan. Daging tebal, biji sedikit.', 9000.00, 70, 'SAYURAN', NULL, '2024-02-22 12:15:00'),
(3, 'Timun Suri', 'Timun suri segar untuk es buah atau rujak. Daging tebal, biji kecil, rasa manis.', 13000.00, 45, 'BUAH', NULL, '2024-02-23 13:00:00'),
(4, 'Pepaya California', 'Pepaya california matang pohon. Daging merah, manis, tekstur lembut. Kaya vitamin C dan enzim papain.', 18000.00, 35, 'BUAH', NULL, '2024-02-24 13:45:00'),
(1, 'Jeruk Nipis', 'Jeruk nipis segar untuk sambal, sop, atau minuman. Air banyak, asam segar.', 12000.00, 80, 'BUAH', NULL, '2024-02-25 14:30:00');

-- ============================================
-- INSERT ORDERS (Pesanan Sample)
-- ============================================
INSERT IGNORE INTO orders (customer_id, total_amount, status, shipping_address, notes, order_date) VALUES
-- Order dari Andi (customer_id: 5)
(5, 97000.00, 'DELIVERED', 'Apartemen Green Garden Tower A Lt. 8 No. 801, Jakarta Pusat', 'Tolong dikemas rapi dan dikirim sebelum jam 10 pagi', '2024-02-10 09:15:00'),
(5, 55000.00, 'SHIPPED', 'Kantor: Gedung Sudirman Tower B Lt. 15, Jl. Sudirman No. 52, Jakarta', 'Kirim ke resepsionis, atas nama Andi', '2024-02-18 14:30:00'),

-- Order dari Sari (customer_id: 6)
(6, 120000.00, 'PROCESSED', 'Jl. Konsumen Sejahtera No. 20, Bandung, Jawa Barat', 'Hati-hati dalam pengemasan, ada barang mudah rusak', '2024-02-15 11:45:00'),
(6, 75000.00, 'PENDING', 'Jl. Konsumen Sejahtera No. 20, Bandung, Jawa Barat', 'Bayar di tempat (COD)', '2024-02-22 16:20:00'),

-- Order dari Rizky (customer_id: 7)
(7, 88000.00, 'DELIVERED', 'Perumahan Bumi Asri Blok C No. 5, Surabaya, Jawa Timur', 'Terima kasih', '2024-02-12 13:10:00'),
(7, 45000.00, 'SHIPPED', 'Kantor: Jl. Raya Darmo No. 89, Surabaya', 'Kirim ke bagian gudang', '2024-02-20 10:05:00'),

-- Order dari Dewi (customer_id: 8)
(8, 65000.00, 'DELIVERED', 'Jl. Mawar No. 33, Yogyakarta', 'Dikemas dengan baik ya', '2024-02-14 15:40:00'),

-- Order dari Fajar (customer_id: 9)
(9, 110000.00, 'PROCESSED', 'Komplek Bumi Permai No. 17, Semarang, Jawa Tengah', 'Prioritas pengiriman', '2024-02-19 12:25:00');

-- ============================================
-- INSERT ORDER ITEMS (Detail Item Pesanan)
-- ============================================
INSERT IGNORE INTO order_items (order_id, product_id, quantity, price_at_time) VALUES
-- Order 1 (Andi - DELIVERED)
(1, 1, 3, 6000.00),   -- Bayam 3kg
(1, 3, 2, 28000.00),  -- Cabai Rawit 2kg
(1, 6, 2, 12000.00),  -- Wortel 2kg
(1, 8, 1, 22000.00),  -- Bawang Merah 1kg

-- Order 2 (Andi - SHIPPED)
(2, 2, 5, 5500.00),   -- Kangkung 5kg
(2, 5, 3, 7000.00),   -- Sawi Hijau 3kg
(2, 10, 1, 18000.00), -- Bawang Putih 1kg

-- Order 3 (Sari - PROCESSED)
(3, 7, 3, 15000.00),  -- Tomat 3kg
(3, 9, 2, 25000.00),  -- Brokoli 2kg
(3, 11, 2, 10000.00), -- Selada 2kg
(3, 13, 1, 15000.00), -- Jahe Merah 1kg

-- Order 4 (Sari - PENDING)
(4, 4, 4, 7500.00),   -- Daun Bawang 4kg
(4, 12, 3, 12000.00), -- Pakcoy 3kg
(4, 14, 1, 13000.00), -- Kunyit 1kg

-- Order 5 (Rizky - DELIVERED)
(5, 15, 2, 9000.00),  -- Lengkuas 2kg
(5, 16, 4, 14000.00), -- Kentang 4kg
(5, 17, 1, 11000.00), -- Ubi Jalar 1kg
(5, 18, 2, 6000.00),  -- Serai 2kg

-- Order 6 (Rizky - SHIPPED)
(6, 19, 3, 5000.00),  -- Daun Jeruk 3kg
(6, 20, 1, 20000.00), -- Cabe Merah 1kg
(6, 21, 2, 8000.00),  -- Kacang Panjang 2kg

-- Order 7 (Dewi - DELIVERED)
(7, 22, 3, 9000.00),  -- Terong 3kg
(7, 23, 2, 13000.00), -- Timun Suri 2kg
(7, 24, 1, 18000.00), -- Pepaya 1kg

-- Order 8 (Fajar - PROCESSED)
(8, 25, 4, 12000.00), -- Jeruk Nipis 4kg
(8, 1, 2, 6000.00),   -- Bayam 2kg
(8, 6, 3, 12000.00),  -- Wortel 3kg
(8, 8, 2, 22000.00);  -- Bawang Merah 2kg

-- ============================================
-- SET IMAGE_URL KE FILE GAMBAR LOKAL (static/image/<file>)
-- Template memakai @{/image/{img}(img=${product.imageUrl})}, jadi image_url
-- harus berisi NAMA FILE lokal, bukan URL eksternal. Idempotent (by name).
-- ============================================
UPDATE products SET image_url='bawang-merah.jpg' WHERE name='Bawang Merah Lokal';
UPDATE products SET image_url='bawang-putih.jpg' WHERE name='Bawang Putih';
UPDATE products SET image_url='bayam-hijau-segar.jpg' WHERE name='Bayam Hijau Segar';
UPDATE products SET image_url='brokoli-organik.jpg' WHERE name='Brokoli Organik';
UPDATE products SET image_url='cabai-rawit-setan.jpg' WHERE name='Cabai Rawit Setan';
UPDATE products SET image_url='cabe-merah-keriting.jpg' WHERE name='Cabe Merah Keriting';
UPDATE products SET image_url='daun-bawang.jpg' WHERE name='Daun Bawang Lokal';
UPDATE products SET image_url='daun-jeruk-purut.jpg' WHERE name='Daun Jeruk Purut';
UPDATE products SET image_url='jahe-merah-emprit.jpg' WHERE name='Jahe Merah Emprit';
UPDATE products SET image_url='jeruk-nipis.jpg' WHERE name='Jeruk Nipis';
UPDATE products SET image_url='kacang-panjang.jpg' WHERE name='Kacang Panjang';
UPDATE products SET image_url='kangkung.jpg' WHERE name='Kangkung Air';
UPDATE products SET image_url='kentang-segar.jpg' WHERE name='Kentang Segar';
UPDATE products SET image_url='kunyit-segar.jpg' WHERE name='Kunyit Segar';
UPDATE products SET image_url='lengkuas.jpg' WHERE name='Lengkuas';
UPDATE products SET image_url='pakcoy-baby.jpg' WHERE name='Pakcoy Baby';
UPDATE products SET image_url='pepaya-california.jpg' WHERE name='Pepaya California';
UPDATE products SET image_url='sawi-hijau.jpg' WHERE name='Sawi Hijau';
UPDATE products SET image_url='selada-hijau.jpg' WHERE name='Selada Hijau Romaine';
UPDATE products SET image_url='serai-wangi.jpg' WHERE name='Serai Wangi';
UPDATE products SET image_url='terong-ungu.jpg' WHERE name='Terong Ungu';
UPDATE products SET image_url='timun-suri.jpg' WHERE name='Timun Suri';
UPDATE products SET image_url='tomat-merah-segar.jpg' WHERE name='Tomat Merah Segar';
UPDATE products SET image_url='ubi-jalar-orange.jpg' WHERE name='Ubi Jalar Orange';
UPDATE products SET image_url='wortel.jpg' WHERE name='Wortel Manis Import';

-- ============================================
-- INSERT REVIEWS (Ulasan Produk)
-- ============================================
INSERT IGNORE INTO reviews (product_id, customer_id, rating, comment, created_at) VALUES
-- Review untuk Bayam (product_id: 1)
(1, 5, 5, 'Bayamnya segar banget! Langsung dari kebun, daun hijau dan batang renyah. Anak-anak suka sekali.', '2024-02-11 10:30:00'),
(1, 7, 4, 'Kualitas bagus, packing rapi. Cuma agak sedikit kotor masih ada tanahnya, tapi wajar karena organik.', '2024-02-13 14:20:00'),

-- Review untuk Cabai Rawit (product_id: 3)
(3, 5, 5, 'Cabaianya PEDAS BANGET! Sesuai namanya "setan". Bikin sambal auto nangis tapi nagih.', '2024-02-11 11:15:00'),

-- Review untuk Wortel (product_id: 6)
(6, 5, 5, 'Wortel manis dan besar-besar. Warna orange cerah, segar banget. Cocok untuk jus atau mpasi bayi.', '2024-02-11 12:00:00'),
(6, 9, 4, 'Kualitas wortel bagus, cuma ada beberapa yang bengkok. Tapi rasa tetap manis dan segar.', '2024-02-20 09:45:00'),

-- Review untuk Tomat (product_id: 7)
(7, 6, 5, 'Tomat merah segar, daging tebal, biji sedikit. Perfect untuk salad dan sambal.', '2024-02-16 15:30:00'),

-- Review untuk Brokoli (product_id: 9)
(9, 6, 5, 'Brokoli organiknya keren! Bonggol hijau segar, tidak ada ulat. Anak-anak doyan sekali.', '2024-02-16 16:15:00'),

-- Review untuk Selada (product_id: 11)
(11, 6, 4, 'Selada segar dan crunchy. Cocok untuk burger homemade. Packing pakai sterofoam jadi tetap segar.', '2024-02-16 17:00:00'),

-- Review untuk Kentang (product_id: 16)
(16, 7, 5, 'Kentangnya bagus, ukuran seragam. Pas digoreng jadi perkedel, teksturnya lembut.', '2024-02-13 18:30:00'),

-- Review untuk Ubi Jalar (product_id: 17)
(17, 7, 5, 'Ubi jalar manis alami. Warna orange cerah, tekstur lembut. Enak dikukus atau dibuat kolak.', '2024-02-13 19:15:00'),

-- Review untuk Terong (product_id: 22)
(22, 8, 4, 'Terong ungu segar, kulit mulus. Enak dibalado. Cuma ada 1 buah yang agak kecil.', '2024-02-15 20:00:00'),

-- Review untuk Jeruk Nipis (product_id: 25)
(25, 9, 5, 'Jeruk nipis banyak airnya, asam segar. Cocok untuk sambal dan minuman. Aroma citrusnya wangi.', '2024-02-20 10:30:00');

-- ============================================
-- UPDATE STOCK PRODUK setelah ada order
-- ============================================
UPDATE products SET stock = stock - 3 WHERE id = 1;   -- Bayam -3 (Order 1,8)
UPDATE products SET stock = stock - 5 WHERE id = 2;   -- Kangkung -5 (Order 2)
UPDATE products SET stock = stock - 2 WHERE id = 3;   -- Cabai Rawit -2 (Order 1)
UPDATE products SET stock = stock - 4 WHERE id = 4;   -- Daun Bawang -4 (Order 4)
UPDATE products SET stock = stock - 3 WHERE id = 5;   -- Sawi Hijau -3 (Order 2)
UPDATE products SET stock = stock - 5 WHERE id = 6;   -- Wortel -5 (Order 1,8)
UPDATE products SET stock = stock - 3 WHERE id = 7;   -- Tomat -3 (Order 3)
UPDATE products SET stock = stock - 3 WHERE id = 8;   -- Bawang Merah -3 (Order 1,8)
UPDATE products SET stock = stock - 2 WHERE id = 9;   -- Brokoli -2 (Order 3)
UPDATE products SET stock = stock - 1 WHERE id = 10;  -- Bawang Putih -1 (Order 2)
UPDATE products SET stock = stock - 2 WHERE id = 11;  -- Selada -2 (Order 3)
UPDATE products SET stock = stock - 3 WHERE id = 12;  -- Pakcoy -3 (Order 4)
UPDATE products SET stock = stock - 1 WHERE id = 13;  -- Jahe Merah -1 (Order 3)
UPDATE products SET stock = stock - 1 WHERE id = 14;  -- Kunyit -1 (Order 4)
UPDATE products SET stock = stock - 2 WHERE id = 15;  -- Lengkuas -2 (Order 5)
UPDATE products SET stock = stock - 4 WHERE id = 16;  -- Kentang -4 (Order 5)
UPDATE products SET stock = stock - 1 WHERE id = 17;  -- Ubi Jalar -1 (Order 5)
UPDATE products SET stock = stock - 2 WHERE id = 18;  -- Serai -2 (Order 5)
UPDATE products SET stock = stock - 3 WHERE id = 19;  -- Daun Jeruk -3 (Order 6)
UPDATE products SET stock = stock - 1 WHERE id = 20;  -- Cabe Merah -1 (Order 6)
UPDATE products SET stock = stock - 2 WHERE id = 21;  -- Kacang Panjang -2 (Order 6)
UPDATE products SET stock = stock - 3 WHERE id = 22;  -- Terong -3 (Order 7)
UPDATE products SET stock = stock - 2 WHERE id = 23;  -- Timun Suri -2 (Order 7)
UPDATE products SET stock = stock - 1 WHERE id = 24;  -- Pepaya -1 (Order 7)
UPDATE products SET stock = stock - 4 WHERE id = 25;  -- Jeruk Nipis -4 (Order 8)

-- ============================================
-- DATA STATISTIK (Optional Views)
-- ============================================
-- Total produk per petani
SELECT 
    u.full_name as 'Nama Petani',
    COUNT(p.id) as 'Jumlah Produk',
    SUM(p.stock) as 'Total Stok',
    FORMAT(SUM(p.price * p.stock), 0) as 'Nilai Stok (Rp)'
FROM users u
LEFT JOIN products p ON u.id = p.farmer_id
WHERE u.role = 'FARMER'
GROUP BY u.id
ORDER BY COUNT(p.id) DESC;

-- Produk terlaris
SELECT 
    p.name as 'Nama Produk',
    u.full_name as 'Petani',
    p.category as 'Kategori',
    p.price as 'Harga',
    p.stock as 'Stok',
    COALESCE(SUM(oi.quantity), 0) as 'Terjual',
    COUNT(DISTINCT r.id) as 'Jumlah Review',
    COALESCE(AVG(r.rating), 0) as 'Rating Rata-rata'
FROM products p
LEFT JOIN users u ON p.farmer_id = u.id
LEFT JOIN order_items oi ON p.id = oi.product_id
LEFT JOIN reviews r ON p.id = r.product_id
GROUP BY p.id
ORDER BY COALESCE(SUM(oi.quantity), 0) DESC, p.name;

-- Ringkasan order per customer
SELECT 
    u.full_name as 'Nama Customer',
    COUNT(o.id) as 'Jumlah Order',
    FORMAT(SUM(o.total_amount), 0) as 'Total Belanja (Rp)',
    MAX(o.order_date) as 'Order Terakhir',
    GROUP_CONCAT(DISTINCT o.status ORDER BY o.status SEPARATOR ', ') as 'Status Order'
FROM users u
LEFT JOIN orders o ON u.id = o.customer_id
WHERE u.role = 'CUSTOMER'
GROUP BY u.id
HAVING COUNT(o.id) > 0
ORDER BY SUM(o.total_amount) DESC;

-- ============================================
-- DATA DUMMY UNTUK TESTING (Jika perlu lebih banyak)
-- ============================================
/*
-- Tambah produk out of stock untuk testing
INSERT INTO products (farmer_id, name, description, price, stock, category, created_at) VALUES
(1, 'Labu Kuning', 'Labu kuning untuk kolak atau kue. Stok habis untuk testing.', 14000.00, 0, 'SAYURAN', NOW()),
(2, 'Mangga Harum Manis', 'Mangga harum manis musiman. Stok habis.', 35000.00, 0, 'BUAH', NOW());

-- Tambah produk low stock
INSERT INTO products (farmer_id, name, description, price, stock, category, created_at) VALUES
(3, 'Daun Kemangi', 'Daun kemangi segar untuk lalapan pepes. Stok menipis.', 8000.00, 3, 'BUMBU', NOW()),
(4, 'Kembang Kol', 'Kembang kol organik. Stok sedikit.', 18000.00, 2, 'SAYURAN', NOW());
*/

-- ============================================
-- PESAN SUKSES
-- ============================================
SELECT '✅ DATA SAMPLE BERHASIL DIINSERT!' as 'STATUS';

SELECT 
    CONCAT('📊 TOTAL DATA:') as 'SUMMARY',
    CONCAT('- ', COUNT(*), ' Users') as 'Users'
FROM users
UNION ALL
SELECT '', CONCAT('- ', COUNT(*), ' Products') FROM products
UNION ALL
SELECT '', CONCAT('- ', COUNT(*), ' Orders') FROM orders
UNION ALL
SELECT '', CONCAT('- ', COUNT(*), ' Order Items') FROM order_items
UNION ALL
SELECT '', CONCAT('- ', COUNT(*), ' Reviews') FROM reviews;