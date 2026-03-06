package com.toy.util;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ApiClient {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> sendRequest(String path, String method, String body) throws Exception {
        // Для авторизованных запросов используем JWT пользователя,
        // иначе — anon ключ
        String token = SupabaseConfig.API_KEY;
        if (UserSession.getInstance() != null
                && UserSession.getInstance().getAccessToken() != null
                && !path.contains("/auth/")) {
            token = UserSession.getInstance().getAccessToken();
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(SupabaseConfig.URL + path))
                .header("apikey", SupabaseConfig.API_KEY)
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json")
                .header("Prefer", "return=representation"); // чтобы POST возвращал созданный объект

        switch (method) {
            case "POST"   -> builder.POST(HttpRequest.BodyPublishers.ofString(body));
            case "PATCH"  -> builder.method("PATCH", HttpRequest.BodyPublishers.ofString(body));
            case "DELETE" -> builder.DELETE();
            default       -> builder.GET();
        }

        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}
