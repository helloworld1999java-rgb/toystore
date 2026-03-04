package com.toy.util;

public class UserSession {
    private static UserSession instance;
    private String userId;
    private String email;
    private String role;

    private UserSession(String userId, String email, String role) {
        this.userId = userId;
        this.email = email;
        this.role = role;
    }

    public static void init(String id, String email, String role) {
        instance = new UserSession(id, email, role);
    }

    public static UserSession getInstance() { return instance; }
    public static void logout() { instance = null; }

    public String getUserId() { return userId; }
    public String getEmail() { return email; }
}