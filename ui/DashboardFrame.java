
package ui;

import model.User;

import javax.swing.*;
import java.awt.*;

public class DashboardFrame extends JFrame {

    private User user;

    public DashboardFrame(User user) {

        this.user = user;

        setTitle("Supermarket Billing System - Dashboard");
        setSize(900, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 247, 252));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new Color(37, 99, 235));
        headerPanel.setBorder(
                BorderFactory.createEmptyBorder(18, 25, 18, 25)
        );

        JLabel titleLabel = new JLabel("SUPERMARKET BILLING SYSTEM");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 23));
        titleLabel.setForeground(Color.WHITE);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setFont(new Font("Arial", Font.BOLD, 13));

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(logoutButton, BorderLayout.EAST);

        logoutButton.addActionListener(e -> logout());

        // Welcome Panel
        JPanel welcomePanel = new JPanel(new GridLayout(2, 1, 5, 5));
        welcomePanel.setBackground(new Color(245, 247, 252));
        welcomePanel.setBorder(
                BorderFactory.createEmptyBorder(25, 30, 15, 30)
        );

        JLabel welcomeLabel = new JLabel(
                "Welcome, " + user.getUsername() + "!"
        );

        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 25));

        JLabel subtitleLabel = new JLabel(
                "Manage your supermarket operations from here."
        );

        subtitleLabel.setFont(new Font("Arial", Font.PLAIN, 15));
        subtitleLabel.setForeground(Color.GRAY);

        welcomePanel.add(welcomeLabel);
        welcomePanel.add(subtitleLabel);

        // Dashboard Buttons
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 25, 25));
        menuPanel.setBackground(new Color(245, 247, 252));
        menuPanel.setBorder(
                BorderFactory.createEmptyBorder(25, 50, 50, 50)
        );

        JButton productButton = createButton("Product Management");
        JButton billingButton = createButton("Billing System");
        JButton historyButton = createButton("Sales History");
        JButton exitButton = createButton("Exit Application");

        menuPanel.add(productButton);
        menuPanel.add(billingButton);
        menuPanel.add(historyButton);
        menuPanel.add(exitButton);

        productButton.addActionListener(e -> openProducts());
        billingButton.addActionListener(e -> openBilling());
        historyButton.addActionListener(e -> openSalesHistory());

        exitButton.addActionListener(e -> {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit Confirmation",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        });

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(welcomePanel, BorderLayout.CENTER);
        mainPanel.add(menuPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JButton createButton(String text) {

        JButton button = new JButton(text);

        button.setFont(new Font("Arial", Font.BOLD, 17));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(37, 99, 235));
        button.setFocusPainted(false);
        button.setBorder(
                BorderFactory.createLineBorder(
                        new Color(210, 220, 240), 2
                )
        );

        return button;
    }

    private void openProducts() {
        new ProductFrame(user).setVisible(true);
    }

    private void openBilling() {
        new BillingFrame(user).setVisible(true);
    }

    private void openSalesHistory() {
        new SalesHistoryFrame().setVisible(true);
    }

    private void logout() {

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to logout?",
                "Logout Confirmation",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {
            dispose();
            new LoginFrame().setVisible(true);
        }
    }
}
