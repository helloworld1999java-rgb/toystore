package com.toy.model;

public class User {
    public int id;
    public String full_name;
    public String phone;
    public String email;
    public String address;
    // Поля для мониторинга из схемы
    public int cart_operations_count;
    public String cart_last_reset_timestamp;
}