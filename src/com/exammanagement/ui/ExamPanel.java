package com.exammanagement.ui;

import javax.swing.*;

import com.exammanagement.dao.UserDao;
import com.exammanagement.model.Questions;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.List;
import javax.swing.border.EmptyBorder;


public class ExamPanel extends JPanel {
    
    private List<Questions> questionList;
    private HashMap<Integer, String> secretAnswerKey;
    private HashMap<Integer, String> userAnswer;
    private int currentIndex = 0;

    private Timer examTimer;
    private int timeLeft = 600;
    private boolean examActive = false;
    private boolean isSecurityTriggered = false;

    private JLabel lblTimer;
    private JLabel lblQuestionNo;
    private JTextArea txtQuestion;
    private JRadioButton rbA, rbB, rbC, rbD; 
    private ButtonGroup optionGroup;
    private JButton btnPrev, btnNext, btnSubmit;
    private JPanel mainCard;
    private WindowAdapter antiCheatListener;

    public ExamPanel(){
        setBackground(UiTheme.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(30, 50, 30, 50));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        JLabel lblTitle = new JLabel("⚡ AI Exam Execution Mode", JLabel.LEFT);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 22));
        lblTitle.setForeground(UiTheme.ACCENT);

        lblTimer = new JLabel("Time Left: 10:00", JLabel.RIGHT);
        lblTimer.setFont(new Font("Monospaced", Font.BOLD, 22));
        lblTimer.setForeground(UiTheme.ERROR);

        headerPanel.add(lblTitle, BorderLayout.WEST);
        headerPanel.add(lblTimer, BorderLayout.EAST);

        mainCard = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(UiTheme.SURFACE);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);
                g2.setColor(UiTheme.BORDER);
                g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 15, 15);
                g2.dispose();
            }
        };
        mainCard.setOpaque(false);
        mainCard.setLayout(new BorderLayout());
        mainCard.setBorder(new EmptyBorder(30, 40, 30, 40));

        JPanel contentPanel = new JPanel();
        contentPanel.setLayout(new BoxLayout(contentPanel, BoxLayout.Y_AXIS));
        contentPanel.setOpaque(false);

        JPanel pnlTitle = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        pnlTitle.setOpaque(false);
        lblQuestionNo = new JLabel("Question 1 of 5");
        lblQuestionNo.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblQuestionNo.setForeground(UiTheme.MUTED_TEXT);
        pnlTitle.add(lblQuestionNo);

        // Question Row
        JPanel pnlQuestion = new JPanel(new BorderLayout());
        pnlQuestion.setOpaque(false);
        txtQuestion = new JTextArea("Loading question...");
        txtQuestion.setFont(new Font("SansSerif", Font.PLAIN, 18));
        txtQuestion.setForeground(UiTheme.TEXT);
        txtQuestion.setOpaque(false);
        txtQuestion.setEditable(false);
        txtQuestion.setLineWrap(true);
        txtQuestion.setWrapStyleWord(true);
        pnlQuestion.add(txtQuestion, BorderLayout.CENTER);

        // Options Setup
        JPanel pnlOptions = new JPanel(new GridLayout(4, 1, 0, 15));
        pnlOptions.setOpaque(false);
        optionGroup = new ButtonGroup();
        rbA = createFuturisticRadioButton("Option A");
        rbB = createFuturisticRadioButton("Option B");
        rbC = createFuturisticRadioButton("Option C");
        rbD = createFuturisticRadioButton("Option D");

        optionGroup.add(rbA); optionGroup.add(rbB);
        optionGroup.add(rbC); optionGroup.add(rbD);
        pnlOptions.add(rbA); pnlOptions.add(rbB);
        pnlOptions.add(rbC); pnlOptions.add(rbD);

        contentPanel.add(pnlTitle);
        contentPanel.add(Box.createVerticalStrut(15));
        contentPanel.add(pnlQuestion);
        contentPanel.add(Box.createVerticalStrut(30));
        contentPanel.add(pnlOptions);

        // Add content to top of the main card
        mainCard.add(contentPanel, BorderLayout.NORTH);

        // 3. Footer (Navigation Buttons)
        JPanel footerPanel = new JPanel(new BorderLayout());
        footerPanel.setOpaque(false);

        btnPrev = createNavButton("⬅ Previous", UiTheme.ACCENT_SOFT, UiTheme.TEXT);
        btnNext = createNavButton("Next ➡", UiTheme.ACCENT, UiTheme.ON_ACCENT);
        btnSubmit = createNavButton("🛑 Submit Exam", UiTheme.ERROR, UiTheme.ON_ACCENT);

        JPanel leftNav = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftNav.setOpaque(false);
        leftNav.add(btnPrev);
        leftNav.add(btnNext);

        footerPanel.add(leftNav, BorderLayout.WEST);
        footerPanel.add(btnSubmit, BorderLayout.EAST);

        add(headerPanel, BorderLayout.NORTH);
        add(mainCard, BorderLayout.CENTER);
        add(footerPanel, BorderLayout.SOUTH);

        // Action Listeners
        btnNext.addActionListener(e -> navigateQuestion(1));
        btnPrev.addActionListener(e -> navigateQuestion(-1));
        btnSubmit.addActionListener(e -> submitExam(false));
    }

    public void loadQuestions(List<Questions> aiQuestions){
        this.isSecurityTriggered = false;
        if (antiCheatListener != null) {    // old listener irundha remove
            MainFrame.instance.removeWindowFocusListener(antiCheatListener);
        }
        lblTimer.setText(String.format("Time Left: %02d:00", aiQuestions.size()));
        this.questionList = aiQuestions;
        this.secretAnswerKey = new HashMap<>();
        this.userAnswer = new HashMap<>();
        this.currentIndex = 0;
        this.timeLeft = aiQuestions.size() * 60;
        this.examActive = true;

        for(int i = 0; i < aiQuestions.size(); i++){
            secretAnswerKey.put(i,aiQuestions.get(i).getCorrectAnswer());
        }

        displayQuestion(0);
        startTimer();

        Timer delaySetUp = new Timer(2000, e ->{setupAntiCheat();});
        delaySetUp.setRepeats(false);
        delaySetUp.start();
    
    }

    private void displayQuestion(int index){
        if (index < 0 || index >= questionList.size()) return;

        Questions q = questionList.get(index);
        lblQuestionNo.setText("Question "+(index + 1)+ " of "+ questionList.size());
        txtQuestion.setText(q.getQuestionText());

        rbA.setText(q.getOptA());
        rbB.setText(q.getOptB());
        rbC.setText(q.getOptC());
        rbD.setText(q.getOptD());

        optionGroup.clearSelection();

        if(userAnswer.containsKey(index)){
            String ans = userAnswer.get(index);
            if (ans.equals(q.getOptA())) rbA.setSelected(true);
            else if (ans.equals(q.getOptB())) rbB.setSelected(true);
            else if (ans.equals(q.getOptC())) rbC.setSelected(true);
            else if (ans.equals(q.getOptD())) rbD.setSelected(true);
        }

        btnPrev.setEnabled(index > 0);
        btnNext.setEnabled(index < questionList.size() -1);
    }

    private void saveCurrentAnswer(){
        if (rbA.isSelected()) userAnswer.put(currentIndex, rbA.getText());
        else if (rbB.isSelected()) userAnswer.put(currentIndex, rbB.getText());
        else if (rbC.isSelected()) userAnswer.put(currentIndex, rbC.getText());
        else if (rbD.isSelected()) userAnswer.put(currentIndex, rbD.getText());
    }

    private void navigateQuestion(int direction){
        saveCurrentAnswer();
        currentIndex += direction;
        displayQuestion(currentIndex);
    }

    private void startTimer(){
        if (examTimer != null) examTimer.stop();

        examTimer = new Timer(1000, e->{

            if(timeLeft > 0){
                timeLeft --;
                int minutes = timeLeft / 60;
                int seconds = timeLeft % 60;
                lblTimer.setText(String.format("Time Left: %02d:%02d", minutes, seconds));
            }else{
                examTimer.stop();
                JOptionPane.showMessageDialog(this, "Time is up! Auto-submitting exam.", "Timeout", JOptionPane.WARNING_MESSAGE);
                submitExam(true);
            }

        });
        examTimer.start();
    }

    private void submitExam(boolean isForcedSubmit){
        if (!examActive) return;

        examActive = false;
        if(examTimer != null) examTimer.stop();
        saveCurrentAnswer();

        if (antiCheatListener != null) {
            MainFrame.instance.removeWindowListener(antiCheatListener);
            MainFrame.instance.removeWindowFocusListener(antiCheatListener);
            MainFrame.instance.removeWindowStateListener(antiCheatListener);
        }

        int score = 0;
        for(int i = 0; i < questionList.size(); i++){
            String correct = secretAnswerKey.get(i);
            String userClicked = userAnswer.get(i);

            if(correct != null && userClicked != null ){

                if(correct.trim().equalsIgnoreCase(userClicked.trim())){
                    score++;
                }

            }
        }

        int total = questionList.size();

        System.out.println("[EXAM RESULT] User scored: " + score + " / " + total);

        if (!isForcedSubmit) {
            JOptionPane.showMessageDialog(this, "Exam Submitted Successfully!\nYour Score: " + score + " / " + total, "Completion", JOptionPane.INFORMATION_MESSAGE);

            if(MainFrame.instance.getCurrentUser()!=null && score > 0){
                int userId = MainFrame.instance.getCurrentUser().getUserId();

                UserDao userDao = new UserDao();
                userDao.addUserScore(userId, score);

                int currentXp = MainFrame.instance.getCurrentUser().getScore();
                MainFrame.instance.getCurrentUser().setScore(currentXp + score);

                MainFrame.instance.showResultScreen(score, total, questionList, userAnswer, secretAnswerKey);
            }
        }else{
            DashboardPanel.resetButton();
            MainFrame.instance.switchScreen("DASHBOARD");
        }

        examActive = true;
    }


    private void setupAntiCheat() {
        antiCheatListener = new WindowAdapter() {

            @Override
            public void windowDeactivated(WindowEvent e) {
                triggerSecurityViolation();
            }

            @Override
            public void windowIconified(WindowEvent e) {
                triggerSecurityViolation();
            }

            @Override
            public void windowLostFocus(WindowEvent e) {
                triggerSecurityViolation();
            }
        };

        MainFrame.instance.addWindowListener(antiCheatListener);
        MainFrame.instance.addWindowFocusListener(antiCheatListener);
        MainFrame.instance.addWindowStateListener(antiCheatListener);
    }

    private void triggerSecurityViolation() {
        if (!examActive || isSecurityTriggered) return;
        isSecurityTriggered = true;

        // Auto-submit timer FIRST (dialog blocking so munnadiye start pannanum)
        Timer delay = new Timer(2000, de -> submitExam(true));
        delay.setRepeats(false);
        delay.start();

        SwingUtilities.invokeLater(() ->
        JOptionPane.showMessageDialog(MainFrame.instance,"🚨 WARNING: Focus lost or Alt-Tab detected!\nYour exam is being AUTO-SUBMITTED for security reasons.","Security Violation", JOptionPane.ERROR_MESSAGE)
        );
    }

    private JRadioButton createFuturisticRadioButton(String text) {
        JRadioButton rb = new JRadioButton(text);
        rb.setFont(new Font("SansSerif", Font.PLAIN, 16));
        rb.setForeground(UiTheme.TEXT);
        rb.setBackground(UiTheme.SURFACE);
        rb.setFocusPainted(false);
        rb.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return rb;
    }

    private JButton createNavButton(String text, Color bgColor, Color fgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bgColor.darker() : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 8, 8);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 14));
        btn.setForeground(fgColor);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(140, 40));
        return btn;
    }

}
