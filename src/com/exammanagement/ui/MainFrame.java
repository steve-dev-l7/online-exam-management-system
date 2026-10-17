package com.exammanagement.ui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;
import java.util.HashMap;
import java.util.List;

import com.exammanagement.model.Questions;
import com.exammanagement.model.Users;

public class MainFrame extends JFrame{

    public static MainFrame instance;

    private CardLayout cardLayout;

    private JPanel mainContainer;

    private Users currentUser;

    private DashboardPanel dashboardPanel;

    private LoginPanel loginPanel;

    private ExamPanel examPanel;

    private ResultPanel resultPanel;

    public MainFrame(){

        instance = this;

        UiTheme.apply();
        setTitle("AI Exam Management System");
        getContentPane().setBackground(UiTheme.BACKGROUND);

        setMinimumSize(new Dimension(1024, 768));

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);

        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(UiTheme.BACKGROUND);

        loginPanel = new LoginPanel();
        dashboardPanel = new DashboardPanel();
        examPanel = new ExamPanel();
        

        mainContainer.add(loginPanel, "LOGIN");
        mainContainer.add(dashboardPanel, "DASHBOARD");
        mainContainer.add(examPanel, "EXAM");
        

        add(mainContainer);
        cardLayout.show(mainContainer, "LOGIN");

    }

    public void switchScreen(String screenName) {
        // User data update for Dashboard
        if ("DASHBOARD".equals(screenName)) {
            if (dashboardPanel != null) {
                dashboardPanel.updateUserData();
            }
        }

        
        if ("EXAM".equalsIgnoreCase(screenName)) {
            
            if (!isUndecorated()) {
                dispose(); 
                setUndecorated(true); 
                setExtendedState(JFrame.MAXIMIZED_BOTH); 
                setVisible(true); 
            }
        } else {
            
            if (isUndecorated()) {
                dispose(); 
                setUndecorated(false);
                setExtendedState(JFrame.NORMAL);
                setSize(1024, 768);
                setLocationRelativeTo(null);
                setVisible(true);
            }
        }
        
        // Final ah antha specific panel-a kaatturom
        cardLayout.show(mainContainer, screenName);
        revalidate();
        repaint();
    }
    public void setCurrentUser(Users user) {
        this.currentUser = user;
    }

    public Users getCurrentUser() {
        return currentUser;
    }

    public void startExamSession(List<Questions> questionList){

        examPanel.loadQuestions(questionList);

        switchScreen("EXAM");

    }

    public void showResultScreen(int score, int total, List<Questions> questions, HashMap<Integer, String> uAns, HashMap<Integer, String> cAns) {
        if(resultPanel != null) mainContainer.remove(resultPanel);
        resultPanel = new ResultPanel();
        mainContainer.add(resultPanel,"RESULT");

        // Disable Kiosk mode and return to normal window state
        if (isUndecorated()) {
            dispose(); 
            setUndecorated(false);
            setExtendedState(JFrame.NORMAL);
            setSize(1200, 800); // Make it slightly wider for the split screen
            setLocationRelativeTo(null);
            setVisible(true);
        }

        // Pass data to result panel
        resultPanel.loadResultData(score, total, questions, uAns, cAns);
        
        // Switch screen
        cardLayout.show(mainContainer, "RESULT");
    }
    
}
