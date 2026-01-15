# 06 - User Guide & System Usage

## Quick Start Guide

### System Requirements
- **Java**: 11 LTS or higher
- **Database**: PostgreSQL 13+
- **RAM**: 2GB minimum (4GB recommended)
- **Disk**: 500MB free space
- **OS**: Windows, macOS, or Linux

### First Time Setup
1. Start PostgreSQL service
2. Run `schema.sql` to create database
3. Run `seed.sql` to populate test data
4. Launch application: `java -jar agripos.jar`
5. Login with test credentials

**Test Account**:
```
Username: kasir
Password: 12345
Role: Kasir (Cashier)
```

---

## For Cashier Users (Kasir)

### Main Dashboard
The cashier interface is divided into three main tabs:

#### 1️⃣ Tab Kasir (Point of Sale)

**Purpose**: Process customer transactions

**Workflow**:
```
1. Enter Product Name → Click "Cari"
   ↓
2. Select Product from Results
   ↓
3. Enter Quantity → Click "Add to Cart"
   ↓
4. (Repeat for more items)
   ↓
5. Review Cart
   ↓
6. Click "Checkout"
   ↓
7. Select Payment Method
   ↓
8. Enter Amount Paid
   ↓
9. Process Payment
   ↓
10. View & Print Receipt
```

**Step-by-Step Instructions**:

**Search & Add Product**:
1. Type product name in search field (e.g., "Bayam")
2. Click "🔍 Cari" button
3. Select product from results table
4. Input quantity (must be > 0 and ≤ available stock)
5. Click "Add to Cart"

**Expected Result**: 
- Product appears in cart on the right
- Quantity counter shows in cart
- Unit price displayed
- Subtotal calculated automatically

**Review Cart**:
- **Cart Column Headers**:
  - Nama: Product name
  - Qty: Quantity ordered
  - Harga: Unit price
  - Subtotal: Qty × Harga

- **Modify Cart**:
  - Click quantity cell to edit
  - Click delete button (🗑️) to remove item
  - Click "Clear Cart" to remove all

**Checkout Process**:
1. Ensure cart has items
2. Click "💳 Checkout" button
3. Select payment method:
   - **CASH**: For cash payments
   - **EWALLET**: For digital/card payments

4. Enter amount paid (must be ≥ total)
5. System calculates change automatically
6. Click "Proses Pembayaran"

**Expected Result**:
- Success message appears
- Receipt dialog shows with:
  - Transaction ID
  - Date and time
  - Cashier name
  - Item listing with prices
  - Payment method
  - Total, amount paid, change
- Option to close receipt
- Cart automatically cleared

**Troubleshooting**:
```
❌ "Stok tidak cukup" 
   → Requested quantity exceeds available stock
   → Solution: Reduce quantity

❌ "Jumlah bayar harus ≥ total"
   → Payment amount too low
   → Solution: Enter amount equal to or higher than total

❌ "Kategori harus dipilih"
   → Happens during product add (admin feature)
   → Solution: Select category from dropdown
```

---

#### 2️⃣ Tab Riwayat Transaksi (Transaction History)

**Purpose**: View transaction records

**Features**:
- View all transactions or filtered transactions
- Apply filters by:
  - **Kasir**: Select cashier name
  - **Metode**: Select payment method (CASH, EWALLET)

**How to Use**:
1. Table automatically displays all transactions
2. (Optional) Select filter values
3. Click "Apply Filter"
4. Table updates to show matching records

**Table Columns**:
- **ID**: Transaction ID
- **Tanggal**: Transaction date
- **Kasir**: Cashier who processed
- **Total**: Total amount
- **Metode**: Payment method
- **Status**: Transaction status (Sukses/Gagal)

**Typical View**:
```
ID | Tanggal    | Kasir        | Total      | Metode  | Status
---+------------+--------------+------------+---------+--------
1  | 2026-01-10 | Andi Wijaya  | Rp 45.000  | CASH    | Sukses
2  | 2026-01-10 | Andi Wijaya  | Rp 120.000 | EWALLET | Sukses
3  | 2026-01-11 | Andi Wijaya  | Rp 78.500  | CASH    | Sukses
```

---

#### 3️⃣ Tab User Management

