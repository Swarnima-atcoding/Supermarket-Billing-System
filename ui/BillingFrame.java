
package ui;

import dao.BillDAO;
import dao.ProductDAO;
import model.Bill;
import model.BillItem;
import model.Product;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class BillingFrame extends JFrame {

    private User user;

    private JTable productTable;
    private JTable cartTable;

    private DefaultTableModel productModel;
    private DefaultTableModel cartModel;

    private JTextField quantityField;
    private JLabel subtotalLabel;
    private JLabel gstLabel;
    private JLabel totalLabel;

    private List<Product> products;
    private List<BillItem> cartItems;

    private ProductDAO productDAO;
    private BillDAO billDAO;

    private static final BigDecimal GST_RATE =
            new BigDecimal("0.05");

    public BillingFrame(User user) {

        this.user = user;

        productDAO = new ProductDAO();
        billDAO = new BillDAO();

        products = new ArrayList<>();
        cartItems = new ArrayList<>();

        setTitle("Supermarket Billing System - Billing");
        setSize(1050, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 252));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        JLabel heading = new JLabel("CUSTOMER BILLING");
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(new Color(37, 99, 235));

        mainPanel.add(heading, BorderLayout.NORTH);

        // Product Table
        productModel = new DefaultTableModel(
                new String[]{
                        "ID", "Product Name", "Category",
                        "Price", "Available Stock"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(productModel);
        productTable.setRowHeight(24);
        productTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane productScroll = new JScrollPane(productTable);
        productScroll.setBorder(
                BorderFactory.createTitledBorder("Available Products")
        );

        

        // Add to Cart Panel
        JPanel addPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        addPanel.setBackground(Color.WHITE);

        quantityField = new JTextField(8);

        JButton addButton = new JButton("Add to Cart");

        addPanel.add(new JLabel("Quantity:"));
        addPanel.add(quantityField);
        addPanel.add(addButton);

        // Cart Table
        cartModel = new DefaultTableModel(
                new String[]{
                        "Product ID", "Product Name",
                        "Quantity", "Unit Price", "Line Total"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        cartTable = new JTable(cartModel);
        cartTable.setRowHeight(24);

        JScrollPane cartScroll = new JScrollPane(cartTable);
        productScroll.setPreferredSize(new Dimension(1000, 220));
        cartScroll.setPreferredSize(new Dimension(1000, 180));
        cartScroll.setBorder(
                BorderFactory.createTitledBorder("Shopping Cart")
        );

        // Billing Summary
        JPanel summaryPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        summaryPanel.setBackground(Color.WHITE);
        summaryPanel.setBorder(
                BorderFactory.createTitledBorder("Bill Summary")
        );

        subtotalLabel = new JLabel("0.00");
        gstLabel = new JLabel("0.00");
        totalLabel = new JLabel("0.00");

        summaryPanel.add(new JLabel("Subtotal:"));
        summaryPanel.add(subtotalLabel);

        summaryPanel.add(new JLabel("GST (5%):"));
        summaryPanel.add(gstLabel);

        summaryPanel.add(new JLabel("Total Amount:"));
        summaryPanel.add(totalLabel);

        JButton removeButton = new JButton("Remove Item");
        JButton clearButton = new JButton("Clear Cart");
        JButton generateButton = new JButton("Generate Bill");

        summaryPanel.add(removeButton);
        summaryPanel.add(clearButton);

        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBackground(new Color(245, 247, 252));

        bottomPanel.add(cartScroll, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new BorderLayout(10, 10));
        actionPanel.setBackground(new Color(245, 247, 252));

        actionPanel.add(summaryPanel, BorderLayout.CENTER);
        actionPanel.add(generateButton, BorderLayout.EAST);

        bottomPanel.add(actionPanel, BorderLayout.SOUTH);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 247, 252));

        centerPanel.add(productScroll, BorderLayout.CENTER);
        centerPanel.add(addPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);

        // Button Actions
        addButton.addActionListener(e -> addToCart());
        removeButton.addActionListener(e -> removeItem());
        clearButton.addActionListener(e -> clearCart());
        generateButton.addActionListener(e -> generateBill());

        loadProducts();
    }

    // Load Available Products
    private void loadProducts() {

        productModel.setRowCount(0);

        products = productDAO.getAllProducts();

        for (Product product : products) {

            productModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getStockQuantity()
            });
        }
    }

    // Add Product to Cart
    private void addToCart() {

        int row = productTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a product first."
            );
            return;
        }

        try {

            int quantity = Integer.parseInt(
                    quantityField.getText().trim()
            );

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(
                        this, "Quantity must be greater than zero."
                );
                return;
            }

            Product product = products.get(row);

            int existingQuantity = 0;

            for (BillItem item : cartItems) {

                if (item.getProductId() == product.getProductId()) {
                    existingQuantity = item.getQuantity();
                    break;
                }
            }

            if (existingQuantity + quantity > product.getStockQuantity()) {

                JOptionPane.showMessageDialog(
                        this, "Insufficient stock available."
                );
                return;
            }

            int newQuantity = existingQuantity + quantity;

            BigDecimal lineTotal = product.getPrice().multiply(
                    BigDecimal.valueOf(newQuantity)
            );

            boolean found = false;

            for (BillItem item : cartItems) {

                if (item.getProductId() == product.getProductId()) {

                    item.setQuantity(newQuantity);
                    item.setLineTotal(lineTotal);

                    found = true;
                    break;
                }
            }

            if (!found) {

                BillItem item = new BillItem(
                        0,
                        0,
                        product.getProductId(),
                        product.getProductName(),
                        quantity,
                        product.getPrice(),
                        lineTotal
                );

                cartItems.add(item);
            }

            refreshCart();

            quantityField.setText("");

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Please enter a valid quantity."
            );
        }
    }

    // Refresh Cart Table and Summary
    private void refreshCart() {

        cartModel.setRowCount(0);

        BigDecimal subtotal = BigDecimal.ZERO;

        for (BillItem item : cartItems) {

            cartModel.addRow(new Object[]{
                    item.getProductId(),
                    item.getProductName(),
                    item.getQuantity(),
                    item.getUnitPrice(),
                    item.getLineTotal()
            });

            subtotal = subtotal.add(item.getLineTotal());
        }

        BigDecimal gst = subtotal.multiply(GST_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subtotal.add(gst);

        subtotalLabel.setText(subtotal.toString());
        gstLabel.setText(gst.toString());
        totalLabel.setText(total.toString());
    }

    // Remove Selected Cart Item
    private void removeItem() {

        int row = cartTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select an item from the cart."
            );
            return;
        }

        cartItems.remove(row);

        refreshCart();
    }

    // Clear Shopping Cart
    private void clearCart() {

        cartItems.clear();

        refreshCart();
    }

    // Generate Customer Bill
    private void generateBill() {

        if (cartItems.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this, "Please add products to the cart first."
            );

            return;
        }

        BigDecimal subtotal = BigDecimal.ZERO;

        for (BillItem item : cartItems) {
            subtotal = subtotal.add(item.getLineTotal());
        }

        BigDecimal gst = subtotal.multiply(GST_RATE)
                .setScale(2, RoundingMode.HALF_UP);

        BigDecimal total = subtotal.add(gst);

        Bill bill = new Bill(
                0,
                user.getUserId(),
                null,
                subtotal,
                gst,
                total
        );

        List<BillItem> savedItems = new ArrayList<>(cartItems);

        boolean success = billDAO.saveBill(bill, savedItems);

        if (success) {

            StringBuilder invoice = new StringBuilder();

            invoice.append("SUPERMARKET BILLING SYSTEM\n");
            invoice.append("----------------------------------\n");
            invoice.append("Bill ID: ").append(bill.getBillId()).append("\n");
            invoice.append("Admin: ").append(user.getUsername()).append("\n");
            invoice.append("----------------------------------\n");

            for (BillItem item : savedItems) {

                invoice.append(item.getProductName())
                        .append(" x ")
                        .append(item.getQuantity())
                        .append(" = ")
                        .append(item.getLineTotal())
                        .append("\n");
            }

            invoice.append("----------------------------------\n");
            invoice.append("Subtotal: ").append(subtotal).append("\n");
            invoice.append("GST (5%): ").append(gst).append("\n");
            invoice.append("Total: ").append(total).append("\n");
            invoice.append("----------------------------------\n");
            invoice.append("Thank you for shopping!");

            JTextArea invoiceArea = new JTextArea(invoice.toString());
            invoiceArea.setEditable(false);
            invoiceArea.setFont(new Font("Monospaced", Font.PLAIN, 13));

            JOptionPane.showMessageDialog(
                    this,
                    new JScrollPane(invoiceArea),
                    "Bill Generated Successfully",
                    JOptionPane.INFORMATION_MESSAGE
            );

            clearCart();
            loadProducts();

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Bill generation failed. Please check stock and try again.",
                    "Billing Error",
                    JOptionPane.ERROR_MESSAGE
            );

            loadProducts();
        }
    }
}
