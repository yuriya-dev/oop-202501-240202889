# 07 - Runbook & System Administration

## System Overview

**Project**: AgriPOS - Agricultural Point of Sale System  
**Version**: 1.0  
**Platform**: Java 11+ with JavaFX 17.0.6  
**Database**: PostgreSQL 13+  
**Build Tool**: Maven 3.8+

---

## Pre-Installation Checklist

Before deploying AgriPOS, verify the following:

- [ ] Java 11 LTS or higher installed
- [ ] PostgreSQL 13+ installed and running
- [ ] Maven 3.8+ installed
- [ ] Minimum 2GB RAM available
- [ ] 500MB disk space available
- [ ] Network connectivity to database
- [ ] Git installed (for version control)

### Verify Prerequisites

```powershell
# Check Java version
java -version
# Output: java version "11.0.x" or higher

# Check PostgreSQL version
psql --version
# Output: psql (PostgreSQL) 13.x

# Check Maven version
mvn --version
# Output: Maven 3.8.x
```

---

## Installation Guide

### Step 1: Download & Extract Project

```bash
# Clone repository
git clone https://github.com/your-repo/agripos.git
cd agripos

# Or extract ZIP file
unzip agripos-v1.0.zip
cd agripos
```

**Folder Structure**:
```
agripos/
├── src/
│   ├── main/java/
│   │   └── com/agripos/
│   │       ├── entity/
│   │       ├── service/
│   │       ├── dao/
│   │       ├── ui/
│   │       └── util/
│   ├── test/java/
│   └── resources/
├── sql/
│   ├── schema.sql
│   ├── seed.sql
│   └── backup.sql
├── pom.xml
├── README.md
└── docs/
```

---

### Step 2: Configure Database

#### 2a. Create PostgreSQL Database

```sql
-- Connect as postgres superuser
psql -U postgres

-- Create database
CREATE DATABASE agripos_db
  WITH OWNER postgres
  ENCODING 'UTF8'
  LOCALE 'id_ID.UTF-8';

-- Create user (optional - for security)
CREATE USER agripos_user WITH PASSWORD 'agripos_pass123';
GRANT ALL PRIVILEGES ON DATABASE agripos_db TO agripos_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO agripos_user;

-- Verify
\l
\du

-- Exit psql
\q
```

#### 2b. Load Schema

```bash
# Navigate to sql folder
cd sql

# Load schema (requires password)
psql -U postgres -d agripos_db -f schema.sql
# Enter password when prompted

# Verify schema created
psql -U postgres -d agripos_db -c "\dt"
# Should show: products, users, transactions, transaction_items

# Load seed data
psql -U postgres -d agripos_db -f seed.sql
```

**Seed Data Loaded**:
```
- 3 test users (kasir, admin, kasir2)
- 9 sample products across 5 categories
- 3 sample transactions for testing
```

---

### Step 3: Update Database Configuration

**File**: `src/main/java/com/agripos/util/DatabaseConnection.java`

```java
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/agripos_db";
    private static final String USER = "postgres";  // Or "agripos_user"
    private static final String PASSWORD = "your_password";
    private static final int MAX_POOL_SIZE = 10;
    
    // ... rest of code
}
```

**Configuration Parameters**:
| Parameter | Default | Notes |
|-----------|---------|-------|
| URL | localhost:5432 | Change if database remote |
| USER | postgres | Use dedicated user for security |
| PASSWORD | password | Set strong password |
| MAX_POOL_SIZE | 10 | Adjust for concurrent users |

**For Security** (Production):
```java
// Read from environment variables instead
private static final String URL = System.getenv("DB_URL");
private static final String USER = System.getenv("DB_USER");
private static final String PASSWORD = System.getenv("DB_PASSWORD");
```

Set environment variables:
```bash
# Windows
set DB_URL=jdbc:postgresql://localhost:5432/agripos_db
set DB_USER=agripos_user
set DB_PASSWORD=secure_password

# Linux/Mac
export DB_URL=jdbc:postgresql://localhost:5432/agripos_db
export DB_USER=agripos_user
export DB_PASSWORD=secure_password
```

