
package dao;

import database.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SalesHistoryDAO {

    // Retrieve all bills
    public List<Object[]> getAllBills() {

        List<Object[]> bills = new ArrayList<>();

        String sql = "SELECT bill_id, bill_date, subtotal, "
                + "gst_amount, total_amount FROM bills "
                + "ORDER BY bill_id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                bills.add(new Object[]{
                        rs.getInt("bill_id"),
                        rs.getTimestamp("bill_date"),
                        rs.getBigDecimal("subtotal"),
                        rs.getBigDecimal("gst_amount"),
                        rs.getBigDecimal("total_amount")
                });
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bills;
    }

    // Search bill by ID
    public List<Object[]> searchBill(int billId) {

        List<Object[]> bills = new ArrayList<>();

        String sql = "SELECT bill_id, bill_date, subtotal, "
                + "gst_amount, total_amount FROM bills "
                + "WHERE bill_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    bills.add(new Object[]{
                            rs.getInt("bill_id"),
                            rs.getTimestamp("bill_date"),
                            rs.getBigDecimal("subtotal"),
                            rs.getBigDecimal("gst_amount"),
                            rs.getBigDecimal("total_amount")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bills;
    }

    // Retrieve individual items of a bill
    public List<Object[]> getBillItems(int billId) {

        List<Object[]> items = new ArrayList<>();

        String sql = "SELECT product_name, quantity, "
                + "unit_price, line_total FROM bill_items "
                + "WHERE bill_id = ?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, billId);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    items.add(new Object[]{
                            rs.getString("product_name"),
                            rs.getInt("quantity"),
                            rs.getBigDecimal("unit_price"),
                            rs.getBigDecimal("line_total")
                    });
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return items;
    }
}

