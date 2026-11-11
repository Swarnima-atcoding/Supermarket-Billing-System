
package ui;

import dao.SalesHistoryDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class SalesHistoryFrame extends JFrame {

    private JTable billTable;
    private JTable itemTable;

    private DefaultTableModel billModel;
    private DefaultTableModel itemModel;

    private JTextField searchField;

    private SalesHistoryDAO salesDAO;

    public SalesHistoryFrame() {

        salesDAO = new SalesHistoryDAO();

        setTitle("Supermarket Billing System - Sales History");
        setSize(1000, 650);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(245, 247, 252));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(15, 20, 15, 20)
        );

        // Heading
        JLabel heading = new JLabel("SALES HISTORY");
        heading.setFont(new Font("Arial", Font.BOLD, 23));
        heading.setForeground(new Color(37, 99, 235));

        mainPanel.add(heading, BorderLayout.NORTH);

        // Search Panel
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBackground(Color.WHITE);

        searchField = new JTextField(18);

        JButton searchButton = new JButton("Search Bill");
        JButton showAllButton = new JButton("Show All Bills");

        searchPanel.add(new JLabel("Enter Bill ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);

        // Bill Table
        billModel = new DefaultTableModel(
                new String[]{
                        "Bill ID", "Bill Date",
                        "Subtotal", "GST", "Total Amount"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        billTable = new JTable(billModel);
        billTable.setRowHeight(25);
        billTable.setSelectionMode(
                ListSelectionModel.SINGLE_SELECTION
        );

        JScrollPane billScroll = new JScrollPane(billTable);
        billScroll.setBorder(
                BorderFactory.createTitledBorder("Previous Bills")
        );

        // Bill Items Table
        itemModel = new DefaultTableModel(
                new String[]{
                        "Product Name", "Quantity",
                        "Unit Price", "Line Total"
                }, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        itemTable = new JTable(itemModel);
        itemTable.setRowHeight(25);

        JScrollPane itemScroll = new JScrollPane(itemTable);
        itemScroll.setBorder(
                BorderFactory.createTitledBorder("Selected Bill Details")
        );

        // Center Panel
        JPanel centerPanel = new JPanel(new GridLayout(2, 1, 10, 10));
        centerPanel.setBackground(new Color(245, 247, 252));

        centerPanel.add(billScroll);
        centerPanel.add(itemScroll);

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.setBackground(new Color(245, 247, 252));

        topPanel.add(searchPanel, BorderLayout.NORTH);
        topPanel.add(centerPanel, BorderLayout.CENTER);

        mainPanel.add(topPanel, BorderLayout.CENTER);

        add(mainPanel);

        // Button Actions
        searchButton.addActionListener(e -> searchBill());
        showAllButton.addActionListener(e -> {
            searchField.setText("");
            loadBills();
        });

        // Load selected bill items
        billTable.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = billTable.getSelectedRow();

                if (row != -1) {

                    int billId = (int) billModel.getValueAt(row, 0);

                    loadBillItems(billId);
                }
            }
        });

        loadBills();
    }

    // Load All Bills
    private void loadBills() {

        billModel.setRowCount(0);
        itemModel.setRowCount(0);

        List<Object[]> bills = salesDAO.getAllBills();

        for (Object[] bill : bills) {
            billModel.addRow(bill);
        }
    }

    // Search Bill by ID
    private void searchBill() {

        try {

            String searchText = searchField.getText().trim();

            if (searchText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "Please enter a Bill ID."
                );

                return;
            }

            int billId = Integer.parseInt(searchText);

            if (billId <= 0) {

                JOptionPane.showMessageDialog(
                        this, "Enter a valid Bill ID."
                );

                return;
            }

            billModel.setRowCount(0);
            itemModel.setRowCount(0);

            List<Object[]> bills = salesDAO.searchBill(billId);

            for (Object[] bill : bills) {
                billModel.addRow(bill);
            }

            if (bills.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this, "No bill found with this ID."
                );
            }

        } catch (NumberFormatException e) {

            JOptionPane.showMessageDialog(
                    this, "Bill ID must be a valid number."
            );
        }
    }

    // Load Items of Selected Bill
    private void loadBillItems(int billId) {

        itemModel.setRowCount(0);

        List<Object[]> items = salesDAO.getBillItems(billId);

        for (Object[] item : items) {
            itemModel.addRow(item);
        }
    }
}