---

### Step 4: Build Application

```bash
# Navigate to project root
cd agripos

# Clean previous builds
mvn clean

# Compile source code
mvn compile

# Expected output:
# [INFO] BUILD SUCCESS

# Run tests (optional)
mvn test

# Package JAR file
mvn package

# Expected output:
# [INFO] Building jar: target/agripos-1.0.jar
```

**Build Artifacts**:
- Main JAR: `target/agripos-1.0.jar`
- Dependency JARs: `target/agripos-1.0-jar-with-dependencies.jar` (if configured)

---

### Step 5: Run Application

#### Option A: Run from IDE (Development)

```bash
# In IntelliJ IDEA / Eclipse / VS Code
1. Open project in IDE
2. Locate AppJavaFX.java (main class)
3. Right-click → Run
4. Application starts with debug support
```

#### Option B: Run from JAR (Production)

```bash
# Navigate to target folder
cd target

# Run application
java -jar agripos-1.0.jar

# Or with custom memory allocation
java -Xms512m -Xmx1024m -jar agripos-1.0.jar

# With console output
java -jar agripos-1.0.jar > agripos.log 2>&1
```

#### Option C: Create Windows Batch Script

**File**: `run_agripos.bat`

```batch
@echo off
REM Start PostgreSQL (if not already running)
REM net start postgresql-x64-13

REM Navigate to application folder
cd /d "C:\path\to\agripos\target"

REM Run application with memory settings
java -Xms512m -Xmx1024m -jar agripos-1.0.jar

REM Keep window open if error
pause
```

**To use**:
```bash
double-click run_agripos.bat
```

#### Option D: Create Linux/Mac Startup Script

**File**: `run_agripos.sh`

```bash
#!/bin/bash

# Check if PostgreSQL is running
if ! pgrep -x "postgres" > /dev/null; then
    echo "Starting PostgreSQL..."
    # macOS: brew services start postgresql
    # Linux: sudo systemctl start postgresql
fi

# Change to application directory
cd "$(dirname "$0")/target"

# Run application with Java
java -Xms512m -Xmx1024m -jar agripos-1.0.jar &

echo "AgriPOS started (PID: $!)"
```

**To use**:
```bash
chmod +x run_agripos.sh
./run_agripos.sh
```

---

## System Operation

### Starting the System

**Daily Startup Procedure**:

```
1. Start Database Server
   ├─ Windows: Services → PostgreSQL
   ├─ Linux: sudo systemctl start postgresql
   └─ Mac: brew services start postgresql

2. Wait 5-10 seconds for database ready

3. Launch Application
   ├─ Double-click run_agripos.bat (Windows)
   ├─ Run ./run_agripos.sh (Linux/Mac)
   └─ java -jar agripos-1.0.jar (terminal)

4. Wait 3-5 seconds for UI to appear

5. Verify Login Screen
   ├─ Login form displays
   ├─ Database connected (check logs)
   └─ Ready to use
```

### Shutdown Procedure

**End-of-Day Shutdown**:

```
1. Complete Final Transactions
   ├─ Process any pending sales
   ├─ Ensure checkout complete
   └─ Verify payment processed

2. Close Application
   ├─ Click Logout (completes session)
   ├─ Close application window
   └─ Wait for graceful shutdown

3. Stop Database
   ├─ Windows: Services → PostgreSQL → Stop
   ├─ Linux: sudo systemctl stop postgresql
   └─ Mac: brew services stop postgresql

4. Verify Clean Shutdown
   ├─ No Java processes running
   └─ Database connections closed
```

---

## Database Management

### Backup Procedures

#### Full Backup (Complete Database)

