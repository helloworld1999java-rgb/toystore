package com.toy.service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.toy.model.Reservation;
import com.toy.model.StockMovement;
import com.toy.model.Toy;
import com.toy.util.ApiClient;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class StockService {
    private final Gson gson = new Gson();

    public List<StockMovement> getAllMovements() {
        try {
            HttpResponse<String> r = ApiClient.sendRequest(
                    "/rest/v1/stock_movements?select=*&order=created_at.desc", "GET", "");
            if (r.statusCode() == 200)
                return gson.fromJson(r.body(), new TypeToken<List<StockMovement>>(){}.getType());
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public boolean createMovement(int toyId, String type, int qty, String from, String to, String note) {
        try {
            String json = gson.toJson(Map.of(
                    "toy_id", toyId,
                    "movement_type", type,
                    "quantity", qty,
                    "from_location", from != null ? from : "",
                    "to_location", to != null ? to : "",
                    "note", note != null ? note : ""
            ));
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/stock_movements", "POST", json);
            if (r.statusCode() == 201) {
                updateStock(toyId, type, qty);
                return true;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    private void updateStock(int toyId, String type, int qty) throws Exception {
        HttpResponse<String> r = ApiClient.sendRequest(
                "/rest/v1/toys?id=eq." + toyId + "&select=stock_qty", "GET", "");
        if (r.statusCode() == 200) {
            List<Map<String, Object>> list = gson.fromJson(r.body(),
                    new TypeToken<List<Map<String, Object>>>(){}.getType());
            if (!list.isEmpty()) {
                int current = ((Double) list.get(0).get("stock_qty")).intValue();
                int newQty;
                if (type.equals("arrival") || type.equals("unreserve")) {
                    newQty = current + qty;
                } else if (type.equals("sale") || type.equals("reserve")) {
                    newQty = Math.max(0, current - qty);
                } else {
                    newQty = current;
                }
                ApiClient.sendRequest("/rest/v1/toys?id=eq." + toyId, "PATCH",
                        gson.toJson(Map.of("stock_qty", newQty)));
            }
        }
    }

    public List<Toy> getLowStockToys() {
        try {
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/toys?select=*", "GET", "");
            if (r.statusCode() == 200) {
                List<Toy> all = gson.fromJson(r.body(), new TypeToken<List<Toy>>(){}.getType());
                List<Toy> low = new ArrayList<>();
                for (Toy t : all) {
                    if (t.stock_qty <= t.min_stock) low.add(t);
                }
                return low;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public boolean reserveToy(int toyId, String customerId, int qty) {
        try {
            String json = gson.toJson(Map.of(
                    "toy_id", toyId,
                    "customer_id", customerId,
                    "quantity", qty,
                    "status", "active"
            ));
            HttpResponse<String> r = ApiClient.sendRequest("/rest/v1/reservations", "POST", json);
            if (r.statusCode() == 201)
                return createMovement(toyId, "reserve", qty, "shop", "reserved", "Бронь клиента " + customerId);
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public List<Reservation> getActiveReservations() {
        try {
            HttpResponse<String> r = ApiClient.sendRequest(
                    "/rest/v1/reservations?status=eq.active&select=*", "GET", "");
            if (r.statusCode() == 200)
                return gson.fromJson(r.body(), new TypeToken<List<Reservation>>(){}.getType());
        } catch (Exception e) { e.printStackTrace(); }
        return new ArrayList<>();
    }

    public boolean cancelReservation(int reservationId, int toyId, int qty) {
        try {
            HttpResponse<String> r = ApiClient.sendRequest(
                    "/rest/v1/reservations?id=eq." + reservationId, "PATCH",
                    gson.toJson(Map.of("status", "cancelled")));
            if (r.statusCode() == 204)
                return createMovement(toyId, "unreserve", qty, "reserved", "shop", "Отмена бронирования");
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }
}
