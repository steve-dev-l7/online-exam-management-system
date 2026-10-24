package com.exammanagement.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import org.w3c.dom.events.MouseEvent;

import com.exammanagement.dao.UserDao;

public class SignUpPanel extends JPanel{

    private JTextField txtName;
    private JPasswordField txtPassword;
    private JPasswordField txtRePassword;
    private JButton btnSignUp;


    public SignUpPanel(){

        setBackground(UiTheme.BACKGROUND);
        setLayout(new GridLayout());

        JPanel signUpCard = new JPanel(){
            @Override 
            protected void paintComponent(Graphics g){

                super.paintComponent(g);

                Graphics2D g2 = (Graphics2D)g.create();

                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UiTheme.SURFACE);
                g2.fillRoundRect(0,0,getWidth(),getHeight(),8,8);

                g2.setColor(UiTheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 8, 8);

                g2.dispose();

            }
        };

        signUpCard.setOpaque(false);
        signUpCard.setLayout(new BoxLayout(signUpCard, BoxLayout.Y_AXIS));

        signUpCard.setBorder(new EmptyBorder(20, 30, 20, 30));
        signUpCard.setPreferredSize(new Dimension(480, 520));

        JLabel lblHeader = new JLabel("\"🔐 EMS Signup Authentication\"");
        lblHeader.setFont(new Font("Monospaced", Font.BOLD, 12));

        lblHeader.setForeground(UiTheme.ACCENT);
        lblHeader.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        headerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
        headerPanel.add(lblHeader, BorderLayout.WEST);

        JPanel bannerPanel = new JPanel(new BorderLayout(15, 0));

