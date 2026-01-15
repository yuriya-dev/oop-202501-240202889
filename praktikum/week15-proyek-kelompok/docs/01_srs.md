# 01 - Software Requirements Specification (SRS)

## Ringkasan Masalah dan Tujuan Sistem

### Masalah
Petani dan pedagang sayuran/buah memerlukan sistem Point of Sale (POS) yang mudah digunakan untuk mengelola penjualan harian, stok produk, dan laporan transaksi dengan cepat dan akurat.

### Tujuan
Mengembangkan aplikasi desktop berbasis JavaFX yang memungkinkan:
- Kasir untuk melakukan transaksi penjualan cepat
- Admin untuk mengelola produk, kategori, user, dan laporan
- Tracking transaksi dan laporan penjualan dengan filter tanggal
- Export laporan ke format Excel

---

## Scope dan Batasan

### In Scope
✅ Sistem login multi-role (Kasir, Admin)  
✅ Manajemen produk (CRUD)  
✅ Manajemen kategori produk  
✅ Manajemen user  
✅ Keranjang belanja (Cart)  
✅ Checkout dengan metode pembayaran (Tunai, E-Wallet)  
✅ Riwayat transaksi dengan filter  
✅ Laporan penjualan dengan date range filter  
✅ Export laporan ke Excel  
✅ Pagination untuk data besar  

### Out of Scope
❌ Multi-store/branch management  
❌ Integration dengan payment gateway real  
❌ Mobile application  
❌ Cloud synchronization  
❌ Barcode scanner integration  

---

## Daftar Requirement

### Functional Requirements (FR)

| ID | Requirement | Acceptance Criteria |
|----|-----------|----|
| FR-1 | Login System | User dapat login dengan username/password sesuai role (Kasir/Admin), dan sistem hanya menampilkan menu sesuai role |
| FR-2 | Product Management | Admin dapat CRUD produk (Kode, Nama, Kategori, Harga, Stok) dan sistem update stok otomatis saat checkout |
| FR-3 | Category Management | Admin dapat menambah/hapus kategori, dropdown kategori update otomatis di form produk |
| FR-4 | User Management | Admin dapat CRUD user dengan role assignment (Kasir/Admin) |
| FR-5 | Shopping Cart | Kasir dapat add/remove produk ke cart, update quantity, lihat total harga |
| FR-6 | Payment Methods | Sistem support pembayaran Tunai dan E-Wallet dengan validasi jumlah |
| FR-7 | Transaction Receipt | Sistem generate struk pembayaran dengan detail transaksi |
| FR-8 | Transaction History | Kasir/Admin dapat view riwayat transaksi dengan filter kasir/metode pembayaran |
| FR-9 | Sales Report | Admin dapat view laporan dengan date range filter, update statistik otomatis |
| FR-10 | Excel Export | Admin dapat export laporan transaksi ke file Excel dengan format rapi |
| FR-11 | Logout Confirmation | Sistem menampilkan dialog konfirmasi sebelum logout |

### Non-Functional Requirements (NFR)

| ID | Requirement | Target |
|----|-----------|----|
| NFR-1 | Performance | Response time < 2 detik untuk operasi CRUD |
| NFR-2 | Database | PostgreSQL dengan 5+ tabel, proper schema design |
| NFR-3 | UI/UX | Desktop-friendly UI dengan responsive layout |
| NFR-4 | Security | Password tidak ditampilkan plain text, input validation |
| NFR-5 | Error Handling | Custom exception handling dengan pesan user-friendly |
| NFR-6 | Logging | Database operations logged, helpful error messages |

---

## User Stories dan Acceptance Criteria

### User Story 1: Kasir Checkout Transaksi
```
Sebagai seorang Kasir
Saya ingin melakukan transaksi penjualan dengan cepat
Agar penjualan dapat dicatat dan stok terupdate

Acceptance Criteria:
✓ Kasir dapat search produk dan add ke cart
✓ Sistem validasi stok tersedia
✓ Kasir dapat lihat total harga dan edit quantity
✓ Pembayaran diterima dengan metode Tunai/E-Wallet
✓ Struk dicetak/ditampilkan setelah pembayaran sukses
✓ Stok produk berkurang otomatis
✓ Transaksi tersimpan di database
```

