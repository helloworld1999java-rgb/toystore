package com.toy.util;

import java.net.URI;
import java.net.http.*;

public class ApiClient {
    private static final HttpClient client = HttpClient.newHttpClient();

    public static HttpResponse<String> sendRequest(String path, String method, String body) throws Exception {
        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(SupabaseConfig.URL + path))
                .header("apikey", SupabaseConfig.API_KEY)
                .header("Authorization", "Bearer " + SupabaseConfig.API_KEY)
                .header("Content-Type", "application/json");

        if (method.equals("POST")) builder.POST(HttpRequest.BodyPublishers.ofString(body));
        else if (method.equals("PATCH")) builder.method("PATCH", HttpRequest.BodyPublishers.ofString(body));
        else builder.GET();

        return client.send(builder.build(), HttpResponse.BodyHandlers.ofString());
    }
}