        bannerPanel.setOpaque(false);
        bannerPanel.setBorder(new EmptyBorder(15, 0, 20, 0));
        bannerPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 70));

        JLabel lblIcon = new JLabel("🎓", JLabel.CENTER);
        lblIcon.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 32));
        
        JPanel brandInfo = new JPanel(new GridLayout(2,1,0,2));
        brandInfo.setOpaque(false);

        JLabel lblBrand = new JLabel("Exam management system");
        lblBrand.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblBrand.setForeground(UiTheme.TEXT);

        JLabel lblSubBrand = new JLabel("EMS-CORE :: CREATE_ACCOUNT");
        lblSubBrand.setFont(new Font("Monospaced", Font.BOLD, 11));
        lblSubBrand.setForeground(UiTheme.MUTED_TEXT);

        brandInfo.add(lblBrand);
        brandInfo.add(lblSubBrand);

        bannerPanel.add(lblIcon, BorderLayout.WEST);
        bannerPanel.add(brandInfo, BorderLayout.CENTER);

        txtName = new JTextField();
        styleField(txtName);

        JPanel namePanel = createFieldWrapper(
            "Full Name", txtName
        );

        txtPassword = new JPasswordField();
        styleField(txtPassword);

        JPanel passwordPanel = createFieldWrapper(
            "Password", txtPassword
        );

        txtRePassword = new JPasswordField();
        styleField(txtRePassword);

        JPanel rePasswordPanel = createFieldWrapper(
            "Re-enter Password", txtRePassword
        );
        btnSignUp = new JButton("➔ Create Account") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();

                g2.setRenderingHint(
                    RenderingHints.KEY_ANTIALIASING,
                    RenderingHints.VALUE_ANTIALIAS_ON
                );

                g2.setColor(
                    getModel().isPressed()
                        ? UiTheme.ACCENT_HOVER
                        : UiTheme.ACCENT
                );

                g2.fillRoundRect(
                    0, 0, getWidth(), getHeight(), 4, 4
                );

                g2.dispose();
                super.paintComponent(g);
            }
        };

        btnSignUp.setContentAreaFilled(false);
        btnSignUp.setBorderPainted(false);
        btnSignUp.setFocusPainted(false);
        btnSignUp.setFont(
            new Font("SansSerif", Font.BOLD, 14)
        );
        btnSignUp.setForeground(UiTheme.ON_ACCENT);
        btnSignUp.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );
        btnSignUp.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 45)
        );
        btnSignUp.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Footer
        JPanel footerPanel = new JPanel(
            new BorderLayout()
        );
        footerPanel.setOpaque(false);
        footerPanel.setBorder(
            new EmptyBorder(12, 0, 0, 0)
        );
        footerPanel.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 30)
        );

        JLabel lblLogin = new JLabel(
            "Already have an account? Sign In"
        );
        lblLogin.setFont(
            new Font("SansSerif", Font.BOLD, 11)
        );
        lblLogin.setForeground(UiTheme.ACCENT);
        lblLogin.setCursor(
            new Cursor(Cursor.HAND_CURSOR)
        );

        footerPanel.add(lblLogin, BorderLayout.CENTER);

        // Assemble Card
        signUpCard.add(headerPanel);
        signUpCard.add(createSeparator());
        signUpCard.add(bannerPanel);

        signUpCard.add(Box.createVerticalStrut(5));
        signUpCard.add(namePanel);
        signUpCard.add(Box.createVerticalStrut(5));
        signUpCard.add(passwordPanel);
        signUpCard.add(Box.createVerticalStrut(5));
        signUpCard.add(rePasswordPanel);

        // Keep button at bottom
        signUpCard.add(Box.createVerticalGlue());
        signUpCard.add(btnSignUp);
        signUpCard.add(Box.createVerticalStrut(10));
        signUpCard.add(createSeparator());
        signUpCard.add(footerPanel);

        add(signUpCard);

        UserDao userDao = new UserDao();


        // Actions
        btnSignUp.addActionListener(e ->{ 
            String name = txtName.getText().trim();
            String password = new String(txtPassword.getPassword());
            String rePassword = new String(txtRePassword.getPassword());

            if(name.isEmpty() || password.isEmpty() || rePassword.isEmpty()){
                JOptionPane.showMessageDialog(this, "\"Please fill in all fields.\"","Signup Error",JOptionPane.ERROR_MESSAGE);
                return;
            }

            if(!password.equals(rePassword)){
                JOptionPane.showMessageDialog(
                this,
                "Passwords do not match!",
                "Signup Error",
                JOptionPane.ERROR_MESSAGE);

                txtRePassword.setText("");
                txtRePassword.requestFocus();
                return;
            }

            JOptionPane.showMessageDialog(this, userDao.createAccount(name, password,rePassword),"Account created",JOptionPane.INFORMATION_MESSAGE);
            MainFrame.instance.switchScreen("LOGIN");
        });

        lblLogin.addMouseListener(new MouseListener() {

            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                MainFrame.instance.switchScreen("LOGIN");
            }

            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                
            }

            @Override
            public void mousePressed(java.awt.event.MouseEvent e) {
                
            }

            @Override
            public void mouseReleased(java.awt.event.MouseEvent e) {
                
            }
            
        });
    }

    private JPanel createFieldWrapper(
            String title, JComponent field) {

        JPanel wrapper = new JPanel(
            new BorderLayout(0, 8)
        );

        wrapper.setOpaque(false);
        wrapper.setBorder(
            new EmptyBorder(0, 0, 8, 0)
        );
        wrapper.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 72)
        );

        JLabel label = new JLabel(title);
        label.setFont(
            new Font("SansSerif", Font.BOLD, 12)
        );
        label.setForeground(UiTheme.MUTED_TEXT);

        wrapper.add(label, BorderLayout.NORTH);
        wrapper.add(field, BorderLayout.CENTER);

        return wrapper;
    }

    private void styleField(JComponent field) {

        field.setBackground(UiTheme.INPUT_BACKGROUND);
        field.setForeground(UiTheme.TEXT);
        field.setFont(
            new Font("Monospaced", Font.PLAIN, 14)
        );

        field.setBorder(
            BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(
                    UiTheme.BORDER, 1
                ),
                new EmptyBorder(8, 10, 8, 10)
            )
        );

        field.setPreferredSize(new Dimension(400, 40));
    }

    private JSeparator createSeparator() {

        JSeparator separator = new JSeparator();
        separator.setForeground(UiTheme.BORDER);
        separator.setMaximumSize(
            new Dimension(Integer.MAX_VALUE, 1)
        );

        return separator;
    }
}

    
