package com.exammanagement.model;

public class Users {

    private int userId;
    private String username;
    private String password;
    private String role;
    private int score;

    //Constructors
    public Users (){};

    public Users(int userId, String username, String password, String role, int score){
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.score = score;
    }

    //Getters and Setters
    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public int getScore() { return score; }
    public void setScore(int score) {this.score = score;}
    
}
