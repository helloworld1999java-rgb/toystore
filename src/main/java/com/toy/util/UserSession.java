package com.toy.util;

public class UserSession {
    private static UserSession instance;
    private String userId;
    private String email;
    private String role;
    private String accessToken;

    private UserSession(String userId, String email, String role, String accessToken) {
        this.userId      = userId;
        this.email       = email;
        this.role        = role;
        this.accessToken = accessToken;
    }

    public static void init(String id, String email, String role, String accessToken) {
        instance = new UserSession(id, email, role, accessToken);
    }

    public static UserSession getInstance() { return instance; }
    public static void logout()             { instance = null; }

    public String getUserId()      { return userId; }
    public String getEmail()       { return email; }
    public String getRole()        { return role; }
    public String getAccessToken() { return accessToken; }
    public boolean isAdmin()       { return "admin".equalsIgnoreCase(role); }
}
