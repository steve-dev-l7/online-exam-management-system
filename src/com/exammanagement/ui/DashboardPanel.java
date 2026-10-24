package com.exammanagement.ui;

import java.awt.*;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import com.exammanagement.dao.UserDao;
import com.exammanagement.model.Users;
import com.exammanagement.model.Questions;
import com.exammanagement.ai.Ollama;

public class DashboardPanel extends JPanel {

    private JLabel lblWelcome;
    private JComboBox<String> cbSubject;
    private static JTextField txtTopic;
    private static JButton btnStartExam;
    private JLabel lblTotalScore;
    private static JButton logout;
    
    JPanel leaderboardCard;

    public DashboardPanel() {
        setBackground(UiTheme.BACKGROUND);
        setLayout(new BorderLayout(20, 20)); 
        setBorder(new EmptyBorder(30, 40, 30, 40));

        // 1. Top Header Section
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        lblWelcome = new JLabel("Welcome, STEVE", JLabel.LEFT);
        lblWelcome.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblWelcome.setForeground(UiTheme.TEXT);
        
        logout = new JButton("Logout");
        logout.setFont(new Font("Monospaced", Font.BOLD, 12));
        stylePrimaryButton(logout);
        
        headerPanel.add(lblWelcome, BorderLayout.WEST);
        headerPanel.add(logout, BorderLayout.EAST);

        logout.addActionListener(e->{
            MainFrame.instance.switchScreen("LOGIN");
        });

        // 2. Main Content Split (Left: Exam Config, Right: Stats & Leaderboard)
        JPanel mainSplitPanel = new JPanel(new GridLayout(1, 2, 30, 0));
        mainSplitPanel.setOpaque(false);

        // --- LEFT COLUMN (Exam Config Card) ---
        JPanel leftCard = createDarkCard();
        
        JLabel lblSectionTitle = new JLabel("⚡ Configure AI Examination");
        lblSectionTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblSectionTitle.setForeground(UiTheme.ACCENT);
        
        cbSubject = new JComboBox<>(new String[]{"Java Programming", "Database Management", "Python programming"});
        styleFuturisticField(cbSubject);
        JPanel panelSubject = createFieldWrapper("Select Core Subject", cbSubject);
        
        txtTopic = new JTextField();
        styleFuturisticField(txtTopic);
        addPlaceholderBehavior(txtTopic, "Ex: Multithreading, Polymorphism");
        JPanel panelTopic = createFieldWrapper("Specify Sub-Topic (AI Prompt)", txtTopic);
        
        btnStartExam = new JButton("Generate & Start Exam");
        stylePrimaryButton(btnStartExam);
        
        leftCard.add(lblSectionTitle);
        leftCard.add(Box.createVerticalStrut(15));
        leftCard.add(createSeparator());
        leftCard.add(Box.createVerticalStrut(20));
        leftCard.add(panelSubject);
        leftCard.add(panelTopic);
        leftCard.add(Box.createVerticalGlue());
        leftCard.add(btnStartExam);

        // --- RIGHT COLUMN (Stats & Leaderboard) ---
        JPanel rightContainer = new JPanel(new GridLayout(2, 1, 0, 20));
        rightContainer.setOpaque(false);

        // Top Right: User Stats Card
        JPanel statsCard = createDarkCard();
        JLabel lblStatsTitle = new JLabel("📊 Your Performance");
        lblStatsTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblStatsTitle.setForeground(UiTheme.ACCENT);
        
        lblTotalScore = new JLabel("Total XP / Score: 1,250");
        lblTotalScore.setFont(new Font("Monospaced", Font.BOLD, 18));
        lblTotalScore.setForeground(UiTheme.TEXT);
        
        
        
        statsCard.add(lblStatsTitle);
        statsCard.add(Box.createVerticalStrut(15));
        statsCard.add(createSeparator());
        statsCard.add(Box.createVerticalStrut(20));
        statsCard.add(lblTotalScore);
        statsCard.add(Box.createVerticalStrut(10));
        

        // Bottom Right: Leaderboard Card
        leaderboardCard = createDarkCard();
        JLabel lblLeaderboardTitle = new JLabel("🏆 Global Leaderboard (Top 3)");
        lblLeaderboardTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblLeaderboardTitle.setForeground(UiTheme.ACCENT);
        
        leaderboardCard.add(lblLeaderboardTitle);
        leaderboardCard.add(Box.createVerticalStrut(15));
        leaderboardCard.add(createSeparator());
        leaderboardCard.add(Box.createVerticalStrut(15));
        
        // Dummy Leaderboard Data (Replace with DB fetch later)
        leaderboardCard.add(createLeaderboardRow("1. Sarah Connor", "2,400 XP", UiTheme.GOLD));
        leaderboardCard.add(Box.createVerticalStrut(10));
        leaderboardCard.add(createLeaderboardRow("2. John Wick", "2,150 XP", UiTheme.SILVER));
        leaderboardCard.add(Box.createVerticalStrut(10));
        leaderboardCard.add(createLeaderboardRow("3. Steve (You)", "1,250 XP", UiTheme.BRONZE));
        leaderboardCard.add(Box.createVerticalGlue());

        rightContainer.add(statsCard);
        rightContainer.add(leaderboardCard);

        mainSplitPanel.add(leftCard);
        mainSplitPanel.add(rightContainer);

        // Assembly
        add(headerPanel, BorderLayout.NORTH);
        add(mainSplitPanel, BorderLayout.CENTER);

        // Action
        btnStartExam.addActionListener(e -> {
            String topic = txtTopic.getText().trim();

            String subject = cbSubject.getSelectedItem().toString();
            //Disabling the button 
            btnStartExam.setEnabled(false);
            btnStartExam.setText("⏳ Generating via AI... Please Wait");
            btnStartExam.setBackground(UiTheme.DISABLED);

            SwingWorker<List<Questions> , Void> aiWorker = new SwingWorker<>() {

                @Override
                protected List<Questions> doInBackground() throws Exception {
                    System.out.println("[DASHBOARD] Fetching questions for: " + topic);

                    return Ollama.generateExam(subject,topic, 5);
                }

                @Override 
                protected void done(){
                    try{

                        List<Questions> generatedQuestions = get();

                        if(generatedQuestions!=null && !generatedQuestions.isEmpty()){

                            System.out.println("[DASHBOARD] AI Generation Success! Navigating to Exam...");

                            // 3. MainFrame kitta Questions-a kuduthu Exam Screen-kku switch panna solrom
                            MainFrame.instance.startExamSession(generatedQuestions);

                        }else{
                            JOptionPane.showMessageDialog(DashboardPanel.this, "Failed to generate questions. Is Ollama running?", "AI Error", JOptionPane.ERROR_MESSAGE);
                            resetButton();
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                        JOptionPane.showMessageDialog(DashboardPanel.this, "Error during generation!", "System Error", JOptionPane.ERROR_MESSAGE);
                        resetButton();
                    }
                }

                
                
            };
            aiWorker.execute();

        });
    }

    public static  void resetButton(){
        btnStartExam.setEnabled(true);
        btnStartExam.setText("⚡ Generate & Start Exam");
        btnStartExam.setBackground(UiTheme.ACCENT);
        txtTopic.setText("");
        addPlaceholderBehavior(txtTopic, "Ex: Multithreading, Polymorphism");
    }
    

    private JPanel createDarkCard() {
        JPanel card = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UiTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
                g2.setColor(UiTheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);
                g2.dispose();
            }
        };
        card.setOpaque(false);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(20, 25, 20, 25));
        return card;
    }

    private JPanel createLeaderboardRow(String name, String score, Color highlight) {
        JPanel row = new JPanel(new BorderLayout());
        row.setOpaque(false);
        row.setMaximumSize(new Dimension(Integer.MAX_VALUE, 25));
        
        JLabel lblName = new JLabel(name);
        lblName.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblName.setForeground(UiTheme.TEXT);
        
        JLabel lblScore = new JLabel(score);
        lblScore.setFont(new Font("Monospaced", Font.BOLD, 14));
        lblScore.setForeground(highlight);
        
        row.add(lblName, BorderLayout.WEST);
        row.add(lblScore, BorderLayout.EAST);
        return row;
    }

    private JPanel createFieldWrapper(String title, JComponent inputField) {
        JPanel wrapper = new JPanel(new BorderLayout(0, 8)); 
        wrapper.setOpaque(false);
        wrapper.setBorder(new EmptyBorder(0, 0, 20, 0));
        wrapper.setMaximumSize(new Dimension(Integer.MAX_VALUE, 75));
        
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 12));
        lblTitle.setForeground(UiTheme.MUTED_TEXT);
        
        wrapper.add(lblTitle, BorderLayout.NORTH);
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
        c.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    }
    
    private void stylePrimaryButton(JButton btn) {
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(UiTheme.ON_ACCENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        
        btn.addChangeListener(e -> {
            btn.setBackground(btn.getModel().isPressed() ? UiTheme.ACCENT_HOVER : UiTheme.ACCENT);
        });
        btn.setBackground(UiTheme.ACCENT);
        btn.setOpaque(true);
    }

    private static void addPlaceholderBehavior(JTextField txt, String placeholder) {
        txt.setText(placeholder);
        txt.setForeground(UiTheme.PLACEHOLDER);
        txt.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent e) {
                if (txt.getText().equals(placeholder)) {
                    txt.setText("");
                    txt.setForeground(UiTheme.TEXT);
                }
            }
            public void focusLost(java.awt.event.FocusEvent e) {
                if (txt.getText().trim().isEmpty()) {
                    txt.setText(placeholder);
                    txt.setForeground(UiTheme.PLACEHOLDER);
                }
            }
        });
    }

    public void updateUserData() {
        if (MainFrame.instance != null && MainFrame.instance.getCurrentUser() != null) {
            Users currentUser= MainFrame.instance.getCurrentUser();


            lblWelcome.setText("Welcome, " + currentUser.getUsername().toUpperCase());
            lblTotalScore.setText("Total XP / Score: " + currentUser.getScore());

            refreshLeaderBoard();

        }
    }

    private void refreshLeaderBoard(){
        leaderboardCard.removeAll();

        JLabel lblLeaderboardTitle = new JLabel("🏆 Global Leaderboard (Top 3)");
        lblLeaderboardTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
        lblLeaderboardTitle.setForeground(UiTheme.ACCENT);
        

        leaderboardCard.add(lblLeaderboardTitle);
        leaderboardCard.add(Box.createVerticalStrut(15));
        leaderboardCard.add(createSeparator());
        leaderboardCard.add(Box.createVerticalStrut(15));

        // DB connection vazhiya top scorers-a fetch pandrom
        UserDao userDao = new UserDao();
        List<Users> topScorers = userDao.getTopScorers();
        
        Color[] medalColors = {
            UiTheme.GOLD,
            UiTheme.SILVER,
            UiTheme.BRONZE
        };

        // For loop pottu UI-la rows add pandrom
        for (int i = 0; i < topScorers.size(); i++) {
            Users u = topScorers.get(i);
            String rankName = (i + 1) + ". " + u.getUsername();
            String scoreStr = u.getScore() + " XP";
            
            // Top 3 kku mela ponalum safe ah irukka color handling
            Color c = (i < medalColors.length) ? medalColors[i] : UiTheme.TEXT;
            
            leaderboardCard.add(createLeaderboardRow(rankName, scoreStr, c));
            leaderboardCard.add(Box.createVerticalStrut(10));
        }
        
        leaderboardCard.add(Box.createVerticalGlue());
        
        // UI-a refresh panni puthu data-va kaatta solrom
        leaderboardCard.revalidate();
        leaderboardCard.repaint();
    }
}