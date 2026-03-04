package com.toy.service;

import com.google.gson.Gson;
import com.toy.model.Toy;
import com.toy.util.ApiClient;
import com.toy.util.UserSession;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

public class CartService {
    private final Gson gson = new Gson();

    public boolean placeOrder(List<Toy> items, double totalAmount) throws Exception {
        // Создание записи в таблице orders
        String orderJson = gson.toJson(Map.of(
                "customer_id", UserSession.getInstance().getUserId(),
                "total_amount", totalAmount,
                "status", "pending"
        ));

        HttpResponse<String> orderResp = ApiClient.sendRequest("/rest/v1/orders", "POST", orderJson);

        if (orderResp.statusCode() == 201) {
            // В идеале здесь нужно получить ID созданного заказа для order_items
            for (Toy item : items) {
                String itemJson = gson.toJson(Map.of(
                        "toy_id", item.id,
                        "quantity", 1,
                        "price_each", item.price
                ));
                ApiClient.sendRequest("/rest/v1/order_items", "POST", itemJson);
            }
            return true;
        }
        return false;
    }
}