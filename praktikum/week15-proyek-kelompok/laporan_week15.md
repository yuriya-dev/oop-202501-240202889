# Laporan Praktikum Minggu 15: Proyek Kelompok Terintegrasi

Topik: **Agri-POS - Sistem Point of Sale Terintegrasi dengan JavaFX, PostgreSQL, dan Arsitektur MVC**

## Identitas Kelompok
- **Nama Kelompok**: Agri-POS Development Team
- **Anggota**:
  1. Wahyu Tri Cahya (240202889) - Lead Developer, Backend Architecture
  2. [Nama Anggota 2] - Frontend Developer
  3. [Nama Anggota 3] - Database & Testing
  4. [Nama Anggota 4] - Documentation & QA
- **Kelas**: 3IKRB

---

## Tujuan Pembelajaran
Setelah menyelesaikan proyek ini, kelompok mampu:
1. ✅ Merancang sistem terintegrasi menggunakan UML (Use Case, Class, Sequence Diagram)
2. ✅ Mengimplementasikan aplikasi dengan arsitektur MVC dan SOLID principles
3. ✅ Mengintegrasikan JavaFX (GUI) dengan PostgreSQL (Database) via JDBC
4. ✅ Menerapkan design pattern (Singleton, Strategy, DAO) dalam kode
5. ✅ Membuat unit test dan integration test dengan bukti eksekusi
6. ✅ Mendokumentasikan sistem dengan lengkap dan jelas

---

## Ringkasan Sistem
**Agri-POS** adalah sistem Point of Sale (POS) untuk perdagangan pertanian yang mencakup:
- 🛍️ **Transaksi Penjualan**: Keranjang belanja interaktif dengan real-time total calculation
- 📦 **Manajemen Produk**: CRUD produk dengan 5 atribut (kode, nama, kategori, harga, stok)
- 💳 **Metode Pembayaran**: Tunai & E-Wallet (extensible via Strategy Pattern)
- 🧾 **Struk & Laporan**: Auto-generate receipt dan dashboard laporan untuk admin
- 🔐 **Autentikasi**: Login dengan dua role (Kasir & Admin) dengan hak akses berbeda

**Teknologi Stack**:
- **Frontend**: JavaFX (Theme: green gradient, responsive design)
- **Backend**: Java 17 (MVC Architecture)
- **Database**: PostgreSQL dengan JDBC PreparedStatement
- **Build Tool**: Maven 3.6+
- **Testing**: JUnit 4 (Unit Test)
---

## Dasar Teori
Proyek ini mengintegrasikan konsep-konsep OOP dari Bab 1-14:

1. **Encapsulation** (Bab 2): Model classes dengan getter/setter
2. **Inheritance** (Bab 3): CartItem extends dari base domain object
3. **Polymorphism** (Bab 4): PaymentMethod interface dengan multiple implementasi
4. **Abstract & Interface** (Bab 5): IProductDAO, IUserDAO sebagai contract
5. **SOLID Principles** (Bab 6): 
   - SRP: Tiap class satu tanggung jawab (Service, DAO, View terpisah)
   - OCP: Strategy Pattern untuk payment methods
   - LSP: DAO implementasi sesuai interface contract
   - ISP: Interface khusus untuk setiap domain
   - DIP: Service bergantung pada DAO interface
6. **Collections** (Bab 7): ArrayList untuk Cart items dan product list
7. **Exception Handling** (Bab 9): Custom exceptions (ValidationException, OutOfStockException)
8. **Design Pattern** (Bab 10): Singleton, Strategy, DAO, MVC
9. **DAO + JDBC** (Bab 11): PreparedStatement untuk akses data aman
10. **JavaFX GUI** (Bab 12-13): Table view, buttons, text fields, stage management

---

## Desain Sistem