### User Story 2: Admin Manage Produk
```
Sebagai seorang Admin
Saya ingin mengelola katalog produk lengkap
Agar sistem POS selalu up-to-date

Acceptance Criteria:
✓ Admin dapat CRUD produk (Kode, Nama, Kategori, Harga, Stok)
✓ Kategori dapat dipilih dari dropdown (tidak ketik manual)
✓ Admin dapat manage kategori (tambah/hapus)
✓ Dapat search produk by nama
✓ Filter berdasarkan kategori berfungsi
✓ Update harga/stok langsung berlaku untuk transaksi
```

### User Story 3: Admin View Laporan Penjualan
```
Sebagai seorang Admin
Saya ingin melihat laporan penjualan per periode
Agar bisa analisis performa bisnis

Acceptance Criteria:
✓ Admin dapat filter laporan by date range
✓ Laporan menampilkan statistik (Total Penjualan, Jumlah Transaksi, Produk Terlaku)
✓ Tabel menampilkan detail setiap transaksi
✓ Dapat export laporan ke Excel
✓ Format Excel rapi dengan header warna, currency format
```

---

## Entity Requirements

### Entities
1. **User**: ID, Username, Password, Nama Lengkap, Role, Created At
2. **Product**: ID, Kode, Nama, Kategori, Harga, Stok, Created At, Updated At
3. **Transaction**: ID, User ID, Tanggal, Total Harga, Metode Payment, Jumlah Bayar, Kembalian, Status
4. **TransactionItem**: ID, Transaction ID, Product ID, Quantity, Harga, Subtotal

---

## Quality Attributes

| Attribute | Target |
|-----------|--------|
| Reliability | 99% uptime, proper error handling |
| Maintainability | Clean code, proper documentation |
| Usability | Intuitive UI, user guidance |
| Performance | Smooth desktop application |
| Security | Basic authentication, no SQL injection |
=======
# 01 - Software Requirements Specification (SRS)

## Ringkasan Masalah dan Tujuan Sistem

### Masalah
Petani dan pedagang sayuran/buah memerlukan sistem Point of Sale (POS) yang mudah digunakan untuk mengelola penjualan harian, stok produk, dan laporan transaksi dengan cepat dan akurat.

### Tujuan
Mengembangkan aplikasi desktop berbasis JavaFX yang memungkinkan:
- Kasir untuk melakukan transaksi penjualan cepat
- Admin untuk mengelola produk, kategori, user, dan laporan
- Tracking transaksi dan laporan penjualan dengan filter tanggal
- Export laporan ke format Excel

---

## Scope dan Batasan

### In Scope
✅ Sistem login multi-role (Kasir, Admin)  
✅ Manajemen produk (CRUD)  
✅ Manajemen kategori produk  
✅ Manajemen user  
✅ Keranjang belanja (Cart)  
✅ Checkout dengan metode pembayaran (Tunai, E-Wallet)  
✅ Riwayat transaksi dengan filter  
✅ Laporan penjualan dengan date range filter  
✅ Export laporan ke Excel  
✅ Pagination untuk data besar  

### Out of Scope
❌ Multi-store/branch management  
❌ Integration dengan payment gateway real  
❌ Mobile application  
❌ Cloud synchronization  
❌ Barcode scanner integration  

---

## Daftar Requirement

### Functional Requirements (FR)

