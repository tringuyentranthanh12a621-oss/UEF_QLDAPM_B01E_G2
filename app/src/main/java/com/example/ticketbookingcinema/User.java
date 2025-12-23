package com.example.ticketbookingcinema;

public class User {
    private String fullName;
    private String email;
    private String role; // "admin" hoặc "client"

    // 1. Constructor rỗng (BẮT BUỘC cho Firestore)
    public User() {
    }

    // 2. Constructor đầy đủ
    public User(String fullName, String email, String role) {
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getRole() { return role; }
}