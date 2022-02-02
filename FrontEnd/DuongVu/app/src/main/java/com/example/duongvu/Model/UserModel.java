package com.example.duongvu.Model;

public class UserModel {
    private int id;
    private String displayName;
    private int score;
    private String userName;
    private String password;

    public int getId() {
        return id;
    }

    public UserModel() {
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String toString(){
        return "User ID: " + id +
                "\nDisplay name: " + displayName +
                "\nUser's score: " + score +
                "\nUsername: " + userName +
                "\nPassword: " + password;
     }

}