package com.toy.service;

import com.toy.util.ApiClient;
import java.net.http.HttpResponse;

public class AuthService {

    public String login(String email, String password) throws Exception {
        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password + "\"}";
        HttpResponse<String> r = ApiClient.sendRequest(
                "/auth/v1/token?grant_type=password", "POST", json);
        return r.body();
    }

    // Возвращает полный JSON ответа для анализа
    public String registerRaw(String email, String password, String name) throws Exception {
        String json = "{\"email\":\"" + email + "\",\"password\":\"" + password
                + "\",\"data\":{\"full_name\":\"" + name + "\"}}";
        HttpResponse<String> r = ApiClient.sendRequest("/auth/v1/signup", "POST", json);
        return r.body();
    }

    public boolean register(String email, String password, String name) throws Exception {
        HttpResponse<String> r = ApiClient.sendRequest("/auth/v1/signup", "POST",
                "{\"email\":\"" + email + "\",\"password\":\"" + password
                        + "\",\"data\":{\"full_name\":\"" + name + "\"}}");
        return r.statusCode() == 200 || r.statusCode() == 201;
    }
}