```bash
# Using pg_dump
pg_dump -U postgres -d agripos_db -Fc -f agripos_backup_$(date +%Y%m%d_%H%M%S).dump

# Or SQL format
pg_dump -U postgres -d agripos_db -f agripos_backup_$(date +%Y%m%d_%H%M%S).sql

# Verify backup created
ls -lh agripos_backup_*
```

#### Incremental Backup (Transactions Only)

```bash
# Export transaction tables
pg_dump -U postgres -d agripos_db -t transactions -t transaction_items -f transactions_backup_$(date +%Y%m%d).sql
```

#### Automated Daily Backup

**Linux/Mac Cron Job**:

```bash
# Edit crontab
crontab -e

# Add line for 2 AM daily backup:
0 2 * * * pg_dump -U postgres -d agripos_db -Fc -f /backups/agripos_$(date +\%Y\%m\%d).dump

# Save and exit
```

**Windows Task Scheduler**:
```
1. Open Task Scheduler
2. Create Basic Task
3. Set Trigger: Daily at 02:00
4. Action: Start program
5. Program: C:\Program Files\PostgreSQL\13\bin\pg_dump.exe
6. Arguments: -U postgres -d agripos_db -Fc -f C:\backups\agripos_%date:~-4,4%%date:~-10,2%%date:~-7,2%.dump
```

---

### Restore from Backup

```bash
# From custom format dump
pg_restore -U postgres -d agripos_db -Fc agripos_backup_20260114.dump

# From SQL dump
psql -U postgres -d agripos_db -f agripos_backup_20260114.sql

# Verify restore
psql -U postgres -d agripos_db -c "SELECT COUNT(*) FROM transactions;"
```

---

### Database Maintenance

#### Check Database Size

```sql
-- Connect to database
psql -U postgres -d agripos_db

-- Check overall size
SELECT pg_size_pretty(pg_database_size('agripos_db')) AS database_size;

-- Check individual table sizes
SELECT schemaname, tablename, pg_size_pretty(pg_total_relation_size(schemaname||'.'||tablename)) AS size
FROM pg_tables
ORDER BY pg_total_relation_size(schemaname||'.'||tablename) DESC;
```

#### Vacuum & Analyze

```sql
-- Optimize database
VACUUM ANALYZE;

-- Rebuild indexes
REINDEX DATABASE agripos_db;
```

#### Check Connections

```bash
# View active connections
psql -U postgres -d agripos_db -c "SELECT * FROM pg_stat_activity;"

# Kill specific connection (if needed)
# SELECT pg_terminate_backend(pid);
```

---

## Monitoring & Logs

### Application Logs

```bash
# Capture console output
java -jar target/agripos-1.0.jar > logs/agripos_$(date +%Y%m%d_%H%M%S).log 2>&1 &

# View real-time logs
tail -f logs/agripos_latest.log

# Search logs for errors
grep ERROR logs/agripos_*.log

# Archive old logs (older than 30 days)
find logs/ -name "*.log" -mtime +30 -delete
```

### Database Query Log

```sql
-- Enable query logging
ALTER SYSTEM SET log_statement = 'all';
ALTER SYSTEM SET log_duration = on;

-- Reload configuration
SELECT pg_reload_conf();

-- Logs location: PostgreSQL data directory/pg_log/
```

### Performance Monitoring

```sql
-- Slow queries
CREATE EXTENSION IF NOT EXISTS pg_stat_statements;

SELECT query, calls, mean_time FROM pg_stat_statements 
ORDER BY mean_time DESC LIMIT 10;
```

---

## Troubleshooting Guide

### Issue 1: Application Won't Start

**Error**: Application hangs on startup

**Diagnosis**:
```bash
# Check Java is working
java -version

# Check PostgreSQL is running
psql -U postgres -c "SELECT version();"
```

**Solution**:
```bash
# Option 1: Check database connection
# Verify URL, USER, PASSWORD in DatabaseConnection.java

# Option 2: Check database exists
psql -U postgres -l | grep agripos_db

# Option 3: Check tables exist
psql -U postgres -d agripos_db -c "\dt"
# Should show: products, users, transactions, transaction_items

# Option 4: Reload schema
psql -U postgres -d agripos_db -f sql/schema.sql
```

