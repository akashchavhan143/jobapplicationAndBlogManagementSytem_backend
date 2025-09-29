package com.example.jobtracker.dto;

public class JobPortalAccountDto {

    private String username;
    private String password;  // raw password from user input
    private String link;

    // Getter and Setter for username
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    // Getter and Setter for password
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    // Getter and Setter for link
    public String getLink() {
        return link;
    }
    public void setLink(String link) {
        this.link = link;
    }
}