### Arsitektur Berlapis (Layered Architecture)
```
┌──────────────────────────────────┐
│     Presentation Layer (View)    │
│  LoginView, KasirView, AdminView │
└────────────────┬─────────────────┘
                 │
┌────────────────▼─────────────────┐
│  Business Logic Layer (Service)  │
│  ProductService, CartService,    │
│  TransactionService, AuthService │
└────────────────┬─────────────────┘
                 │
┌────────────────▼─────────────────┐
│   Data Access Layer (DAO/JDBC)   │
│  ProductDAOImpl, UserDAOImpl       │
└────────────────┬─────────────────┘
                 │
┌────────────────▼─────────────────┐
│    Database Layer (PostgreSQL)   │
│  users, products, transactions   │
└──────────────────────────────────┘
```

### Design Pattern Digunakan

#### 1️⃣ Singleton Pattern (DatabaseConnection)
```java
// Single instance koneksi database
DatabaseConnection dbConn = DatabaseConnection.getInstance();
Connection conn = dbConn.getConnection();
```
✅ Benefit: Resource efficiency, thread-safe connection pool

#### 2️⃣ Strategy Pattern (Payment Methods)
```java
PaymentMethod payment;
if (paymentType.equals("TUNAI")) {
    payment = new CashPayment();
} else {
    payment = new EWalletPayment();
}
boolean success = payment.processPayment(amount, total);
```
✅ Benefit: OCP compliance, mudah menambah metode pembayaran baru

#### 3️⃣ DAO Pattern (Data Access)
```java
// Service tidak langsung akses DB, gunakan interface
IProductDAO productDAO = new ProductDAOImpl();
List<Product> products = productDAO.getAll();
```
✅ Benefit: Decoupling, easier to test, mockable

#### 4️⃣ MVC Pattern (Application)
- **Model**: Product, Cart, User, Transaction
- **View**: JavaFX UI components
- **Controller**: AppJavaFX coordinating logic

---

## Functional Requirements Implementation

### FR-1: Manajemen Produk ✅
**Atribut Produk**:
```java
public class Product {
    int id;           // Primary Key
    String kode;      // Kode unik produk
    String nama;      // Nama deskriptif
    String kategori;  // Kategorisasi
    double harga;     // Harga satuan
    int stok;         // Jumlah tersedia
}
```

**Operations**:
| Operasi | Kelas | Method |
|---------|-------|--------|
| Create | ProductService | addProduct(kode, nama, kategori, harga, stok) |
| Read | ProductService | getAllProducts(), getProductById(id) |
| Update | ProductService | updateProduct(id, nama, ...) |
| Delete | ProductService | deleteProduct(id) |
| Search | ProductService | searchProducts(nama), getByCategory(kategori) |

### FR-2: Transaksi Penjualan ✅
**Alur**:
1. Kasir pilih produk dari tabel
2. Input quantity dan klik "Tambah ke Keranjang"
3. CartService validate stok dan add ke Cart
4. Real-time update total
5. Ulangi atau lanjut checkout

**Implementasi**:
```java
public class CartService {
    public void addToCart(Product product, int quantity)
        throws ValidationException, OutOfStockException {
        // Validasi quantity > 0
        // Validasi stok cukup
        cart.addItem(product, quantity);
    }
    
    public double getCartTotal() {
        return cart.getTotal();  // Hitung otomatis
    }
}
```

### FR-3: Metode Pembayaran ✅
**Strategy Pattern untuk extensibility**:
```java
public interface PaymentMethod {
    boolean processPayment(double jumlahBayar, double totalBelanja);
    double calculateChange(double jumlahBayar, double totalBelanja);
    boolean validatePayment(double jumlahBayar, double totalBelanja);
}

// Implementasi 1: Tunai
public class CashPayment implements PaymentMethod {
    public double calculateChange(double bayar, double total) {
        return bayar - total;  // Ada kembalian
    }
}

// Implementasi 2: E-Wallet
public class EWalletPayment implements PaymentMethod {
    public double calculateChange(double bayar, double total) {
        return 0;  // Tidak ada kembalian
    }
}
```

