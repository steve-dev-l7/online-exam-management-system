package com.exammanagement.ui;

import com.exammanagement.ai.Ollama;
import com.exammanagement.model.Questions;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ResultPanel extends JPanel {

    // Left Panel Variables (Review)
    private List<Integer> wrongQuestionIndices;
    private List<Questions> allQuestions;
    private HashMap<Integer, String> userAnswers;
    private HashMap<Integer, String> correctAnswers;
    private int currentReviewIndex = 0;

    private JLabel lblScore;
    private JTextArea txtQuestionReview;
    private JLabel lblYourAnswer;
    private JLabel lblCorrectAnswer;
    private JTextArea txtAiExplanation;
    private JButton btnNextReview;
    JLabel lblChatTitle;

    // Right Panel Variables (Chat)
    private JTextArea txtChatHistory;
    private JTextField txtChatInput;
    private JButton btnSendChat;

    public ResultPanel() {
        setBackground(UiTheme.BACKGROUND);
        setLayout(new BorderLayout(20, 20));
        setBorder(new EmptyBorder(20, 30, 20, 30));

        // Top Header
        lblScore = new JLabel("Exam Completed! Analyzing Results...", JLabel.CENTER);
        lblScore.setFont(new Font("SansSerif", Font.BOLD, 24));
        lblScore.setForeground(UiTheme.TEXT);
        add(lblScore, BorderLayout.NORTH);

        // Main Split Screen
        JPanel splitPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        splitPanel.setOpaque(false);

        // ==========================================
        // LEFT COLUMN: Mistake Review & Explanation
        // ==========================================
        JPanel leftCard = createDarkCard();
        
        JLabel lblReviewTitle = new JLabel("🔍 AI Mistake Analysis");
        lblReviewTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblReviewTitle.setForeground(UiTheme.ACCENT);
        lblReviewTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        txtQuestionReview = createTextArea(16, UiTheme.TEXT);
        // FIX 1: Question box thevai illama full screen viriyama irukka Max Height limit pandrom
        txtQuestionReview.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        
        lblYourAnswer = new JLabel("Your Answer: ");
        lblYourAnswer.setForeground(UiTheme.ERROR);
        lblYourAnswer.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblYourAnswer.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        lblCorrectAnswer = new JLabel("Correct Answer: ");
        lblCorrectAnswer.setForeground(UiTheme.SUCCESS);
        lblCorrectAnswer.setFont(new Font("SansSerif", Font.BOLD, 14));
        lblCorrectAnswer.setAlignmentX(Component.LEFT_ALIGNMENT); 

        JLabel lblExpTitle = new JLabel("Ollama Explanation:");
        lblExpTitle.setForeground(UiTheme.MUTED_TEXT);
        lblExpTitle.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        txtAiExplanation = createTextArea(14, UiTheme.TEXT);
        JScrollPane expScroll = new JScrollPane(txtAiExplanation);
        expScroll.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        expScroll.setAlignmentX(Component.LEFT_ALIGNMENT); 
        
        
        expScroll.setPreferredSize(new Dimension(400, 200));
        expScroll.setMaximumSize(new Dimension(Integer.MAX_VALUE, 250));

        btnNextReview = createStyledButton("Next Wrong Answer ➡", UiTheme.ACCENT);
        btnNextReview.setAlignmentX(Component.LEFT_ALIGNMENT); 
        btnNextReview.addActionListener(e -> handleReviewButton());

        leftCard.add(lblReviewTitle);
        leftCard.add(Box.createVerticalStrut(15));
        leftCard.add(txtQuestionReview);
        leftCard.add(Box.createVerticalStrut(15));
        leftCard.add(lblYourAnswer);
        leftCard.add(Box.createVerticalStrut(5));
        leftCard.add(lblCorrectAnswer);
        leftCard.add(Box.createVerticalStrut(20));
        leftCard.add(lblExpTitle);
        leftCard.add(Box.createVerticalStrut(5));
        leftCard.add(expScroll); 
        
        // FIX 3: Intha Glue add pannathala, extra space iruntha athu empty-a irukkum, components-a stretch pannathu
        leftCard.add(Box.createVerticalGlue()); 
        
        leftCard.add(Box.createVerticalStrut(15));
        leftCard.add(btnNextReview);

        // ==========================================
        // RIGHT COLUMN: Personal AI Tutor Chat
        // ==========================================
        JPanel rightCard = createDarkCard();
        
        lblChatTitle = new JLabel("💬 Ask Anything, Student");
        lblChatTitle.setFont(new Font("SansSerif", Font.BOLD, 18));
        lblChatTitle.setForeground(UiTheme.ACCENT);
        lblChatTitle.setAlignmentX(Component.LEFT_ALIGNMENT); // FIX: Left align

        txtChatHistory = createTextArea(14, UiTheme.TEXT);
        txtChatHistory.setText("Tutor: Hello! I'm your AI assistant. Do you have any doubts about the exam topics?\n");
        
        JScrollPane scrollChat = new JScrollPane(txtChatHistory);
        scrollChat.setBorder(BorderFactory.createLineBorder(UiTheme.BORDER));
        scrollChat.setAlignmentX(Component.LEFT_ALIGNMENT); // FIX: Left align

        JPanel chatInputPanel = new JPanel(new BorderLayout(10, 0));
        chatInputPanel.setOpaque(false);
        // FIX: The Magic line that stops the button from growing infinitely
        chatInputPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40)); 
        chatInputPanel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        txtChatInput = new JTextField();
        txtChatInput.setBackground(UiTheme.INPUT_BACKGROUND);
        txtChatInput.setForeground(UiTheme.TEXT);
        txtChatInput.setCaretColor(UiTheme.TEXT);
        txtChatInput.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtChatInput.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(UiTheme.BORDER),
            new EmptyBorder(5, 10, 5, 10)));
            
        btnSendChat = createStyledButton("Send", UiTheme.SUCCESS);
        btnSendChat.setPreferredSize(new Dimension(80, 40)); // Fixed size for send button

        chatInputPanel.add(txtChatInput, BorderLayout.CENTER);
        chatInputPanel.add(btnSendChat, BorderLayout.EAST);

        rightCard.add(lblChatTitle);
        rightCard.add(Box.createVerticalStrut(15));
        rightCard.add(scrollChat);
        rightCard.add(Box.createVerticalStrut(15));
        rightCard.add(chatInputPanel);

        splitPanel.add(leftCard);
        splitPanel.add(rightCard);
        
        add(splitPanel, BorderLayout.CENTER);

        // Chat Action
        btnSendChat.addActionListener(e -> sendChatMessage());
        txtChatInput.addActionListener(e -> sendChatMessage()); 
    }

    // --- DATA LOADING & LOGIC ---

    public void loadResultData(int score, int total, List<Questions> questions, HashMap<Integer, String> uAns, HashMap<Integer, String> cAns) {
        this.allQuestions = questions;
        this.userAnswers = uAns;
        this.correctAnswers = cAns;
        this.wrongQuestionIndices = new ArrayList<>();
        this.currentReviewIndex = 0;

        lblScore.setText("Exam Completed! Your Score: " + score + " / " + total);
        lblYourAnswer.setVisible(true);
        lblCorrectAnswer.setVisible(true);
        lblChatTitle.setText("💬 Ask Anything, "+getUsernameSafe());
        txtAiExplanation.setVisible(true);
        btnNextReview.setText("Next Wrong Answer ➡");

        for (int i = 0; i < total; i++) {
            String u = userAnswers.get(i);
            String c = correctAnswers.get(i);
            if (u == null || c == null || !u.trim().equalsIgnoreCase(c.trim())) {
                wrongQuestionIndices.add(i);
            }
        }

        if (wrongQuestionIndices.isEmpty()) {
            txtQuestionReview.setText("Perfect Score! You have no mistakes to review.");
            lblYourAnswer.setVisible(false);
            lblCorrectAnswer.setVisible(false);
            txtAiExplanation.setVisible(false);
            btnNextReview.setText("Back to Dashboard");
            btnNextReview.setEnabled(true);
        } else {
            loadWrongAnswer(0);
        }
    }

    private void loadWrongAnswer(int index) {
        if (index >= wrongQuestionIndices.size()) {
            txtQuestionReview.setText("You have reviewed all your mistakes.");
            lblYourAnswer.setText("");
            lblCorrectAnswer.setText("");
            txtAiExplanation.setText("");
            btnNextReview.setText("Back to Dashboard");
            btnNextReview.setEnabled(true);
            
            return;
        }

        int qIndex = wrongQuestionIndices.get(index);
        Questions q = allQuestions.get(qIndex);
        String uAns = userAnswers.getOrDefault(qIndex, "Not Answered");
        String cAns = correctAnswers.get(qIndex);

        txtQuestionReview.setText("Q: " + q.getQuestionText());
        lblYourAnswer.setText("Your Answer: " + uAns);
        lblCorrectAnswer.setText("Correct Answer: " + cAns);
        
        txtAiExplanation.setText("Generating explanation via Ollama... Please wait ⏳");
        btnNextReview.setEnabled(false);

        SwingWorker<String, Void> expWorker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return Ollama.getAnswerExplanation(q.getQuestionText(), uAns, cAns);
            }
            @Override
            protected void done() {
                try {
                    txtAiExplanation.setText(get());
                    btnNextReview.setEnabled(true);
                } catch (Exception e) {
                    txtAiExplanation.setText("Error fetching explanation.");
                    btnNextReview.setEnabled(true);
                }
            }
        };
        expWorker.execute();
    }

    private void showNextWrongAnswer() {
        currentReviewIndex++;
        loadWrongAnswer(currentReviewIndex);
    }

    private void handleReviewButton() {
        if (currentReviewIndex < wrongQuestionIndices.size()) {
            showNextWrongAnswer();
            return;
        }

        DashboardPanel.resetButton();
        MainFrame.instance.switchScreen("DASHBOARD");
    }

    private void sendChatMessage() {
        String query = txtChatInput.getText().trim();
        if (query.isEmpty()) return;

        txtChatHistory.append("\nYou: " + query + "\n");
        txtChatInput.setText("");
        txtChatInput.setEnabled(false);
        btnSendChat.setEnabled(false);

        SwingWorker<String, Void> chatWorker = new SwingWorker<>() {
            @Override
            protected String doInBackground() {
                return Ollama.getGeneralChatResponse(query);
            }
            @Override
            protected void done() {
                try {
                    txtChatHistory.append("Tutor: " + get() + "\n");
                } catch (Exception e) {
                    txtChatHistory.append("Tutor: Error connecting to AI.\n");
                }
                txtChatInput.setEnabled(true);
                btnSendChat.setEnabled(true);
                txtChatInput.requestFocus();
            }
        };
        chatWorker.execute();
    }

    // --- UI HELPERS ---

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

    private JTextArea createTextArea(int fontSize, Color fgColor) {
        JTextArea txt = new JTextArea();
        txt.setFont(new Font("SansSerif", Font.PLAIN, fontSize));
        txt.setForeground(fgColor);
        txt.setBackground(UiTheme.INPUT_BACKGROUND);
        txt.setOpaque(true);
        txt.setEditable(false);
        txt.setLineWrap(true);
        txt.setWrapStyleWord(true);
        txt.setAlignmentX(Component.LEFT_ALIGNMENT); // FIX: Left align
        return txt;
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton btn = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getModel().isPressed() ? bgColor.darker() : bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 6, 6);
                super.paintComponent(g2);
                g2.dispose();
            }
        };
        btn.setContentAreaFilled(false);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setFont(new Font("SansSerif", Font.BOLD, 13));
        btn.setForeground(UiTheme.ON_ACCENT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return btn;
    }

    private String getUsernameSafe() {
        if (MainFrame.instance != null && MainFrame.instance.getCurrentUser() != null) {
            return MainFrame.instance.getCurrentUser().getUsername();
        }
        return "Student";
    }
}