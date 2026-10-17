package com.exammanagement.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.exammanagement.core.DatabaseConnector;
import com.exammanagement.model.Users;

public class UserDao {

    //Login
    public Users validateCredentials(String username, String password){

        Users user = null;

        String query = " SELECT * FROM users WHERE username = ? AND password = ?";

        try{
            Connection currentUserCon = DatabaseConnector.getConnection();

            PreparedStatement executeLoginQuery = currentUserCon.prepareStatement(query);

            executeLoginQuery.setString(1, username);

            executeLoginQuery.setString(2, password);

            ResultSet result = executeLoginQuery.executeQuery();

            if(result.next()){

                user = new Users();

                user.setUserId(result.getInt("user_id"));

                user.setUsername(result.getString("username"));

                user.setPassword(result.getString("password"));

                user.setScore(result.getInt("score"));

                System.out.println("[AUTH] Login successful for: " + username);

            }else{
                System.out.println("[AUTH] Invalid credentials attempted.");
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }

    public List<Users> getTopScorers(){
        List<Users> topUsers = new ArrayList<>();

        String query = "SELECT username, score FROM users ORDER BY score DESC LIMIT 3";

        try{
            Connection conn = DatabaseConnector.getConnection();
            PreparedStatement pst = conn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while(rs.next()){
                Users u = new Users();
                u.setUsername(rs.getString("username"));
                u.setScore(rs.getInt("score"));
                topUsers.add(u);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return  topUsers;
    }

    public void addUserScore(int userId, int scoreToAdd){
        String query = "UPDATE users SET score = score + ? WHERE user_id = ?";

        try{
            Connection con = DatabaseConnector.getConnection();
            PreparedStatement pst = con.prepareStatement(query);
            pst.setInt(1, scoreToAdd);
            pst.setInt(2, userId);
            pst.executeUpdate();

            System.out.println("[DB] Score successfully updated for User ID: " + userId);
            
        }catch (Exception e) {
            System.out.println("[ERROR] Failed to update score in DB.");
            e.printStackTrace();
        }
    }
    
}
