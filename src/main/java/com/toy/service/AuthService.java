package com.toy.service;

import com.toy.util.ApiClient;
import java.net.http.HttpResponse;

public class AuthService {
    public String login(String email, String password) throws Exception {
        String json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\"}";
        HttpResponse<String> response = ApiClient.sendRequest("/auth/v1/token?grant_type=password", "POST", json);
        return response.body();
    }

    public boolean register(String email, String password, String name) throws Exception {
        String json = "{\"email\":\"" + email + "\", \"password\":\"" + password + "\", \"data\":{\"full_name\":\"" + name + "\"}}";
        HttpResponse<String> response = ApiClient.sendRequest("/auth/v1/signup", "POST", json);
        return response.statusCode() == 200;
    }
}