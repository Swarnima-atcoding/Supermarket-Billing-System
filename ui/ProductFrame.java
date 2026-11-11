
package ui;

import dao.ProductDAO;
import model.Product;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class ProductFrame extends JFrame {

    private JTextField nameField;
    private JTextField categoryField;
    private JTextField priceField;
    private JTextField stockField;
    private JTextField searchField;

    private JTable productTable;
    private DefaultTableModel tableModel;

    private ProductDAO productDAO;
    private User user;

    public ProductFrame(User user) {

        this.user = user;
        productDAO = new ProductDAO();

        setTitle("Product Management");
        setSize(950, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 252));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        // Heading
        JLabel heading = new JLabel("PRODUCT MANAGEMENT");
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(new Color(37, 99, 235));

        mainPanel.add(heading, BorderLayout.NORTH);

        // Product Form
        JPanel formPanel = new JPanel(new GridLayout(5, 2, 10, 10));
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(
                BorderFactory.createTitledBorder("Product Details")
        );

        nameField = new JTextField();
        categoryField = new JTextField();
        priceField = new JTextField();
        stockField = new JTextField();

        formPanel.add(new JLabel("Product Name:"));
        formPanel.add(nameField);

        formPanel.add(new JLabel("Category:"));
        formPanel.add(categoryField);

        formPanel.add(new JLabel("Price:"));
        formPanel.add(priceField);

        formPanel.add(new JLabel("Stock Quantity:"));
        formPanel.add(stockField);

        JButton addButton = new JButton("Add Product");
        JButton updateButton = new JButton("Update Product");

        formPanel.add(addButton);
        formPanel.add(updateButton);

        // Table
        tableModel = new DefaultTableModel(
                new String[]{
                        "ID", "Product Name", "Category",
                        "Price", "Stock"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        productTable = new JTable(tableModel);
        productTable.setRowHeight(25);
        productTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane scrollPane = new JScrollPane(productTable);

        // Search and Delete Panel
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        bottomPanel.setBackground(new Color(245, 247, 252));

        searchField = new JTextField(18);

        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        JButton deleteButton = new JButton("Delete Product");
        JButton clearButton = new JButton("Clear Fields");

        bottomPanel.add(new JLabel("Search:"));
        bottomPanel.add(searchField);
        bottomPanel.add(searchButton);
        bottomPanel.add(showAllButton);
        bottomPanel.add(deleteButton);
        bottomPanel.add(clearButton);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(new Color(245, 247, 252));

        centerPanel.add(formPanel, BorderLayout.NORTH);
        centerPanel.add(scrollPane, BorderLayout.CENTER);
        centerPanel.add(bottomPanel, BorderLayout.SOUTH);

        mainPanel.add(centerPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Button Actions
        addButton.addActionListener(e -> addProduct());
        updateButton.addActionListener(e -> updateProduct());
        deleteButton.addActionListener(e -> deleteProduct());
        searchButton.addActionListener(e -> searchProducts());
        showAllButton.addActionListener(e -> loadProducts());
        clearButton.addActionListener(e -> clearFields());

        productTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                selectProduct();
            }
        });

        loadProducts();
    }

    // Add Product
    private void addProduct() {

        try {

            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            BigDecimal price = new BigDecimal(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            if (name.isEmpty() || category.isEmpty()
                    || price.compareTo(BigDecimal.ZERO) <= 0
                    || stock < 0) {

                JOptionPane.showMessageDialog(
                        this, "Please enter valid product details."
                );
                return;
            }

            Product product = new Product(
                    0, name, category, price, stock
            );

            if (productDAO.addProduct(product)) {

                JOptionPane.showMessageDialog(
                        this, "Product Added Successfully!"
                );

                clearFields();
                loadProducts();

            } else {
                JOptionPane.showMessageDialog(
                        this, "Unable to add product."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid price and stock quantity."
            );
        }
    }

    // Update Product
    private void updateProduct() {

        int row = productTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a product first."
            );
            return;
        }

        try {

            int id = (int) tableModel.getValueAt(row, 0);

            String name = nameField.getText().trim();
            String category = categoryField.getText().trim();
            BigDecimal price = new BigDecimal(priceField.getText().trim());
            int stock = Integer.parseInt(stockField.getText().trim());

            if (name.isEmpty() || category.isEmpty()
                    || price.compareTo(BigDecimal.ZERO) <= 0
                    || stock < 0) {

                JOptionPane.showMessageDialog(
                        this, "Please enter valid product details."
                );
                return;
            }

            Product product = new Product(
                    id, name, category, price, stock
            );

            if (productDAO.updateProduct(product)) {

                JOptionPane.showMessageDialog(
                        this, "Product Updated Successfully!"
                );

                clearFields();
                loadProducts();

            } else {
                JOptionPane.showMessageDialog(
                        this, "Unable to update product."
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this, "Enter a valid price and stock quantity."
            );
        }
    }

    // Delete Product
    private void deleteProduct() {

        int row = productTable.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(
                    this, "Please select a product first."
            );
            return;
        }

        int id = (int) tableModel.getValueAt(row, 0);

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this product?",
                "Delete Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            if (productDAO.deleteProduct(id)) {

                JOptionPane.showMessageDialog(
                        this, "Product Deleted Successfully!"
                );

                clearFields();
                loadProducts();

            } else {
                JOptionPane.showMessageDialog(
                        this, "Unable to delete product."
                );
            }
        }
    }

    // Load All Products
    private void loadProducts() {

        tableModel.setRowCount(0);

        List<Product> products = productDAO.getAllProducts();

        for (Product product : products) {

            tableModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getStockQuantity()
            });
        }
    }

    // Search Products
    private void searchProducts() {

        String keyword = searchField.getText().trim();

        if (keyword.isEmpty()) {
            loadProducts();
            return;
        }

        tableModel.setRowCount(0);

        List<Product> products = productDAO.searchProducts(keyword);

        for (Product product : products) {

            tableModel.addRow(new Object[]{
                    product.getProductId(),
                    product.getProductName(),
                    product.getCategory(),
                    product.getPrice(),
                    product.getStockQuantity()
            });
        }
    }

    // Select Product from Table
    private void selectProduct() {

        int row = productTable.getSelectedRow();

        if (row != -1) {

            nameField.setText(
                    tableModel.getValueAt(row, 1).toString()
            );

            categoryField.setText(
                    tableModel.getValueAt(row, 2).toString()
            );

            priceField.setText(
                    tableModel.getValueAt(row, 3).toString()
            );

            stockField.setText(
                    tableModel.getValueAt(row, 4).toString()
            );
        }
    }

    // Clear Input Fields
    private void clearFields() {

        nameField.setText("");
        categoryField.setText("");
        priceField.setText("");
        stockField.setText("");
        searchField.setText("");

        productTable.clearSelection();
    }
}
