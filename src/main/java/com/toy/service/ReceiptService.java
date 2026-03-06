package com.toy.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toy.model.Receipt;
import com.toy.model.Toy;
import com.toy.util.ApiClient;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ReceiptService {
    private final Gson gson = new Gson();

    public boolean createReceipt(int orderId, String customerId, double total, String paymentType, List<Toy> items) {
        try {
            StringBuilder sb = new StringBuilder();
            for (Toy t : items) sb.append(t.name).append(" — ").append(t.price).append(" ₽; ");
            String json = gson.toJson(Map.of(
                    "order_id", orderId,
                    "customer_id", customerId,
                    "total_amount", total,
                    "payment_type", paymentType,
                    "items_json", sb.toString()
            ));
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/receipts", "POST", json);
            return r.statusCode() == 201;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public List<Receipt> getReceiptsByCustomer(String customerId) {
        try {
            HttpResponse<String> r = ApiClient.sendRequest(
                    "/rest/v1/receipts?customer_id=eq." + customerId + "&select=*&order=created_at.desc", "GET", "");
            if (r.statusCode() == 200)
                return gson.fromJson(r.body(), new TypeToken<List<Receipt>>(){}.getType());
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }
}
