package com.toy.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toy.model.Order;
import com.toy.util.ApiClient;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final Gson gson = new Gson();

    public List<Order> getCustomerOrders(String customerId) {
        try {
            String path = "/rest/v1/orders?customer_id=eq." + customerId + "&select=*";
            HttpResponse<String> response = ApiClient.sendRequest(path, "GET", "");
            if (response.statusCode() == 200) {
                return gson.fromJson(response.body(), new TypeToken<List<Order>>(){}.getType());
            }
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public boolean updateOrderStatus(int orderId, String status) {
        try {
            String json = gson.toJson(Map.of("status", status));
            HttpResponse<String> response = ApiClient.sendRequest("/rest/v1/orders?id=eq." + orderId, "PATCH", json);
            return response.statusCode() == 204;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean setDeliveryInfo(int orderId, String trackingNumber, String deliveryDate) {
        try {
            String json = gson.toJson(Map.of(
                    "tracking_number", trackingNumber,
                    "delivery_date", deliveryDate
            ));
            HttpResponse<String> response = ApiClient.sendRequest("/rest/v1/orders?id=eq." + orderId, "PATCH", json);
            return response.statusCode() == 204;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
