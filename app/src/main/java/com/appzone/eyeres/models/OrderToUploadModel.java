package com.appzone.eyeres.models;

import java.io.Serializable;
import java.util.List;

public class OrderToUploadModel implements Serializable {

    private String name;
    private String token;
    private String email;
    private String phone;
    private String address;
    private String notes;
    private double total;
    private double total_after_discount;
    private int payment_method;
    private String coupon_code;
    private double coupon_value;
    private List<ItemCartModel> itemsList;

    public OrderToUploadModel() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public List<ItemCartModel> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<ItemCartModel> itemsList) {
        this.itemsList = itemsList;
    }

    public int getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(int payment_method) {
        this.payment_method = payment_method;
    }

    public double getTotal_after_discount() {
        return total_after_discount;
    }

    public void setTotal_after_discount(double total_after_discount) {
        this.total_after_discount = total_after_discount;
    }

    public String getCoupon_code() {
        return coupon_code;
    }

    public void setCoupon_code(String coupon_code) {
        this.coupon_code = coupon_code;
    }

    public double getCoupon_value() {
        return coupon_value;
    }

    public void setCoupon_value(double coupon_value) {
        this.coupon_value = coupon_value;
    }
}
