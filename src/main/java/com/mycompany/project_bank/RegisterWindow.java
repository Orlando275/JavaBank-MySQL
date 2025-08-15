package com.mycompany.project_bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class RegisterWindow {

    private static final String HOST       = "localhost";
    private static final String PORT       = "port";
    private static final String DATABASE   = "database";
    private static final String JDBC_URL   =
        "jdbc:mysql://" + HOST + ":" + PORT + "/" + DATABASE +
        "?useSSL=false&serverTimezone=UTC";
    private static final String JDBC_USER  = "<user>";
    private static final String JDBC_PASS  = "password";

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

        JFrame regFrame = new JFrame("Register");
        regFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        regFrame.setResizable(false);
        regFrame.setLayout(new BorderLayout());

        JPanel fieldsPanel = new JPanel(new GridLayout(4, 2, 10, 10));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        fieldsPanel.add(new JLabel("Username:"));
        JTextField userField = new JTextField(15);
        fieldsPanel.add(userField);

        fieldsPanel.add(new JLabel("Email:"));
        JTextField emailField = new JTextField(15);
        fieldsPanel.add(emailField);

        fieldsPanel.add(new JLabel("Password:"));
        JPasswordField passField = new JPasswordField(15);
        fieldsPanel.add(passField);

        fieldsPanel.add(new JLabel("Confirm Password:"));
        JPasswordField confirmPassField = new JPasswordField(15);
        fieldsPanel.add(confirmPassField);

        JPanel buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton regBtn = new JButton("Register");
        regBtn.setPreferredSize(new Dimension(120, 30));
        JButton backBtn = new JButton("Back");
        backBtn.setPreferredSize(new Dimension(120, 30));

        regBtn.addActionListener(e -> {
            String username        = userField.getText().trim();
            String email           = emailField.getText().trim();
            String password        = new String(passField.getPassword());
            String confirmPassword = new String(confirmPassField.getPassword());

            if (!password.equals(confirmPassword)) {
                JOptionPane.showMessageDialog(
                    regFrame,
                    "Passwords do not match. Please try again.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
                return;
            }

         
            try (Connection conn = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASS)) {
      
                String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS users (
                      id INT AUTO_INCREMENT PRIMARY KEY,
                      username VARCHAR(255) NOT NULL,
                      email VARCHAR(255) NOT NULL,
                      password VARCHAR(255) NOT NULL
                    )
                    """;
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute(createTableSQL);
                }

                String insertSQL = "INSERT INTO users (username, email, password) VALUES (?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertSQL)) {
                    ps.setString(1, username);
                    ps.setString(2, email);
                    ps.setString(3, password);
                    ps.executeUpdate();
                }

                JOptionPane.showMessageDialog(
                    regFrame,
                    "Registered with username: " + username,
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
                );
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                    regFrame,
                    "Database error:\n" + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        });

        backBtn.addActionListener(e -> {
            regFrame.dispose();
            mainFrame.setVisible(true);
        });

        buttonsPanel.add(regBtn);
        buttonsPanel.add(backBtn);

        regFrame.add(fieldsPanel, BorderLayout.CENTER);
        regFrame.add(buttonsPanel, BorderLayout.SOUTH);

        regFrame.pack();
        regFrame.setSize(450, 300);
        regFrame.setMinimumSize(new Dimension(400, 280));
        regFrame.setLocationRelativeTo(null);
        regFrame.setVisible(true);
    }
}