---

### Issue 2: Database Connection Timeout

**Error**: `org.postgresql.util.PSQLException: Connection timeout`

**Cause**: 
- PostgreSQL not running
- Wrong hostname/port
- Firewall blocking connection

**Solution**:
```bash
# Verify PostgreSQL running
# Windows: sc query PostgreSQL-x64-13
# Linux: sudo systemctl status postgresql

# Test connection
psql -h localhost -U postgres -d agripos_db

# If remote database, verify connectivity
ping database-hostname
telnet database-hostname 5432
```

---

### Issue 3: Out of Memory Error

**Error**: `java.lang.OutOfMemoryError: Java heap space`

**Cause**: 
- Too much data loaded in memory
- Memory leak in application
- Insufficient heap allocation

**Solution**:
```bash
# Increase heap memory
java -Xms1024m -Xmx2048m -jar agripos-1.0.jar

# If recurring, add to startup script
# -Xms: Initial heap (512MB)
# -Xmx: Maximum heap (1-2GB depending on system)
```

---

### Issue 4: Transaction Data Lost

**Error**: Transactions not saved after checkout

**Diagnosis**:
```sql
-- Check if transaction saved
SELECT * FROM transactions WHERE DATE(tanggal) = CURRENT_DATE;

-- Check transaction_items
SELECT * FROM transaction_items 
WHERE transaction_id IN (SELECT id FROM transactions WHERE DATE(tanggal) = CURRENT_DATE);
```

**Solution**:
1. Verify database connection is active
2. Check TransactionService.saveTransaction() called
3. Review logs for database errors
4. Restart application and retry transaction

---

### Issue 5: Incorrect Stock Count

**Error**: Stock quantity doesn't match expected

**Diagnosis**:
```sql
-- Check product stock
SELECT kode, nama, stok FROM products WHERE kode = 'SAY-001';

-- Check transaction items
SELECT SUM(quantity) FROM transaction_items 
WHERE product_id = 1;
```

**Solution**:
```sql
-- Manually update stock if needed
UPDATE products SET stok = 100 WHERE kode = 'SAY-001';

-- Verify update
SELECT stok FROM products WHERE kode = 'SAY-001';
```

---

### Issue 6: Category Dropdown Empty

**Error**: No categories showing in dropdown

**Cause**: 
- No categories in database
- CategoryService not loading
- Database connection issue

**Solution**:
```sql
-- Check categories exist
SELECT DISTINCT kategori FROM products;

-- If empty, add default categories
INSERT INTO products (kode, nama, kategori, harga, stok) VALUES
('CAT-SAYURAN', 'Template Sayuran', 'Sayuran', 0, 0),
('CAT-BUAH', 'Template Buah', 'Buah', 0, 0);

-- Restart application
```

---

## Maintenance Schedule

### Daily Tasks
- [ ] Verify application starts cleanly
- [ ] Check database connection health
- [ ] Monitor disk space
- [ ] Review error logs

### Weekly Tasks
- [ ] Backup database
- [ ] Verify backup integrity
- [ ] Check transaction count
- [ ] Monitor application performance

### Monthly Tasks
- [ ] Analyze query performance
- [ ] Vacuum and optimize database
- [ ] Review user access logs
- [ ] Update software if patches available
- [ ] Test disaster recovery procedures

### Quarterly Tasks
- [ ] Full system backup to external drive
- [ ] Security audit of user accounts
- [ ] Performance baseline review
- [ ] Update documentation

---

## Security Considerations

### Database Security

```sql
-- Change default password
ALTER USER postgres WITH PASSWORD 'strong_password_here';

-- Create dedicated application user
CREATE USER agripos_user WITH PASSWORD 'application_password';
GRANT CONNECT ON DATABASE agripos_db TO agripos_user;
GRANT USAGE ON SCHEMA public TO agripos_user;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO agripos_user;
```

