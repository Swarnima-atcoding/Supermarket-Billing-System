
package dao;

import database.DBConnection;
import model.Bill;
import model.BillItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class BillDAO {

    // Save bill and its items
    public boolean saveBill(Bill bill, List<BillItem> items) {

        if (items == null || items.isEmpty()) {
            return false;
        }

        String billSql = "INSERT INTO bills "
                + "(user_id, subtotal, gst_amount, total_amount) "
                + "VALUES (?, ?, ?, ?)";

        String stockSql = "UPDATE products "
                + "SET stock_quantity = stock_quantity - ? "
                + "WHERE product_id = ? AND stock_quantity >= ?";

        String itemSql = "INSERT INTO bill_items "
                + "(bill_id, product_id, product_name, quantity, "
                + "unit_price, line_total) "
                + "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {

                // Insert bill information
                try (PreparedStatement ps = con.prepareStatement(
                        billSql, PreparedStatement.RETURN_GENERATED_KEYS)) {

                    ps.setInt(1, bill.getUserId());
                    ps.setBigDecimal(2, bill.getSubtotal());
                    ps.setBigDecimal(3, bill.getGstAmount());
                    ps.setBigDecimal(4, bill.getTotalAmount());

                    ps.executeUpdate();

                    try (ResultSet rs = ps.getGeneratedKeys()) {

                        if (!rs.next()) {
                            throw new SQLException("Bill ID not generated.");
                        }

                        bill.setBillId(rs.getInt(1));
                    }
                }

                // Insert bill items and update stock
                for (BillItem item : items) {

                    if (item.getQuantity() <= 0 || item.getProductId() <= 0) {
                        throw new SQLException("Invalid product or quantity.");
                    }

                    // Reduce product stock
                    try (PreparedStatement ps = con.prepareStatement(stockSql)) {

                        ps.setInt(1, item.getQuantity());
                        ps.setInt(2, item.getProductId());
                        ps.setInt(3, item.getQuantity());

                        if (ps.executeUpdate() == 0) {
                            throw new SQLException(
                                    "Insufficient stock for: "
                                            + item.getProductName()
                            );
                        }
                    }

                    // Insert bill item
                    try (PreparedStatement ps = con.prepareStatement(itemSql)) {

                        ps.setInt(1, bill.getBillId());
                        ps.setInt(2, item.getProductId());
                        ps.setString(3, item.getProductName());
                        ps.setInt(4, item.getQuantity());
                        ps.setBigDecimal(5, item.getUnitPrice());
                        ps.setBigDecimal(6, item.getLineTotal());

                        ps.executeUpdate();
                    }
                }

                con.commit();

                return true;

            } catch (SQLException e) {

                con.rollback();
                e.printStackTrace();

                return false;
            }

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }
}
