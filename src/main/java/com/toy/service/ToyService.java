package com.toy.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toy.model.Toy;
import com.toy.util.ApiClient;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class ToyService {
    private final Gson gson = new Gson();

    /**
     * Получает все игрушки из таблицы toys
     */
    public List<Toy> getAllToys() {
        try {
            // Запрос к PostgREST: выбираем все колонки
            HttpResponse<String> response = ApiClient.sendRequest("/rest/v1/toys?select=*", "GET", "");

            if (response.statusCode() == 200) {
                // Превращаем JSON-массив в List<Toy>
                return gson.fromJson(response.body(), new TypeToken<List<Toy>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Поиск игрушек по названию (используем фильтр ilike для поиска без учета регистра)
     */
    public List<Toy> searchToysByName(String name) {
        try {
            // Формируем путь с фильтром: name.ilike.*текст*
            String path = "/rest/v1/toys?name=ilike.*" + name.replace(" ", "%20") + "*&select=*";
            HttpResponse<String> response = ApiClient.sendRequest(path, "GET", "");

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<Toy>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * Получение игрушек по категории
     */
    public List<Toy> getToysByCategory(int categoryId) {
        try {
            String path = "/rest/v1/toys?category_id=eq." + categoryId + "&select=*";
            HttpResponse<String> response = ApiClient.sendRequest(path, "GET", "");

            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<Toy>>(){}.getType());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }
}