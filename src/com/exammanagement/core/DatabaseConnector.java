package com.exammanagement.core;

import java.sql.*;


public class DatabaseConnector {

    private static Connection connection = null;

    private static final String URL = "jdbc:mysql://localhost:3306/exam_db";
    private static final String USER = "root";
    private static final String PASSWORD = "jarvis";

    public static Connection getConnection() {
       if(connection == null){

            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL,USER,PASSWORD);
                System.out.print("[SUCCESS] Database Connected Successfully!");

            }catch(ClassNotFoundException | SQLException e){

                System.out.println("[ERROR] Database Connection Failed!");
                e.printStackTrace();

            }
        }
        return connection;
    }

}
