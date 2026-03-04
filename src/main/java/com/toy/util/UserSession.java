package com.toy.model;

public class UserSession {
    private static UserSession instance;
    public String userId;
    public String email;
    public String role;

    public static void init(String id, String email, String role) {
        instance = new UserSession();
        instance.userId = id;
        instance.email = email;
        instance.role = role;
    }
    public static UserSession getInstance() { return instance; }
    public static void logout() { instance = null; }
}