package com.mycompany.project_bank;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class MainPage {

    private final String username;
    private double balance;
    private JLabel balanceLabel;

    private static final String DB_URL  = "jdbc:mysql://localhost:3306/project_bank";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "password";

    public MainPage(String username, double balance) {
        this.username = username;
        this.balance  = balance;
    }

    public void show() {
        JFrame frame = new JFrame("Main Page");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Botones a la izquierda
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        JButton sendBtn    = new JButton("Send Money");
        JButton depositBtn = new JButton("Deposit");
        sendBtn.setPreferredSize(new Dimension(120, 30));
        depositBtn.setPreferredSize(new Dimension(100, 30));

        sendBtn.addActionListener(e -> onSendMoney(frame));
        depositBtn.addActionListener(e -> onDeposit(frame));

        buttonPanel.add(sendBtn);
        buttonPanel.add(depositBtn);
        topPanel.add(buttonPanel, BorderLayout.WEST);

        balanceLabel = new JLabel(
            "Balance: $" + String.format("%.2f", balance),
            SwingConstants.RIGHT
        );
        balanceLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        topPanel.add(balanceLabel, BorderLayout.EAST);

        frame.add(topPanel, BorderLayout.NORTH);

        JLabel welcomeLabel = new JLabel(
            "Welcome, " + username + "!",
            SwingConstants.CENTER
        );
        welcomeLabel.setFont(new Font("SansSerif", Font.PLAIN, 16));
        frame.add(welcomeLabel, BorderLayout.CENTER);

        frame.setVisible(true);
    }

    private void onSendMoney(JFrame parent) {
        String targetUser = JOptionPane.showInputDialog(
            parent,
            "Enter recipient username:"
        );
        if (targetUser == null || targetUser.trim().isEmpty()) {
            return;
        }

        String amtStr = JOptionPane.showInputDialog(
            parent,
            "Enter amount to send:"
        );
        double amount;
        try {
            amount = Double.parseDouble(amtStr);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Invalid amount");
            return;
        }

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement checkUser = conn.prepareStatement(
                     "SELECT balance FROM users WHERE username = ?"
                 )) {
                checkUser.setString(1, targetUser);
                ResultSet rs = checkUser.executeQuery();
                if (!rs.next()) {
                    conn.rollback();
                    JOptionPane.showMessageDialog(parent, "User \"" + targetUser + "\" not found");
                    return;
                }
            }

            if (this.balance < amount) {
                conn.rollback();
                int opt = JOptionPane.showConfirmDialog(
                    parent,
                    "Insufficient funds. Would you like to deposit?",
                    "Not enough balance",
                    JOptionPane.YES_NO_OPTION
                );
                if (opt == JOptionPane.YES_OPTION) {
                    onDeposit(parent);
                }
                return;
            }

            try (PreparedStatement withdraw = conn.prepareStatement(
                     "UPDATE users SET balance = balance - ? WHERE username = ?"
                 );
                 PreparedStatement deposit = conn.prepareStatement(
                     "UPDATE users SET balance = balance + ? WHERE username = ?"
                 )) {

                withdraw.setDouble(1, amount);
                withdraw.setString(2, this.username);
                withdraw.executeUpdate();

                deposit.setDouble(1, amount);
                deposit.setString(2, targetUser);
                deposit.executeUpdate();
            }

            conn.commit();
            this.balance -= amount;
            updateBalanceLabel();
            JOptionPane.showMessageDialog(
                parent,
                "Successfully sent $" + amount + " to " + targetUser
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Database error: " + ex.getMessage());
        }
    }

    private void onDeposit(JFrame parent) {
        String amtStr = JOptionPane.showInputDialog(
            parent,
            "Enter deposit amount:"
        );
        if (amtStr == null || amtStr.trim().isEmpty()) return;

        double amount;
        try {
            amount = Double.parseDouble(amtStr);
            if (amount <= 0) throw new NumberFormatException();
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(parent, "Invalid amount");
            return;
        }

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(
                 "UPDATE users SET balance = balance + ? WHERE username = ?"
             )) {

            ps.setDouble(1, amount);
            ps.setString(2, this.username);
            int updated = ps.executeUpdate();

            if (updated == 1) {
                this.balance += amount;
                updateBalanceLabel();
                JOptionPane.showMessageDialog(
                    parent,
                    "Successfully deposited $" + amount
                );
            } else {
                JOptionPane.showMessageDialog(parent, "User not found");
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Database error: " + ex.getMessage());
        }
    }

    private void updateBalanceLabel() {
        balanceLabel.setText(
            "Balance: $" + String.format("%.2f", balance)
        );
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
    }
}
