package com.toy.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toy.model.Toy;
import com.toy.util.ApiClient;

import java.net.URLEncoder;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ToyService {
    private final Gson gson = new Gson();

    public List<Toy> getAllToys() {
        try {
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/toys?select=*", "GET", "");
            if (r.statusCode() == 200)
                return gson.fromJson(r.body(), new TypeToken<List<Toy>>(){}.getType());
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public List<Toy> searchToysByName(String name) {
        try {
            if (name == null || name.isEmpty()) return getAllToys();
            String enc = URLEncoder.encode("*" + name + "*", "UTF-8");
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/toys?name=ilike." + enc + "&select=*", "GET", "");
            if (r.statusCode() == 200)
                return gson.fromJson(r.body(), new TypeToken<List<Toy>>(){}.getType());
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public List<Toy> getToysByCategory(int categoryId) {
        try {
            if (categoryId == 0) return getAllToys();
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/toys?category_id=eq." + categoryId + "&select=*", "GET", "");
            if (r.statusCode() == 200)
                return gson.fromJson(r.body(), new TypeToken<List<Toy>>(){}.getType());
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public boolean addToy(Toy toy) {
        try {
            String json = gson.toJson(Map.of(
                    "name", toy.name,
                    "category_id", toy.category_id != null ? toy.category_id : 0,
                    "price", toy.price,
                    "stock_qty", toy.stock_qty,
                    "min_stock", toy.min_stock,
                    "description", toy.description != null ? toy.description : "",
                    "color", toy.color != null ? toy.color : "",
                    "material", toy.material != null ? toy.material : "",
                    "size", toy.size != null ? toy.size : "",
                    "age_from", toy.age_from
            ));
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/toys", "POST", json);
            return r.statusCode() == 201;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean updateToy(Toy toy) {
        try {
            String json = gson.toJson(Map.of(
                    "name", toy.name,
                    "price", toy.price,
                    "stock_qty", toy.stock_qty,
                    "min_stock", toy.min_stock,
                    "description", toy.description != null ? toy.description : "",
                    "color", toy.color != null ? toy.color : "",
                    "material", toy.material != null ? toy.material : "",
                    "size", toy.size != null ? toy.size : ""
            ));
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/toys?id=eq." + toy.id, "PATCH", json);
            return r.statusCode() == 204;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean deleteToy(int toyId) {
        try {
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/toys?id=eq." + toyId, "DELETE", "");
            return r.statusCode() == 204;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public int getCategoryIdByName(String name) {
        return switch (name) {
            case "Конструкторы" -> 1;
            case "Куклы" -> 2;
            case "Машинки" -> 3;
            case "Настольные игры" -> 4;
            default -> 0;
        };
    }
}
