# 04 - Test Plan & Test Cases

## Test Strategy

### Testing Levels
1. **Unit Testing** - Test individual methods (JUnit)
2. **Integration Testing** - Test layer interactions
3. **System Testing** - End-to-end user flows
4. **Manual Testing** - GUI & user experience

### Testing Approach
- **Black Box Testing** - Test functionality against requirements
- **White Box Testing** - Test code logic and paths
- **Regression Testing** - Ensure fixes don't break existing features

---

## Test Plan Overview

| Test Phase | Focus Area | Automation | Effort |
|-----------|-----------|-----------|--------|
| Unit Test | Service logic, DAO methods | JUnit | 40% |
| Integration Test | Service + DAO interactions | Manual | 30% |
| System Test | End-to-end workflows | Manual | 20% |
| UAT | User acceptance, performance | Manual | 10% |

---

## Manual Test Cases

### TC-01: User Login - Valid Credentials
```
ID: TC-01
Title: User Login dengan Kredensial Valid
Precondition:
  - Database berisi user "kasir" dengan password "12345"
  - Aplikasi menampilkan login screen

Test Steps:
  1. Input username: "kasir"
  2. Input password: "12345"
  3. Click tombol "Login"

Expected Result:
  - Login berhasil
  - KasirView ditampilkan
  - Label "User: Andi Wijaya" muncul di header

Priority: CRITICAL
Status: PASS ✓
```

### TC-02: User Login - Invalid Password
```
ID: TC-02
Title: User Login dengan Password Salah
Precondition:
  - Aplikasi menampilkan login screen

Test Steps:
  1. Input username: "kasir"
  2. Input password: "wrong123"
  3. Click tombol "Login"

Expected Result:
  - Error alert ditampilkan: "Login gagal: Password salah"
  - Tetap di login screen
  - Form password dikosongkan

Priority: HIGH
Status: PASS ✓
```

### TC-03: Tambah Produk Baru
```
ID: TC-03
Title: Admin Menambah Produk Baru
Precondition:
  - User login sebagai Admin
  - Berada di tab "Manajemen Produk"
  - Database sudah memiliki kategori "Sayuran"

Test Steps:
  1. Input Kode Produk: "SAY-004"
  2. Input Nama: "Sawi Hijau"
  3. Pilih Kategori: "Sayuran" dari dropdown
  4. Input Harga: "20000"
  5. Input Stok: "50"
  6. Click "Tambah Produk"

Expected Result:
  - Success alert: "Produk berhasil ditambahkan!"
  - Form dikosongkan
  - Produk baru muncul di tabel

Priority: CRITICAL
Status: PASS ✓
```

### TC-04: Tambah Kategori Baru
```
ID: TC-04
Title: Admin Menambah Kategori Baru
Precondition:
  - User login sebagai Admin
  - Berada di tab "Manajemen Kategori"

Test Steps:
  1. Input Nama Kategori: "Umbi-umbian"
  2. Click "Tambah Kategori"

Expected Result:
  - Success alert: "Kategori 'Umbi-umbian' berhasil ditambahkan!"
  - Form dikosongkan
  - Dropdown kategori di form produk update otomatis
  - Kategori baru muncul di dropdown filter

Priority: HIGH
Status: PASS ✓
```

### TC-05: Checkout Transaksi - Pembayaran Tunai
```
ID: TC-05
Title: Kasir Melakukan Checkout Transaksi Tunai
Precondition:
  - User login sebagai Kasir
  - Berada di tab "Kasir"
  - Database memiliki produk dengan stok > 0

Test Steps:
  1. Input "Bayam" di search → Click "Cari"
  2. Select produk "Bayam Segar" dari hasil
  3. Input quantity: "3"
  4. Click "Add to Cart"
  5. Lihat cart berisi 3x Bayam (total: Rp 45.000)
  6. Click "Checkout"
  7. Select metode pembayaran: "CASH"
  8. Input jumlah bayar: "50000"
  9. Click "Proses Pembayaran"

Expected Result:
  - Transaksi diproses sukses
  - Alert ditampilkan: "Pembayaran diterima"
  - Struk ditampilkan dengan detail:
    * Tanggal, Kasir, Items, Total
    * Jumlah Bayar: Rp 50.000
    * Kembalian: Rp 5.000
  - Cart dikosongkan
  - Status transaksi: "Sukses"

Priority: CRITICAL
Status: PASS ✓
```

### TC-06: Checkout dengan Stok Tidak Cukup
```
ID: TC-06
Title: Checkout Gagal - Stok Produk Tidak Cukup
Precondition:
  - User login sebagai Kasir
  - Produk "Apel Merah" memiliki stok: 25

Test Steps:
  1. Search & add "Apel Merah" ke cart dengan quantity: 30

Expected Result:
  - Error alert ditampilkan: "Stok tidak cukup"
  - Produk tidak ditambahkan ke cart
  - Cart tetap kosong

Priority: HIGH
Status: PASS ✓
```

### TC-07: View Riwayat Transaksi dengan Filter
```
ID: TC-07
Title: Kasir View Riwayat Transaksi dengan Filter
Precondition:
  - User login sebagai Kasir
  - Database memiliki transaksi dari berbagai kasir
  - Berada di tab "Riwayat Transaksi"

Test Steps:
  1. Tabel menampilkan semua transaksi
  2. Select filter Metode: "CASH"
  3. Click "Apply Filter"

Expected Result:
  - Tabel hanya menampilkan transaksi dengan metode CASH
  - Kolom yang ditampilkan: ID, Tanggal, Kasir, Total, Metode, Status

Priority: MEDIUM
Status: PASS ✓
```

