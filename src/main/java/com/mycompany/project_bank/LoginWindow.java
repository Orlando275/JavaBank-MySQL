package com.mycompany.project_bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class LoginWindow {

    // MySQL connection
    private static final String HOST      = "localhost";
    private static final String PORT      = "3306";
    private static final String DATABASE  = "project_bank";
    private static final String JDBC_URL  =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
        "?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASS = "your_mysql_password";

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            JOptionPane.showMessageDialog(
                null,
                "MySQL JDBC Driver not found.\n" + e.getMessage(),
                "Driver Error",
                JOptionPane.ERROR_MESSAGE
            );
            System.exit(1);
        }
    }

    public void show(JFrame mainFrame) {

        mainFrame.setVisible(false);

        // build login frame
        JFrame loginFrame = new JFrame("Login");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setResizable(false);
        loginFrame.setLayout(new BorderLayout());

        // input fields panel
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        fieldsPanel.add(new JLabel("Username:"));
        JTextField userField = new JTextField(15);
        fieldsPanel.add(userField);
        fieldsPanel.add(new JLabel("Password:"));
        JPasswordField passField = new JPasswordField(15);
        fieldsPanel.add(passField);

        // buttons panel
        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton loginBtn = new JButton("Login");
        loginBtn.setPreferredSize(new Dimension(100, 30));
        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(100, 30));

        loginBtn.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                JOptionPane.showMessageDialog(
                    loginFrame,
                    "Please enter both username and password.",
                    "Missing Data",
                    JOptionPane.WARNING_MESSAGE
                );
                return;
            }

            String query = "SELECT password, balance FROM users WHERE username = ?";
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS);
                 PreparedStatement ps = conn.prepareStatement(query)) {

                ps.setString(1, username);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        String storedPass = rs.getString("password");
                        double balance    = rs.getDouble("balance");

                        if (password.equals(storedPass)) {
                            // successful login → open main page
                            loginFrame.dispose();
                            new MainPage(username, balance).show();
                        } else {
                            JOptionPane.showMessageDialog(
                                loginFrame,
                                "Incorrect password. Please try again.",
                                "Login Failed",
                                JOptionPane.ERROR_MESSAGE
                            );
                        }
                    } else {
                        JOptionPane.showMessageDialog(
                            loginFrame,
                            "Username does not exist.",
                            "Login Failed",
                            JOptionPane.ERROR_MESSAGE
                        );
                    }
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    loginFrame,
                    "Database error:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        // back button: close and show main
        backBtn.addActionListener(e -> {
            loginFrame.dispose();
            mainFrame.setVisible(true);
        });

        buttonsPanel.add(loginBtn);
        buttonsPanel.add(backBtn);

        loginFrame.add(fieldsPanel, BorderLayout.CENTER);
        loginFrame.add(buttonsPanel, BorderLayout.SOUTH);

        loginFrame.pack();
        loginFrame.setSize(400, 220);
        loginFrame.setMinimumSize(new Dimension(350, 200));
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
    }
}