**Kasir Permission**: VIEW ONLY (cannot edit/add/delete)

**Available Information**:
- List of all system users
- User details: Username, Role, Full Name
- Display-only (no modification allowed)

---

### Logout Procedure

1. Click "🚪 Logout" button (top-right corner)
2. Confirmation dialog appears:
   ```
   Dialog Title: "Konfirmasi Logout"
   Message: "Apakah Anda yakin ingin logout?"
   Buttons: [Ya, Logout] [Tidak]
   ```
3. Click "Ya, Logout" to confirm
4. Return to login screen
5. Cart and session cleared

---

## For Admin Users (Admin)

### Admin Dashboard
Admin has access to five management tabs:

#### 1️⃣ Tab Manajemen Produk (Product Management)

**Access**: Admin only

**Available Operations**:
- **View**: List all products with search
- **Add**: Create new product
- **Update**: Modify existing product
- **Delete**: Remove product from system

**Add Product**:
```
Form Fields (all required):
┌─────────────────────────────┐
│ Kode Produk                 │  (e.g., SAY-004)
│ Nama Produk                 │  (e.g., Sawi Hijau)
│ Kategori (Dropdown)         │  (Select from list)
│ Harga                       │  (e.g., 20000)
│ Stok                        │  (e.g., 50)
├─────────────────────────────┤
│ [Tambah Produk] [Clear]     │
└─────────────────────────────┘
```

**Step-by-Step Add Product**:
1. Input product code (must be unique)
2. Input product name
3. Select category from dropdown
   - If category doesn't exist, use "Manajemen Kategori" tab first
4. Input price (in Rupiah)
5. Input stock quantity
6. Click "Tambah Produk"

**Success Indicator**:
- Success message: "Produk berhasil ditambahkan!"
- Product appears in table below
- Form clears for next entry

**Update Product**:
1. Click on product row in table
2. Form auto-populates with selected product data
3. Modify desired fields
4. Click "Update Produk"
5. Confirmation message appears

**Delete Product**:
1. Click on product row
2. Click "Hapus Produk" button
3. Confirmation dialog appears
4. Confirm deletion

**Product Table Display**:
```
No | Kode     | Nama            | Kategori   | Harga   | Stok
---+----------+-----------------+------------+---------+-----
1  | SAY-001  | Bayam Segar    | Sayuran    | 15.000  | 100
2  | SAY-002  | Kangkung       | Sayuran    | 12.000  | 80
3  | BUH-001  | Apel Merah     | Buah       | 30.000  | 25
```

---

#### 2️⃣ Tab Manajemen Kategori (Category Management)

**Access**: Admin only

**Purpose**: Add and remove product categories

**Two Sections**:

**Section A: Tambah Kategori (Add Category)**
```
Form:
┌──────────────────────────────┐
│ Nama Kategori: [_________]   │
├──────────────────────────────┤
│  [Tambah Kategori] [Clear]   │
└──────────────────────────────┘
```

**Add Category Steps**:
1. Enter category name (e.g., "Umbi-umbian")
2. Click "Tambah Kategori"
3. System message: "Kategori 'Umbi-umbian' berhasil ditambahkan!"
4. Form clears
5. Category instantly available in:
   - Product management dropdown
   - Report filter dropdown
   - Any other category selection field

**Category Naming Conventions**:
- Sayuran (Vegetables)
- Buah (Fruits)
- Bumbu (Spices)
- Umbi-umbian (Tubers)
- Biji-bijian (Grains)

---

**Section B: Hapus Kategori (Delete Category)**
```
Form:
┌──────────────────────────────┐
│ Kategori: [Sayuran▼]         │
├──────────────────────────────┤
│ [Hapus Kategori]             │
└──────────────────────────────┘
```

**Delete Category Steps**:
1. Select category from dropdown
2. Click "Hapus Kategori"
3. Confirmation: "Kategori 'Sayuran' berhasil dihapus!"
4. Category removed from all dropdowns

⚠️ **Warning**: Deleting category removes all template products associated with it

---

#### 3️⃣ Tab User Management

**Access**: Admin only

**Available Operations**:
- **View**: List all users with roles
- **Add**: Create new user account
- **Update**: Change user information
- **Delete**: Remove user account