### Application Security

1. **Password Hashing** (Recommended for future)
   - Current: Passwords stored plaintext (security risk)
   - Implement: bcrypt or PBKDF2 hashing

2. **Connection Security**
   - Use SSL/TLS for remote database connections
   - Verify certificate in production

3. **Audit Trail**
   - Enable database query logging
   - Archive logs regularly
   - Monitor for suspicious activity

### Access Control

| User Role | Permissions |
|-----------|-------------|
| KASIR | Process transactions, view history |
| ADMIN | Full system management |
| postgres | Database administration |

---

## Performance Tuning

### Database Indexes

```sql
-- Check existing indexes
SELECT * FROM pg_indexes WHERE tablename = 'products';

-- Add useful indexes for performance
CREATE INDEX idx_products_kategori ON products(kategori);
CREATE INDEX idx_transactions_tanggal ON transactions(tanggal);
CREATE INDEX idx_transaction_items_product_id ON transaction_items(product_id);

-- Check query execution plans
EXPLAIN SELECT * FROM transactions WHERE tanggal > NOW() - INTERVAL '30 days';
```

### Connection Pool Optimization

```java
// In DatabaseConnection.java
HikariConfig config = new HikariConfig();
config.setMaximumPoolSize(10);        // Max connections
config.setMinimumIdle(2);             // Min idle connections
config.setConnectionTimeout(30000);   // 30 second timeout
config.setIdleTimeout(600000);        // 10 minute idle timeout
config.setMaxLifetime(1800000);       // 30 minute max lifetime
```

---

## Disaster Recovery Plan

### Recovery Time Objective (RTO): 1 hour
### Recovery Point Objective (RPO): 24 hours (daily backups)

### Recovery Steps

**1. System Failure (Application crash)**
```bash
# 1. Verify database is still running
psql -U postgres -d agripos_db -c "SELECT 1;"

# 2. Restart application
java -jar target/agripos-1.0.jar

# 3. Verify data integrity
psql -U postgres -d agripos_db -c "SELECT COUNT(*) FROM transactions;"

# 4. Resume operations
```

**2. Database Failure (Corrupted database)**
```bash
# 1. Stop application
# 2. Drop corrupted database
psql -U postgres -c "DROP DATABASE agripos_db;"

# 3. Restore from latest backup
pg_restore -U postgres -C -d postgres agripos_backup_20260114.dump

# 4. Verify restore
psql -U postgres -d agripos_db -c "SELECT COUNT(*) FROM transactions;"

# 5. Restart application
```

**3. Data Loss (Accidental deletion)**
```sql
-- Restore specific table from backup
pg_restore -U postgres -d agripos_db -t products agripos_backup_full.dump

-- Or use point-in-time recovery (requires WAL archiving)
-- ALTER SYSTEM SET wal_level = replica;
```

---

## Support & Escalation

### Support Contacts

| Issue Type | Contact | Response Time |
|------------|---------|----------------|
| Database | DBA Team | 1 hour |
| Application | Dev Team | 2 hours |
| Network | IT Support | 30 minutes |
| Critical Outage | All Teams | ASAP |

### Escalation Process

```
Level 1: Application Log Review (15 min)
  ↓ If unresolved
Level 2: Database Check (15 min)
  ↓ If unresolved
Level 3: System Restart (15 min)
  ↓ If unresolved
Level 4: Restore from Backup (30 min)
  ↓ If unresolved
Level 5: Incident Response Team
```

---

## Documentation References

- [Database Schema](03_database.md) - Entity relationships and constraints
- [Architecture Design](02_arsitektur.md) - System design and patterns
- [User Guide](06_user_guide.md) - User operations and workflows
- [Test Report](05_test_report.md) - Testing evidence and coverage

---

**Last Updated**: 2026-01-14  
**Document Version**: 1.0  
**Created By**: Development Team
