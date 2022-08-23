package com.example.chatapp.Utills;

public class Comment {
    private String username,profileImageUrL,comment;

    public Comment() {
    }

    public Comment(String username, String profileImageUrL, String comment) {
        this.username = username;
        this.profileImageUrL = profileImageUrL;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getProfileImageUrL() {
        return profileImageUrL;
    }

    public void setProfileImageUrL(String profileImageUrL) {
        this.profileImageUrL = profileImageUrL;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