### FR-4: Struk & Laporan ✅
**Struk dibuat otomatis setelah pembayaran**:
```
========================================
           AGRI-POS RECEIPT
========================================
Tanggal: 13-01-2026 14:30:45
----------------------------------------
Item:
Beras Putih       2 x    50000 =   100000
Jagung Manis      1 x    30000 =    30000
----------------------------------------
Total:                        130000
Metode:                TUNAI
Bayar:                        150000
Kembalian:                     20000
----------------------------------------
     Terima Kasih Atas Belanja Anda
========================================
```

### FR-5: Login & Hak Akses ✅
**Default Users**:
```
┌──────────────────────────────────────┐
│  Role   │ Username │ Password │ Role │
├─────────┼──────────┼──────────┼──────┤
│ Kasir   │ kasir01  │ kasir123 │ CSR  │
│ Admin   │ admin01  │ admin123 │ ADM  │
└──────────────────────────────────────┘
```

**Access Control**:
| Fitur | Kasir | Admin |
|-------|-------|-------|
| Transaksi | ✅ | ❌ |
| Manajemen Produk | ❌ | ✅ |
| Laporan | ❌ | ✅ |
| Logout | ✅ | ✅ |

---

## Database Design

### Schema DDL (Data Definition Language)

**File**: `sql/agripos_schema.sql`

```sql
-- Users Table
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL CHECK (role IN ('KASIR', 'ADMIN')),
    nama_lengkap VARCHAR(100) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Products Table
CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    kode VARCHAR(50) UNIQUE NOT NULL,
    nama VARCHAR(100) NOT NULL,
    kategori VARCHAR(50),
    harga NUMERIC(10, 2) NOT NULL,
    stok INT NOT NULL DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Transactions Table
CREATE TABLE transactions (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    tanggal TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_harga NUMERIC(12, 2) NOT NULL,
    metode_payment VARCHAR(20),
    jumlah_bayar NUMERIC(12, 2),
    kembalian NUMERIC(12, 2),
    status VARCHAR(20) DEFAULT 'PENDING'
);

-- Transaction Items (Detail Transaksi)
CREATE TABLE transaction_items (
    id SERIAL PRIMARY KEY,
    transaction_id INT NOT NULL REFERENCES transactions(id),
    product_id INT NOT NULL REFERENCES products(id),
    quantity INT NOT NULL,
    harga NUMERIC(10, 2) NOT NULL,
    subtotal NUMERIC(12, 2) NOT NULL
);
```

### Sample Data (Seed)
```sql
-- Default Users
INSERT INTO users VALUES
(1, 'kasir01', 'kasir123', 'KASIR', 'Budi Santoso'),
(2, 'admin01', 'admin123', 'ADMIN', 'Ahmad Wijaya');

-- Products (8 items)
INSERT INTO products (kode, nama, kategori, harga, stok) VALUES
('BRS001', 'Beras Putih Premium', 'Biji-bijian', 50000, 100),
('BRS002', 'Beras Merah Organik', 'Biji-bijian', 65000, 75),
('JGG001', 'Jagung Manis Segar', 'Sayuran', 30000, 150),
('KCG001', 'Kacang Hijau', 'Biji-bijian', 45000, 80),
('GRM001', 'Garam Halus', 'Bumbu-bumbu', 15000, 200),
('MNY001', 'Minyak Kelapa', 'Minyak', 55000, 60),
('TLR001', 'Telur Ayam Segar', 'Produk Hewan', 25000, 120),
('SYR001', 'Sirup Merah', 'Minuman', 35000, 90);
```

---

## Testing

### Unit Test: CartService
**File**: `src/test/java/com/upb/agripos/CartServiceTest.java`

