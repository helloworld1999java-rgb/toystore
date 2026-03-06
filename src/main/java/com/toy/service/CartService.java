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
    private final ReceiptService receiptService = new ReceiptService();
    private final StockService stockService = new StockService();

    public boolean placeOrder(List<Toy> items, double totalAmount, String paymentType) throws Exception {
        String orderJson = gson.toJson(Map.of(
                "customer_id", UserSession.getInstance().getUserId(),
                "total_amount", totalAmount,
                "status", "pending",
                "payment_type", paymentType,
                "order_type", "online"
        ));

        HttpResponse<String> orderResp = ApiClient.sendRequest("/rest/v1/orders", "POST", orderJson);

        if (orderResp.statusCode() == 201) {
            Map<String, Object>[] respArray = gson.fromJson(orderResp.body(), Map[].class);
            if (respArray.length > 0) {
                int orderId = ((Double) respArray[0].get("id")).intValue();

                for (Toy item : items) {
                    String itemJson = gson.toJson(Map.of(
                            "order_id", orderId,
                            "toy_id", item.id,
                            "quantity", 1,
                            "price_each", item.price
                    ));
                    ApiClient.sendRequest("/rest/v1/order_items", "POST", itemJson);
                    stockService.createMovement(item.id, "sale", 1, "shop", "sold",
                            "Продажа, заказ #" + orderId);
                }

                receiptService.createReceipt(
                        orderId,
                        UserSession.getInstance().getUserId(),
                        totalAmount, paymentType, items
                );
                return true;
            }
        }
        return false;
    }
}
