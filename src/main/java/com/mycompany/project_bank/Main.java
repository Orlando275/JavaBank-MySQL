package com.mycompany.project_bank;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Main {

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
        
            JFrame frame = new JFrame("Login System");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setResizable(false);
            frame.setLayout(new BorderLayout());

            JPanel bgPanel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    Graphics2D g2 = (Graphics2D) g.create();
                    int w = getWidth(), h = getHeight();
                    GradientPaint paint = new GradientPaint(
                        0, 0, new Color(240, 248, 255),
                        0, h, new Color(173, 216, 230)
                    );
                    g2.setPaint(paint);
                    g2.fillRect(0, 0, w, h);
                    g2.dispose();
                }
            };
            bgPanel.setLayout(new BoxLayout(bgPanel, BoxLayout.Y_AXIS));
            bgPanel.setBorder(BorderFactory.createEmptyBorder(40, 60, 40, 60));

            JLabel title = new JLabel("Welcome to Orlando's Bank", SwingConstants.CENTER);
            title.setAlignmentX(Component.CENTER_ALIGNMENT);
            title.setFont(new Font("SansSerif", Font.BOLD, 26));
            title.setForeground(new Color(33, 37, 41));
            bgPanel.add(title);
            bgPanel.add(Box.createVerticalStrut(30));

            JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
            btnPanel.setOpaque(false);

            JButton loginBtn = createGradientButton("Login", new Dimension(140, 40));
            loginBtn.addActionListener(e -> new LoginWindow().show(frame));

            JButton registerBtn = createGradientButton("Register", new Dimension(140, 40));
            registerBtn.addActionListener(e -> new RegisterWindow().show(frame));

            btnPanel.add(loginBtn);
            btnPanel.add(registerBtn);
            bgPanel.add(btnPanel);

            frame.add(bgPanel, BorderLayout.CENTER);
            frame.pack();
            frame.setSize(700, 300);
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    private static JButton createGradientButton(String text, Dimension size) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                int w = getWidth(), h = getHeight();
                GradientPaint gp = new GradientPaint(
                    0, 0, new Color(173, 216, 230),
                    0, h, Color.WHITE
                );
                g2.setPaint(gp);
                g2.fillRect(0, 0, w, h);
                g2.dispose();
                super.paintComponent(g);
            }
        };

        btn.setContentAreaFilled(false);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(100, 149, 237)));
        btn.setFont(new Font("SansSerif", Font.PLAIN, 16));
        btn.setForeground(Color.DARK_GRAY);
        btn.setPreferredSize(size);
        return btn;
    }
}