| ID | Requirement | Acceptance Criteria |
|----|-----------|----|
| FR-1 | Login System | User dapat login dengan username/password sesuai role (Kasir/Admin), dan sistem hanya menampilkan menu sesuai role |
| FR-2 | Product Management | Admin dapat CRUD produk (Kode, Nama, Kategori, Harga, Stok) dan sistem update stok otomatis saat checkout |
| FR-3 | Category Management | Admin dapat menambah/hapus kategori, dropdown kategori update otomatis di form produk |
| FR-4 | User Management | Admin dapat CRUD user dengan role assignment (Kasir/Admin) |
| FR-5 | Shopping Cart | Kasir dapat add/remove produk ke cart, update quantity, lihat total harga |
| FR-6 | Payment Methods | Sistem support pembayaran Tunai dan E-Wallet dengan validasi jumlah |
| FR-7 | Transaction Receipt | Sistem generate struk pembayaran dengan detail transaksi |
| FR-8 | Transaction History | Kasir/Admin dapat view riwayat transaksi dengan filter kasir/metode pembayaran |
| FR-9 | Sales Report | Admin dapat view laporan dengan date range filter, update statistik otomatis |
| FR-10 | Excel Export | Admin dapat export laporan transaksi ke file Excel dengan format rapi |
| FR-11 | Logout Confirmation | Sistem menampilkan dialog konfirmasi sebelum logout |

### Non-Functional Requirements (NFR)

| ID | Requirement | Target |
|----|-----------|----|
| NFR-1 | Performance | Response time < 2 detik untuk operasi CRUD |
| NFR-2 | Database | PostgreSQL dengan 5+ tabel, proper schema design |
| NFR-3 | UI/UX | Desktop-friendly UI dengan responsive layout |
| NFR-4 | Security | Password tidak ditampilkan plain text, input validation |
| NFR-5 | Error Handling | Custom exception handling dengan pesan user-friendly |
| NFR-6 | Logging | Database operations logged, helpful error messages |

---

## User Stories dan Acceptance Criteria

### User Story 1: Kasir Checkout Transaksi
```
Sebagai seorang Kasir
Saya ingin melakukan transaksi penjualan dengan cepat
Agar penjualan dapat dicatat dan stok terupdate

Acceptance Criteria:
✓ Kasir dapat search produk dan add ke cart
✓ Sistem validasi stok tersedia
✓ Kasir dapat lihat total harga dan edit quantity
✓ Pembayaran diterima dengan metode Tunai/E-Wallet
✓ Struk dicetak/ditampilkan setelah pembayaran sukses
✓ Stok produk berkurang otomatis
✓ Transaksi tersimpan di database
```

### User Story 2: Admin Manage Produk
```
Sebagai seorang Admin
Saya ingin mengelola katalog produk lengkap
Agar sistem POS selalu up-to-date

Acceptance Criteria:
✓ Admin dapat CRUD produk (Kode, Nama, Kategori, Harga, Stok)
✓ Kategori dapat dipilih dari dropdown (tidak ketik manual)
✓ Admin dapat manage kategori (tambah/hapus)
✓ Dapat search produk by nama
✓ Filter berdasarkan kategori berfungsi
✓ Update harga/stok langsung berlaku untuk transaksi
```

### User Story 3: Admin View Laporan Penjualan
```
Sebagai seorang Admin
Saya ingin melihat laporan penjualan per periode
Agar bisa analisis performa bisnis

Acceptance Criteria:
✓ Admin dapat filter laporan by date range
✓ Laporan menampilkan statistik (Total Penjualan, Jumlah Transaksi, Produk Terlaku)
✓ Tabel menampilkan detail setiap transaksi
✓ Dapat export laporan ke Excel
✓ Format Excel rapi dengan header warna, currency format
```

---

## Entity Requirements

### Entities
1. **User**: ID, Username, Password, Nama Lengkap, Role, Created At
2. **Product**: ID, Kode, Nama, Kategori, Harga, Stok, Created At, Updated At
3. **Transaction**: ID, User ID, Tanggal, Total Harga, Metode Payment, Jumlah Bayar, Kembalian, Status
4. **TransactionItem**: ID, Transaction ID, Product ID, Quantity, Harga, Subtotal

---

## Quality Attributes

| Attribute | Target |
|-----------|--------|
| Reliability | 99% uptime, proper error handling |
| Maintainability | Clean code, proper documentation |
| Usability | Intuitive UI, user guidance |
| Performance | Smooth desktop application |
| Security | Basic authentication, no SQL injection |
>>>>>>> 00b052a34e5245d8a8aa00af34917358ef8208d8
