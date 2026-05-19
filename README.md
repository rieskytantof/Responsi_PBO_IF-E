# Keranjang Belanja — Responsi PBO IF-E Genap 2025/2026

## Apa yang Saya Kerjakan

### 1. Penerapan Pola Arsitektur MVC

Aplikasi sebelumnya tidak memisahkan tanggung jawab — seluruh logika bisnis ditulis langsung di `Responsi.java`. Saya menerapkan pola **Model-View-Controller (MVC)**:

| Layer | File | Tanggung Jawab |
|-------|------|----------------|
| **Model** | `model/CartRepository.java` *(existing)* | Interface kontrak operasi data |
| **Model** | `model/MysqlCartRepository.java` *(baru)* | Implementasi repository dengan MySQL |
| **View** | `view/CartView.java` *(existing, tidak diubah)* | Tampilan UI — Passive View |
| **Controller** | `controller/CartController.java` *(baru)* | Logika bisnis & penghubung View ↔ Model |
| **Service** | `service/EventDiscountStrategy.java` *(baru)* | Strategi diskon 12.12 (12%) |
| **Config** | `config/DatabaseConfig.java` *(baru)* | Koneksi & inisialisasi MySQL |

### 2. Persistensi Data ke MySQL

Data sebelumnya disimpan di `FakeCartRepository` (in-memory), sehingga hilang setiap kali aplikasi ditutup. Saya membuat **`MysqlCartRepository`** yang mengimplementasikan interface `CartRepository` sehingga semua operasi (findAll, save, updateQuantity, delete) tersimpan permanen di MySQL.

### 3. Fitur Diskon Event 12.12

Sebelumnya menggunakan `NoDiscountStrategy` yang memberikan diskon Rp0. Saya membuat **`EventDiscountStrategy`** yang mengimplementasikan `DiscountStrategy` dan memberikan potongan **12%** dari subtotal secara otomatis.

### 4. Perubahan pada Responsi.java (Entry Point)

Sesuai ketentuan, hanya `Responsi.java` yang diubah:
- Mengganti `FakeCartRepository` → `MysqlCartRepository`
- Mengganti `NoDiscountStrategy` → `EventDiscountStrategy`
- Mendelegasikan seluruh logika event ke `CartController`
- Main hanya bertugas *wiring* komponen

---

## Struktur File yang Ditambahkan