### TC-08: View Laporan Penjualan dengan Date Range
```
ID: TC-08
Title: Admin View Laporan dengan Filter Tanggal
Precondition:
  - User login sebagai Admin
  - Database memiliki transaksi di periode berbeda
  - Berada di tab "Laporan Penjualan"

Test Steps:
  1. Input Dari Tanggal: "2026-01-01"
  2. Input Sampai Tanggal: "2026-01-31"
  3. Click "Apply Filter"

Expected Result:
  - Tabel menampilkan transaksi dalam range
  - Statistics card update:
    * Total Penjualan: Rp XXX
    * Total Transaksi: N
    * Produk Terlaku: [nama produk]
  - Data dapat di-export ke Excel

Priority: HIGH
Status: PASS ✓
```

### TC-09: Export Laporan ke Excel
```
ID: TC-09
Title: Export Laporan Transaksi ke Excel
Precondition:
  - User login sebagai Admin
  - Laporan sudah di-filter dengan date range
  - Berada di tab "Laporan Penjualan"

Test Steps:
  1. Click "Download ke Excel"

Expected Result:
  - File Excel berhasil dibuat: Laporan_Transaksi_YYYYMMDD_HHmmss.xlsx
  - File berisi:
    * Header: LAPORAN TRANSAKSI PENJUALAN AGRI-POS
    * Kolom: ID, Tanggal, Kasir, Total Harga, Jumlah Bayar, Kembalian, Metode, Status
    * Format: Header berwarna, currency format, borders
    * Total row di akhir

Priority: MEDIUM
Status: PASS ✓
```

### TC-10: Logout dengan Konfirmasi
```
ID: TC-10
Title: User Logout dengan Konfirmasi Dialog
Precondition:
  - User sudah login (Kasir atau Admin)

Test Steps:
  1. Click tombol "🚪 Logout" di header
  2. Dialog konfirmasi muncul
  3. Click "Ya, Logout"

Expected Result:
  - Dialog tertutup
  - Login screen ditampilkan
  - Current user direset
  - Cart dikosongkan

Priority: MEDIUM
Status: PASS ✓
```

### TC-11: Cancel Logout
```
ID: TC-11
Title: User Cancel Logout
Precondition:
  - User sudah login
  - Dialog konfirmasi logout ditampilkan

Test Steps:
  1. Click "Tidak"

Expected Result:
  - Dialog tertutup
  - Tetap di halaman sebelumnya
  - Session tidak direset

Priority: LOW
Status: PASS ✓
```

---

## Unit Test Examples

### Test: ProductService.addProduct()
```java
@Test
public void testAddProduct_Valid() throws Exception {
    ProductService service = new ProductService();
    
    service.addProduct("TEST-001", "Test Produk", "Sayuran", 50000, 100);
    List<Product> products = service.getAllProducts();
    
    assertTrue(products.stream()
        .anyMatch(p -> p.getKode().equals("TEST-001")));
}

@Test
public void testAddProduct_DuplicateKode() {
    ProductService service = new ProductService();
    
    assertThrows(ValidationException.class, () -> {
        service.addProduct("SAY-001", "Duplicate", "Sayuran", 20000, 50);
        service.addProduct("SAY-001", "Duplicate 2", "Sayuran", 20000, 50);
    });
}
```

### Test: CartService.addItem()
```java
@Test
public void testAddItem_Valid() {
    CartService cartService = new CartService(null);
    Product product = new Product(1, "SAY-001", "Bayam", "Sayuran", 15000, 50);
    CartItem item = new CartItem(product, 3);
    
    cartService.addItem(item);
    
    assertEquals(1, cartService.getCart().getItems().size());
    assertEquals(3, cartService.getCart().getItems().get(0).getQuantity());
}

@Test
public void testAddItem_DuplicateUpdateQuantity() {
    CartService cartService = new CartService(null);
    Product product = new Product(1, "SAY-001", "Bayam", "Sayuran", 15000, 50);
    
    cartService.addItem(new CartItem(product, 2));
    cartService.addItem(new CartItem(product, 3));
    
    assertEquals(1, cartService.getCart().getItems().size());
    assertEquals(5, cartService.getCart().getItems().get(0).getQuantity());
}
```

---

## Test Coverage Goals

| Component | Target Coverage |
|-----------|-----------------|
| Service Layer | 80% |
| DAO Layer | 75% |
| Util Classes | 85% |
| Model Classes | 90% |
| Overall | 80% |

---

## Known Issues & Limitations

| Issue | Severity | Status |
|-------|----------|--------|
| Password stored plain text (tidak di-hash) | HIGH | Known limitation |
| No concurrent user support | MEDIUM | By design |
| Single database connection pool | LOW | Sufficient for desktop |

---

## Test Execution Summary

**Total Test Cases**: 11  
**Passed**: 11 ✓  
**Failed**: 0  
**Skipped**: 0  
**Pass Rate**: 100%

**Test Date**: January 14, 2026  
**Tested By**: Development Team  
**Environment**: PostgreSQL local, JavaFX 17.0.6, Java 11+
