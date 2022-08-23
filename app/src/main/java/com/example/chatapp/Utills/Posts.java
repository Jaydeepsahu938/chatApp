package com.example.chatapp.Utills;

public class Posts {
    private String datePost,postDesc,postImageUri,userProfileImageUrl,username;

    public Posts(){

    }
    public Posts(String datePost,String postDesc,String postImageUri, String userProfileImageUrl, String username) {
        this.datePost = datePost;
        this.postImageUri = postImageUri;
        this.userProfileImageUrl = userProfileImageUrl;
        this.postDesc=postDesc;
        this.username = username;
    }

    public String getPostDesc() {
        return postDesc;
    }

    public void setPostDesc(String postDesc) {
        this.postDesc = postDesc;
    }

    public String getDatePost() {
        return datePost;
    }

    public void setDatePost(String datePost) {
        this.datePost = datePost;
    }

    public String getPostImageUri() {
        return postImageUri;
    }

    public void setPostImageUri(String postImageUri) {
        this.postImageUri = postImageUri;
    }

    public String getUserProfileImageUrl() {
        return userProfileImageUrl;
    }

    public void setUserProfileImageUrl(String userProfileImageUrl) {
        this.userProfileImageUrl = userProfileImageUrl;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
