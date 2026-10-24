package com.exammanagement.ui;

import com.exammanagement.dao.UserDao;
import com.exammanagement.model.Users;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class LoginPanel extends JPanel {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnSignIn;

    public LoginPanel() {

        setBackground(UiTheme.BACKGROUND);
        setLayout(new GridBagLayout()); 
        
        // Inner Container Panel
        JPanel loginCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UiTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                g2.setColor(UiTheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);
                g2.dispose();
            }
        };
        loginCard.setOpaque(false);
        loginCard.setLayout(new BoxLayout(loginCard, BoxLayout.Y_AXIS));
        loginCard.setBorder(new EmptyBorder(20, 30, 20, 30));
        loginCard.setPreferredSize(new Dimension(480, 520));

        // 1. Header
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        
        JLabel lblAuthHeader = new JLabel("🔒 EMS Login Authentication", JLabel.LEFT);
        lblAuthHeader.setFont(new Font("Monospaced", Font.BOLD, 12));
        lblAuthHeader.setForeground(UiTheme.ACCENT);

        // 2. Banner
        JPanel bannerPanel = new JPanel(new BorderLayout(15, 0));
        bannerPanel.setOpaque(false);
        bannerPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));
        
        JLabel lblSystemIcon = new JLabel("🎓", JLabel.CENTER);
        lblSystemIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        lblSystemIcon.setForeground(UiTheme.ACCENT);
        
        JPanel brandInfo = new JPanel(new GridLayout(2, 1, 0, 2));
        brandInfo.setOpaque(false);
        JLabel lblBrandName = new JLabel("Exam Management System");
        lblBrandName.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBrandName.setForeground(UiTheme.TEXT);
        JLabel lblSubBrand = new JLabel("EMS-CORE :: NODE_SECURE_AUTH");
        lblSubBrand.setFont(new Font("Monospaced", Font.BOLD, 11));
        lblSubBrand.setForeground(UiTheme.MUTED_TEXT);
        brandInfo.add(lblBrandName);
        brandInfo.add(lblSubBrand);
        
        bannerPanel.add(lblSystemIcon, BorderLayout.WEST);
        bannerPanel.add(brandInfo, BorderLayout.CENTER);

        // 3. Username Field
        txtUsername = new JTextField();
        styleFuturisticField(txtUsername);
        addPlaceholderBehavior(txtUsername, "e.g. REG-2024-8841 or UID");
        JPanel panelUser = createFieldWrapper("Username / Registration ID", txtUsername, null);

        // 4. Password Field
        txtPassword = new JPasswordField();
        styleFuturisticField(txtPassword);
        JPanel panelPass = createFieldWrapper("Security Token / Password", txtPassword, "Forgot?");

        // 5. Buttons
        JPanel buttonRow = new JPanel(new GridBagLayout());
        buttonRow.setOpaque(false);
        buttonRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        GridBagConstraints gbcBtn = new GridBagConstraints();
        gbcBtn.fill = GridBagConstraints.BOTH;
        gbcBtn.weighty = 1.0;

        btnSignIn = new JButton("➔ Sign In / Start Session") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? UiTheme.ACCENT_HOVER : UiTheme.ACCENT);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnSignIn.setContentAreaFilled(false);
        btnSignIn.setBorderPainted(false);
        btnSignIn.setFocusPainted(false);
        btnSignIn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btnSignIn.setForeground(UiTheme.ON_ACCENT);
        btnSignIn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JButton btnSignUp = new JButton("Signup") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? UiTheme.ACCENT_SOFT : UiTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 4, 4);
                g2.setColor(UiTheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 4, 4);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btnSignUp.setContentAreaFilled(false);
        btnSignUp.setBorderPainted(false);
        btnSignUp.setFocusPainted(false);
        btnSignUp.setFont(new Font("SansSerif", Font.BOLD, 13));
        btnSignUp.setForeground(UiTheme.MUTED_TEXT);
        btnSignUp.setCursor(new Cursor(Cursor.HAND_CURSOR));

        gbcBtn.gridx = 0;
        gbcBtn.weightx = 0.75;
        buttonRow.add(btnSignIn, gbcBtn);
        
        gbcBtn.gridx = 1;
        gbcBtn.weightx = 0.25;
        gbcBtn.insets = new Insets(0, 10, 0, 0);
        buttonRow.add(btnSignUp, gbcBtn);

        // 6. Footer Status
        JPanel footerRow = new JPanel(new BorderLayout());
        footerRow.setOpaque(false);
        footerRow.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        footerRow.setBorder(new EmptyBorder(15, 0, 0, 0));
        
        JLabel lblConnStatus = new JLabel("🔵 Connected: localhost:3306 (v2.4.0)", JLabel.LEFT);
        lblConnStatus.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblConnStatus.setForeground(UiTheme.MUTED_TEXT);
        
        JLabel lblPing = new JLabel("PING: 14ms 🌐", JLabel.RIGHT);
        lblPing.setFont(new Font("Monospaced", Font.PLAIN, 11));
        lblPing.setForeground(UiTheme.MUTED_TEXT);
        
        footerRow.add(lblConnStatus, BorderLayout.WEST);
        footerRow.add(lblPing, BorderLayout.EAST);

        // Assembling components to loginCard
        loginCard.add(headerPanel);
        loginCard.add(createSeparator());
        loginCard.add(bannerPanel);
        
        // Spacing tweaks
        loginCard.add(Box.createVerticalStrut(10)); 
        loginCard.add(panelUser);
        loginCard.add(Box.createVerticalStrut(15)); // Textfields-inu idayil gap
        loginCard.add(panelPass);
        
        // Pushes everything below this to the bottom
        loginCard.add(Box.createVerticalGlue()); 
        
        loginCard.add(buttonRow);
        loginCard.add(Box.createVerticalStrut(15)); // Buttons-kum footer-num idayil gap
        loginCard.add(createSeparator());
        loginCard.add(footerRow);

        add(loginCard);

        // Actions
        btnSignIn.addActionListener(e -> attemptLogin());
        
        btnSignUp.addActionListener(e -> {
            MainFrame.instance.switchScreen("SIGNUP");
        });
    }

    private JPanel createFieldWrapper(String title, JComponent inputField, String rightText) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8)); 
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 0, 15, 0)); 
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75)); 

        JPanel labelRow = new JPanel(new BorderLayout());
        labelRow.setOpaque(false);

        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTitle.setForeground(UiTheme.MUTED_TEXT);
        labelRow.add(lblTitle, BorderLayout.WEST);

        if (rightText != null) {
            JLabel lblRight = new JLabel(rightText);
            lblRight.setFont(new Font("SansSerif", Font.BOLD, 11));
            lblRight.setForeground(UiTheme.ACCENT);
            lblRight.setCursor(new Cursor(Cursor.HAND_CURSOR));
            labelRow.add(lblRight, BorderLayout.EAST);
        }

        wrapper.add(labelRow, BorderLayout.NORTH);
        wrapper.add(inputField, BorderLayout.CENTER);

        return wrapper;
    }

    private JSeparator createSeparator() {
        JSeparator sep = new JSeparator();
        sep.setForeground(UiTheme.BORDER);
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
        return sep;
    }

    private void styleFuturisticField(JComponent c) {
        c.setBackground(UiTheme.INPUT_BACKGROUND);
        c.setForeground(UiTheme.TEXT);
        c.setFont(new Font("Monospaced", Font.PLAIN, 14));
        c.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiTheme.BORDER, 1),
            new EmptyBorder(8, 10, 8, 10)
        ));
        c.setPreferredSize(new Dimension(400, 40));
    }

    private void addPlaceholderBehavior(JTextField txt, String placeholder) {
        txt.setText(placeholder);
        txt.setForeground(UiTheme.PLACEHOLDER);
        txt.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText("");
                    txt.setForeground(UiTheme.TEXT);
                }
            }
            @Override
            public void focusLost(FocusEvent e) {
                if (txt.getText().trim().isEmpty()) {
                    txt.setText(placeholder);
                    txt.setForeground(UiTheme.PLACEHOLDER);
                }
            }
        });
    }

    private void attemptLogin() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword()).trim();
        
        if (username.isEmpty() || password.isEmpty() || username.equals("e.g. REG-2024-8841 or UID")) {
            JOptionPane.showMessageDialog(this, "⚠️ Authenticate failed! Fields cannot be empty.", "Auth Guard Exception", JOptionPane.ERROR_MESSAGE);
            return;
        }

        UserDao userDao = new UserDao();
        Users user = userDao.validateCredentials(username, password);
        
        if (user != null) {
            MainFrame.instance.setCurrentUser(user); 
            MainFrame.instance.switchScreen("DASHBOARD");
        } else {
            JOptionPane.showMessageDialog(this, "❌ Access Denied! Invalid signature or authentication Token.", "Auth Core Exception", JOptionPane.ERROR_MESSAGE);
        }
    }
}