**10 Test Cases**:
```
✅ TC-01: testAddItemToCart() - Add 1 item
✅ TC-02: testAddDuplicateProduct() - Merge duplicate items
✅ TC-03: testRemoveItemFromCart() - Remove specific item
✅ TC-04: testGetCartTotal() - Calculate total correctly
✅ TC-05: testUpdateItemQuantity() - Update qty
✅ TC-06: testRemoveItemByUpdatingQuantityToZero() - Remove via qty=0
✅ TC-07: testClearCart() - Empty cart
✅ TC-08: testGetTotalQuantity() - Sum all qty
✅ TC-09: testPaymentValidationCash() - Cash validation
✅ TC-10: testPaymentCalculateChangeEWallet() - E-Wallet no change
```

**Hasil Eksekusi**:
```
Tests run: 10
Passed: 10 ✅
Failed: 0
Skipped: 0
Execution time: ~150ms
```

### Manual Integration Test: End-to-End Kasir

| Test # | Scenario | Steps | Expected | Status |
|--------|----------|-------|----------|--------|
| K-01 | Login Kasir | username:kasir01, pwd:kasir123 | Masuk ke KasirView | ✅ |
| K-02 | Load Products | Open app | Tabel berisi 8 produk | ✅ |
| K-03 | Add to Cart | Pilih Beras, qty:2, klik Tambah | Item di keranjang | ✅ |
| K-04 | Update Total | Keranjang 2 items | Total = 130000 | ✅ |
| K-05 | Checkout Tunai | Bayar 150000, method TUNAI | Struk + kembalian 20000 | ✅ |
| K-06 | Stock Reduced | Check stok Beras | Berkurang dari 100 → 98 | ✅ |

### Manual Integration Test: End-to-End Admin

| Test # | Scenario | Steps | Expected | Status |
|--------|----------|-------|----------|--------|
| A-01 | Login Admin | username:admin01, pwd:admin123 | Masuk ke AdminView | ✅ |
| A-02 | View Products | Tab Manajemen | Tabel berisi produk | ✅ |
| A-03 | Add Product | Input kode, nama, harga, stok | Produk ditambah | ✅ |
| A-04 | Update Product | Select, ubah harga, klik Update | Data terupdate | ✅ |
| A-05 | Delete Product | Select, klik Hapus | Produk dihapus | ✅ |
| A-06 | Search | Cari "Beras" | Filter hanya Beras | ✅ |
| A-07 | View Report | Tab Laporan | Cards + detail text | ✅ |

---

## Package Structure

```
src/main/java/com/upb/agripos/
├── model/                    # Domain models
│   ├── Product.java
│   ├── Cart.java
│   ├── CartItem.java
│   ├── User.java
│   └── Transaction.java
├── dao/                      # Data Access Objects
│   ├── DatabaseConnection.java    (Singleton)
│   ├── IProductDAO.java           (Interface)
│   ├── ProductDAOImpl.java         (JDBC Implementation)
│   ├── IUserDAO.java              (Interface)
│   └── UserDAOImpl.java            (JDBC Implementation)
├── service/                  # Business Logic
│   ├── ProductService.java
│   ├── CartService.java
│   ├── TransactionService.java
│   ├── AuthService.java
│   ├── PaymentMethod.java         (Interface - Strategy)
│   ├── CashPayment.java           (Strategy impl)
│   └── EWalletPayment.java        (Strategy impl)
├── exception/                # Custom Exceptions
│   ├── ValidationException.java
│   ├── OutOfStockException.java
│   └── DatabaseException.java
├── view/                     # JavaFX Views
│   ├── LoginView.java
│   ├── KasirView.java
│   ├── AdminView.java
│   └── ReceiptDialog.java
└── AppJavaFX.java            # Main Application

src/test/java/com/upb/agripos/
└── CartServiceTest.java      # JUnit Test Suite

sql/
└── agripos_schema.sql        # PostgreSQL DDL + seed data

pom.xml                       # Maven Configuration
```

---

## Setup & Running

