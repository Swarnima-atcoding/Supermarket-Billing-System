
package model;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Bill {

    private int billId;
    private int userId;
    private Timestamp billDate;
    private BigDecimal subtotal;
    private BigDecimal gstAmount;
    private BigDecimal totalAmount;

    public Bill() {
    }

    public Bill(int billId, int userId, Timestamp billDate,
                BigDecimal subtotal, BigDecimal gstAmount,
                BigDecimal totalAmount) {

        this.billId = billId;
        this.userId = userId;
        this.billDate = billDate;
        this.subtotal = subtotal;
        this.gstAmount = gstAmount;
        this.totalAmount = totalAmount;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Timestamp getBillDate() {
        return billDate;
    }

    public void setBillDate(Timestamp billDate) {
        this.billDate = billDate;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public BigDecimal getGstAmount() {
        return gstAmount;
    }

    public void setGstAmount(BigDecimal gstAmount) {
        this.gstAmount = gstAmount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }
}

