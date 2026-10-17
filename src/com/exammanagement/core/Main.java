package com.exammanagement.core;

import javax.swing.*;

import com.exammanagement.ui.MainFrame;

public class Main {

    private static final String TAG = "MainClass";

    public static void main(String[] args){
        System.out.println("[" + TAG + "] Initializing ");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                MainFrame mainFrame = new MainFrame();
                mainFrame.setVisible(true);
            }
        });

    }
    
}