### Prerequisites
- Java 17+
- Maven 3.6+
- PostgreSQL 12+

### Database Setup
```bash
# 1. Create database
psql -U postgres
CREATE DATABASE agripos_db;

# 2. Run schema
psql -U postgres -d agripos_db -f sql/agripos_schema.sql

# 3. Verify data
SELECT COUNT(*) FROM products;  -- Should be 8
```

### Build & Run
```bash
# Clean build
mvn clean package

# Run application
mvn javafx:run

# Run tests
mvn test
```

### Login Credentials
```
KASIR:  username: kasir01   | password: kasir123
ADMIN:  username: admin01   | password: admin123
```

---

## Traceability Matrix

| Requirement | Implemented Class/Method | Test Case | Screenshot |
|-------------|------------------------|-----------|------------|
| FR-1: Produk CRUD | ProductService.* / ProductDAOImpl.* | K-02, A-02, A-03 | admin_products.png |
| FR-2: Transaksi | CartService / Cart | K-03, K-04 | kasir_cart.png |
| FR-3: Pembayaran | PaymentMethod, CashPayment, EWalletPayment | TC-K4, TC-K5 | kasir_payment.png |
| FR-4: Struk | TransactionService.generateReceipt() | K-05 | kasir_receipt.png |
| FR-5: Login/Auth | AuthService / LoginView | K-01, A-01 | login_screen.png |
| Pattern: Singleton | DatabaseConnection | (Code review) | DatabaseConnection.java |
| Pattern: Strategy | PaymentMethod interface | TC-K4, TC-K5 | CashPayment.java |
| Pattern: DAO | IProductDAO / ProductDAOImpl | All K/A tests | ProductDAOImpl.java |
| Exception Handling | ValidationException, OutOfStockException | Manual tests | Service classes |
| Unit Test | CartServiceTest | 10/10 passed ✅ | junit_result.png |

---

## Code Refactoring & Cleanup

### Fitur yang Dihapus
1. **Produk Terlaku (Best-selling Product)** - Dihapus dari laporan penjualan
   - Alasan: Query kompleks dengan hasil inconsisten; priority lebih rendah
   - Perubahan:
     - `ReportService.java`: Dihapus method `getTopProduct()`
     - `ReportStatistics.java`: Dihapus field `produkTerlaku`
     - `AdminView.java`: Dihapus UI card untuk produk terlaku
     - `AppJavaFX.java`: Diperbaharui signature `updateReportStatistics()` dari 3 ke 2 parameter

### Code Quality Improvements
- Removed verbose AI-generated comments (kept only essential documentation)
- Final compilation: **BUILD SUCCESS** ✅

---

## Conclusion

✅ **Semua Functional Requirement berhasil diimplementasikan**
✅ **Arsitektur mengikuti SOLID Principles**
✅ **Design Pattern diterapkan dengan benar (Singleton, Strategy, DAO, MVC)**
✅ **Database terintegrasi dengan JDBC & DAO Pattern**
✅ **Unit Test coverage dengan 10+ test cases**
✅ **UI interaktif dengan tema hijau modern**
✅ **Dokumentasi lengkap dengan traceability**

---

**Disusun oleh**: Wahyu Tri Cahya (240202889)  
**Tanggal**: Januari 2026  
**Status**: ✅ COMPLETE
- Kendala yang dihadapi dan cara mengatasinya.  
)
---

## Kesimpulan
(Tuliskan kesimpulan dari praktikum minggu ini.  
Contoh: *Dengan menggunakan class dan object, program menjadi lebih terstruktur dan mudah dikembangkan.*)

---

## Quiz
(1. [Tuliskan kembali pertanyaan 1 dari panduan]  
   **Jawaban:** …  

2. [Tuliskan kembali pertanyaan 2 dari panduan]  
   **Jawaban:** …  

3. [Tuliskan kembali pertanyaan 3 dari panduan]  
   **Jawaban:** …  )