**User Roles**:
- **KASIR**: Cashier - access to POS transactions
- **ADMIN**: Administrator - access to management features

**Add User**:
```
Form:
┌──────────────────────────────┐
│ Username: [_____________]    │
│ Password: [_____________]    │
│ Role: [KASIR  ▼]             │
│ Nama Lengkap: [___________]  │
├──────────────────────────────┤
│ [Tambah User] [Clear]        │
└──────────────────────────────┘
```

**User Table**:
```
No | Username  | Role    | Nama Lengkap
---+-----------+---------+---------------
1  | kasir     | KASIR   | Andi Wijaya
2  | admin     | ADMIN   | Budi Rahman
3  | kasir2    | KASIR   | Citra Dewi
```

---

#### 4️⃣ Tab Laporan Penjualan (Sales Report)

**Access**: Admin only

**Purpose**: Analyze sales performance with filters and export

**Key Features**:
1. **Date Range Filter**
2. **Category Filter**  
3. **Statistics Display**
4. **Excel Export**

**Workflow**:
```
1. Set Date Range
   ├─ Dari Tanggal: [Date Picker]
   └─ Sampai Tanggal: [Date Picker]

2. (Optional) Filter by Category
   ├─ Kategori: [Dropdown]

3. Click "Apply Filter"
   ↓
4. Table updates with filtered data
   ↓
5. Statistics card shows:
   ├─ Total Penjualan: Rp XXX
   ├─ Total Transaksi: N
   └─ Produk Terlaku: [Product Name]

6. (Optional) Click "Download ke Excel"
   └─ File saved to Downloads folder
```

**Example Report**:
```
Period: 2026-01-01 to 2026-01-31

Statistics:
┌────────────────────────────────────────┐
│ Total Penjualan: Rp 1.847.500          │
│ Total Transaksi: 12                    │
│ Produk Terlaku: Bayam Segar (15 unit)  │
└────────────────────────────────────────┘

Transaction Table:
ID | Tanggal    | Kasir       | Total      | Metode   | Status
---+------------+-------------+------------+----------+-------
1  | 2026-01-01 | Andi Wijaya | 45.000     | CASH     | Sukses
2  | 2026-01-02 | Andi Wijaya | 120.000    | EWALLET  | Sukses
3  | 2026-01-03 | Andi Wijaya | 78.500     | CASH     | Sukses
... (more rows)
```

**Excel Export Feature**:
1. Set filters (optional but recommended)
2. Click "Download ke Excel"
3. File downloads as: `Laporan_Transaksi_YYYYMMDD_HHmmss.xlsx`
4. Excel file contains:
   - Report title and generation timestamp
   - Filtered transaction data
   - Professional formatting (colors, borders)
   - Currency formatting (Rp)
   - Summary row with totals

---

#### 5️⃣ Tab Riwayat Transaksi (Transaction History)

**Purpose**: Full transaction audit trail

**Difference from Kasir Tab**:
- Admin sees ALL transactions (not just their own)
- Can view across all cashiers
- Cannot modify transactions

**Available Filters**:
- By Kasir name
- By Payment method

---

### Admin Logout
Same as Kasir:
1. Click "🚪 Logout" button
2. Confirm dialog
3. Select "Ya, Logout"
4. Return to login

---

## Common Workflows

### Workflow 1: Daily Opening Routine (Admin)

**Time**: 06:00 AM - Before business starts

1. **Start System**
   - Start PostgreSQL
   - Launch application
   - Login as Admin

2. **Verify Stock**
   - Go to "Manajemen Produk" tab
   - Check stock quantities
   - Note items needing reorder

3. **Check Previous Day Report**
   - Go to "Laporan Penjualan" tab
   - Set date filter to yesterday
   - Review total sales and transactions

4. **Add New Products** (if needed)
   - Tab: "Manajemen Produk"
   - Add any new stock items
   - Ensure correct stock quantities

5. **Logout**
   - Click logout
   - Confirm

---

### Workflow 2: Cashier Sales Transaction (Kasir)

**Time**: During business hours

1. **Customer Arrives**
   - Greet customer
   - Note items to purchase

2. **Process Sale**
   - Tab: "Kasir"
   - Search each product
   - Add to cart with correct quantity
   - Review cart total

