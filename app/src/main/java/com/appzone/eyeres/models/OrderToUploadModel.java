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
    private List<ItemCartModel> itemCartModelList;

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

    public List<ItemCartModel> getItemCartModelList() {
        return itemCartModelList;
    }

    public void setItemCartModelList(List<ItemCartModel> itemCartModelList) {
        this.itemCartModelList = itemCartModelList;
    }
}
