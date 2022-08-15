package com.example.instagram.model;

public class PostModel {
    public String email;
    public String comment;
    public String downloadUrl;

    public PostModel(String email, String comment, String downloadUrl) {
        this.email = email;
        this.comment = comment;
        this.downloadUrl = downloadUrl;
    }
}
