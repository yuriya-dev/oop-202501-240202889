# 02 - Arsitektur Sistem

## Ringkasan Arsitektur

AGRI-POS menggunakan **3-Layer Architecture** dengan Separation of Concerns:
- **Presentation Layer** (View) - UI dengan JavaFX
- **Business Logic Layer** (Service) - Logika bisnis aplikasi
- **Data Access Layer** (DAO) - Komunikasi dengan database

---

## Architecture Diagram

```
┌─────────────────────────────────────────────────────────┐
│                    PRESENTATION LAYER                   │
│  LoginView  │  KasirView  │  AdminView  │  ReceiptDialog│
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                    CONTROLLER LAYER                     │
│              (AppJavaFX - Event Handlers)               │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   BUSINESS LOGIC LAYER                  │
│ AuthService  │ ProductService  │ CartService            │
│ TransactionService  │ ReportService  │ CategoryService  │
│ ExcelExportService                                      │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                   DATA ACCESS LAYER                     │
│ ProductDAO  │ UserDAO  │ DatabaseConnection            │
└────────────────────┬────────────────────────────────────┘
                     │
                     ▼
┌─────────────────────────────────────────────────────────┐
│                    DATABASE LAYER                       │
│              PostgreSQL Database                        │
└─────────────────────────────────────────────────────────┘
```

---

## Package Structure

```
com.upb.agripos/
├── AppJavaFX.java                  # Main Application & Controller
├── model/
│   ├── User.java
│   ├── Product.java
│   ├── Transaction.java
│   ├── TransactionHistory.java
│   ├── CartItem.java
│   └── Cart.java
├── view/
│   ├── LoginView.java
│   ├── KasirView.java
│   ├── AdminView.java
│   └── ReceiptDialog.java
├── service/
│   ├── AuthService.java
│   ├── ProductService.java
│   ├── CartService.java
│   ├── TransactionService.java
│   ├── ReportService.java
│   ├── CategoryService.java
│   ├── ExcelExportService.java
│   ├── PaymentMethod.java (interface)
│   ├── CashPayment.java
│   └── EWalletPayment.java
├── dao/
│   ├── DatabaseConnection.java
│   ├── ProductDAO.java
│   ├── ProductDAOImpl.java
│   ├── UserDAO.java
│   └── UserDAOImpl.java
└── exception/
    ├── ValidationException.java
    ├── DatabaseException.java
    └── OutOfStockException.java
```

---

## Layering & Dependency Rules

### Rule 1: Dependency Flow
```
View → Controller → Service → DAO → Database
  ↑         ↑          ↑       ↑
  └─────────┴──────────┴───────┘
  (Only downward dependency allowed)
```

### Rule 2: No Cross-Layer Calls
- ❌ View tidak boleh langsung akses DAO
- ❌ Service tidak boleh akses View
- ✅ Service hanya akses DAO
- ✅ View hanya akses Service melalui Controller

### Rule 3: Model Independence
- Model tidak bergantung pada layer lain
- Model hanya mengandung data + getter/setter

---

## Design Patterns Used

### 1. **Singleton Pattern**
- `DatabaseConnection` - Satu koneksi database untuk seluruh aplikasi
```java
public class DatabaseConnection {
    private static DatabaseConnection instance;
    
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }
}
```

### 2. **DAO Pattern**
- Abstraksi akses data dari business logic
- ProductDAO, UserDAO terpisah dari Service
```java
public interface ProductDAO {
    void addProduct(String kode, String nama, ...) throws DatabaseException;
    Product getProductById(int id) throws DatabaseException;
    List<Product> getAllProducts() throws DatabaseException;
}
```

### 3. **Strategy Pattern**
- Payment methods dapat diswitch (Cash, E-Wallet)
```java
public interface PaymentMethod {
    boolean validatePayment(double amount, double total);
}
public class CashPayment implements PaymentMethod { ... }
public class EWalletPayment implements PaymentMethod { ... }
```

### 4. **MVC Pattern**
- Model (Entity/DAO) → View (JavaFX) → Controller (AppJavaFX)
- Event-driven UI updates

### 5. **Service Locator Pattern**
- Semua Service diinisialisasi di AppJavaFX.start()
- Service dipass ke View untuk digunakan

---

## Key Components

### AuthService
```
Responsibilities:
- User login validation
- Password verification
- Role-based access control
```

### ProductService
```
Responsibilities:
- CRUD Product
- Search & filter produk
- Stock management
- Category handling
Depends on: ProductDAO
```

### CartService
```
Responsibilities:
- Manage shopping cart
- Add/remove items
- Calculate total
- Track quantities
```

### TransactionService
```
Responsibilities:
- Process payment
- Generate receipt
- Save transaction
- Retrieve transaction history
- Handle multiple payment methods
Depends on: DatabaseConnection
```

### ReportService
```
Responsibilities:
- Fetch reports with date filter
- Calculate statistics
- Top product ranking
Depends on: DatabaseConnection
```

### CategoryService
```
Responsibilities:
- CRUD Category
- Get distinct categories
Depends on: DatabaseConnection
```

### ExcelExportService
```
Responsibilities:
- Export data to Excel
- Format cells
- Apply styles
```

---

## Error Handling Strategy

### Custom Exceptions
```java
ValidationException      → Input validation errors
DatabaseException        → Database operation errors
OutOfStockException     → Stock unavailable errors
```

### Exception Flow
```
View → Service → DAO → catch SQLException → throw DatabaseException
  ↑                                           ↑
  └───────────────────────────────────────────┘
  View catches and shows user-friendly message
```

---

## Data Flow Examples

### Flow 1: Add Product
```
AdminView (Form Input)
    ↓
AppJavaFX.handleAddProduct (Validation)
    ↓
ProductService.addProduct()
    ↓
ProductDAOImpl.addProduct()
    ↓
DatabaseConnection.executeUpdate()
    ↓
PostgreSQL DB
```

### Flow 2: Checkout Transaksi
```
KasirView (Click Checkout)
    ↓
AppJavaFX.handleCheckout()
    ↓
TransactionService.processPayment()
    ↓
TransactionService.saveTransaction()
    ↓
DatabaseConnection.executeSave()
    ↓
PostgreSQL DB (transaction saved)
    ↓
ProductService.reduceStock()
    ↓
TransactionService.generateReceipt()
    ↓
ReceiptDialog.show()
```

---

## Configuration Management

### Environment Variables
- Database host, port, username, password
- Application title, version
- UI theme settings

### Resource Files
- `application.properties` untuk konfigurasi
- CSS styles untuk UI theming

---

## Security Considerations

### 1. Authentication
- Username/password validation
- Role-based authorization
- Session management (currentUser)

### 2. SQL Injection Prevention
- PreparedStatement digunakan untuk semua queries
- Input parameterization

### 3. Password Security
- Stored in database (plain text dalam demo, harus hash di production)

---

## Scalability & Future Improvements

### Current Limitations
- Single user login (no concurrent sessions)
- Simple file-based configuration
- Basic error messages

### Future Improvements
1. Multi-store support (store_id in tables)
2. Inventory management (low stock alerts)
3. Customer loyalty program
4. Integration dengan payment gateway
5. Real-time sync dengan cloud
6. Mobile app untuk kasir
