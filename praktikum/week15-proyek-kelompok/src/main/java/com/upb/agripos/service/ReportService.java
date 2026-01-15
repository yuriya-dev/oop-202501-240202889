package com.upb.agripos.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import com.upb.agripos.dao.DatabaseConnection;
import com.upb.agripos.exception.DatabaseException;
import com.upb.agripos.model.TransactionHistory;

/**
 * Service untuk mengelola laporan penjualan dan transaksi.
 */
public class ReportService {

    /**
     * Mendapatkan laporan transaksi berdasarkan rentang tanggal.
     */
    public List<TransactionHistory> getReportByDateRange(LocalDate startDate, LocalDate endDate) 
            throws DatabaseException {
        List<TransactionHistory> reports = new ArrayList<>();
        
        if (startDate == null) {
            startDate = LocalDate.of(2000, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        String sql = "SELECT t.id, t.tanggal, u.nama_lengkap, t.total_harga, t.metode_payment, " +
                     "t.jumlah_bayar, t.kembalian, t.status FROM transactions t " +
                     "JOIN users u ON t.user_id = u.id " +
                     "WHERE DATE(t.tanggal) >= ? AND DATE(t.tanggal) <= ? " +
                     "ORDER BY t.tanggal DESC";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TransactionHistory history = new TransactionHistory(
                        rs.getInt("id"),
                        rs.getTimestamp("tanggal").toLocalDateTime(),
                        rs.getString("nama_lengkap"),
                        rs.getDouble("total_harga"),
                        rs.getString("metode_payment"),
                        rs.getDouble("jumlah_bayar"),
                        rs.getDouble("kembalian"),
                        rs.getString("status")
                    );
                    reports.add(history);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching report by date range: " + e.getMessage(), e);
        }

        return reports;
    }

    /**
     * Mendapatkan statistik penjualan berdasarkan rentang tanggal.
     */
    public ReportStatistics getStatisticsByDateRange(LocalDate startDate, LocalDate endDate) 
            throws DatabaseException {
        if (startDate == null) {
            startDate = LocalDate.of(2000, 1, 1);
        }
        if (endDate == null) {
            endDate = LocalDate.now();
        }

        String sql = "SELECT COUNT(id) as total_transaksi, SUM(total_harga) as total_penjualan " +
                     "FROM transactions WHERE DATE(tanggal) >= ? AND DATE(tanggal) <= ? AND status = 'Sukses'";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    long totalTransaksi = rs.getLong("total_transaksi");
                    double totalPenjualan = rs.getDouble("total_penjualan");
                    String produkTerlaku = getTopProduct(startDate, endDate);
                    
                    return new ReportStatistics(totalTransaksi, totalPenjualan, produkTerlaku);
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching statistics: " + e.getMessage(), e);
        }

        return new ReportStatistics(0, 0, "-");
    }

    /**
     * Mendapatkan produk yang paling banyak terjual.
     */
    private String getTopProduct(LocalDate startDate, LocalDate endDate) 
            throws DatabaseException {
        String sql = "SELECT p.nama, SUM(ti.quantity) as total_qty FROM transaction_items ti " +
                     "JOIN products p ON ti.product_id = p.id " +
                     "JOIN transactions t ON ti.transaction_id = t.id " +
                     "WHERE DATE(t.tanggal) >= ? AND DATE(t.tanggal) <= ? AND t.status = 'Sukses' " +
                     "GROUP BY p.id, p.nama ORDER BY total_qty DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setObject(1, startDate);
            stmt.setObject(2, endDate);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("nama");
                }
            }
        } catch (SQLException e) {
            throw new DatabaseException("Error fetching top product: " + e.getMessage(), e);
        }

        return "-";
    }

    /**
     * Model untuk statistik laporan.
     */
    public static class ReportStatistics {
        public final long totalTransaksi;
        public final double totalPenjualan;
        public final String produkTerlaku;

        public ReportStatistics(long totalTransaksi, double totalPenjualan, String produkTerlaku) {
            this.totalTransaksi = totalTransaksi;
            this.totalPenjualan = totalPenjualan;
            this.produkTerlaku = produkTerlaku;
        }
    }
}