3. **Payment**
   - Customer declares payment method
   - Click "Checkout"
   - Select payment method
   - Enter amount paid
   - Process payment

4. **Receipt**
   - Give receipt to customer
   - Confirm satisfaction
   - Close receipt dialog

5. **Next Customer**
   - Cart auto-clears
   - Repeat from step 2

---

### Workflow 3: End-of-Day Report (Admin)

**Time**: 17:00 - After business closes

1. **Generate Report**
   - Go to "Laporan Penjualan" tab
   - Set date filter to today

2. **Review Metrics**
   - Total daily sales
   - Number of transactions
   - Best-selling product

3. **Export Report**
   - Click "Download ke Excel"
   - Save to secure location
   - Keep for records/analysis

4. **Verify Transactions**
   - Tab: "Riwayat Transaksi"
   - Review all day's transactions
   - Note any issues or refunds

5. **Security Logout**
   - Logout properly
   - Shut down application
   - Stop PostgreSQL service

---

## Troubleshooting

### Login Issues

**❌ "Login gagal: Password salah"**
- Check capslock
- Verify username is correct
- Ensure database is running

**❌ "Database connection failed"**
- Verify PostgreSQL service running
- Check database host/port in config
- Verify database name is correct

---

### Cart Issues

**❌ "Stok tidak cukup"**
- Product quantity exceeds available stock
- Reduce quantity or find alternative product
- Check current stock in product list

**❌ Product not found in search**
- Product name might be spelled differently
- Product might be deleted from system
- Check "Manajemen Produk" to verify existence

---

### Payment Issues

**❌ "Jumlah bayar harus ≥ total"**
- Amount paid is less than total
- Enter amount equal to or greater than total

**❌ "Stok berkurang gagal"**
- Rare database error
- Try payment again
- Contact system administrator

---

### Report Issues

**❌ Report shows no data**
- Date range might be incorrect
- No transactions in selected period
- Check transaction history for available dates

**❌ Excel export fails**
- Disk space issue
- File permissions
- Verify Downloads folder is writable

---

## System Rules & Constraints

| Rule | Details |
|------|---------|
| **Minimum Stock** | Cannot add product with 0 stock |
| **Username Unique** | Two users cannot have same username |
| **Price Format** | Prices in Rupiah (whole numbers) |
| **Payment Amount** | Must be ≥ total amount |
| **Category Required** | All products must have category |
| **Date Format** | System uses YYYY-MM-DD format |
| **Session Duration** | Auto-logout after 30 min inactivity (if configured) |

---

## Keyboard Shortcuts

| Action | Keyboard |
|--------|----------|
| Focus next field | Tab |
| Focus previous field | Shift+Tab |
| Submit form | Enter |
| Clear form | Ctrl+N |
| Search | Ctrl+F |
| Close dialog | Esc |
| Logout | Ctrl+L |

---

## Getting Help

**For Technical Issues**:
- Check Troubleshooting section above
- Verify system requirements are met
- Check application logs in console

**For Database Issues**:
- Verify PostgreSQL running
- Check database name and credentials
- Review seed.sql for schema verification

**For Feature Questions**:
- Refer to relevant tab documentation
- Review step-by-step instructions
- Check workflow examples

---

## System Feedback Messages

**Success Messages**:
- ✓ "Produk berhasil ditambahkan!"
- ✓ "Kategori 'xxx' berhasil ditambahkan!"
- ✓ "User berhasil ditambahkan!"
- ✓ "Pembayaran diterima"

**Error Messages**:
- ✗ "Stok tidak cukup"
- ✗ "Kategori harus dipilih"
- ✗ "Jumlah bayar harus ≥ total"
- ✗ "Database connection failed"

**Info Messages**:
- ℹ "Laporan loading..."
- ℹ "Exporting to Excel..."

---

## Glossary

| Term | Definition |
|------|-----------|
| **Kasir** | Cashier role - processes sales |
| **Admin** | Administrator - manages system |
| **Kategori** | Product category/classification |
| **Metode Pembayaran** | Payment method (CASH, EWALLET) |
| **Stok** | Product inventory quantity |
| **Transaksi** | Sales transaction record |
| **Laporan** | Sales report with analytics |

---

**Last Updated**: 2026-01-14  
**Documentation Version**: 1.0
