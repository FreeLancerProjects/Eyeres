package com.appzone.eyeres.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ItemCartModel implements Serializable{

    private int product_id;
    private String product_image;
    private String product_name_ar;
    private String product_name_en;
    private double product_cost;
    private int quantity;
    ///quantity*product_cost
    private double total;
    private int similar;
    @SerializedName("package")
    private String packageSize;
    private String left_degree;
    private String right_degree;
    private int left_amount;
    private int right_amount;


    public ItemCartModel(int product_id, String product_image, String product_name_ar, String product_name_en, double product_cost, int quantity, double total, int similar, String packageSize, String left_degree, String right_degree, int left_amount, int right_amount) {
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_name_ar = product_name_ar;
        this.product_name_en = product_name_en;
        this.product_cost = product_cost;
        this.quantity = quantity;
        this.total = total;
        this.similar = similar;
        this.packageSize = packageSize;
        this.left_degree = left_degree;
        this.right_degree = right_degree;
        this.left_amount = left_amount;
        this.right_amount = right_amount;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getProduct_name_ar() {
        return product_name_ar;
    }

    public void setProduct_name_ar(String product_name_ar) {
        this.product_name_ar = product_name_ar;
    }

    public String getProduct_name_en() {
        return product_name_en;
    }

    public void setProduct_name_en(String product_name_en) {
        this.product_name_en = product_name_en;
    }

    public double getProduct_cost() {
        return product_cost;
    }

    public void setProduct_cost(double product_cost) {
        this.product_cost = product_cost;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getSimilar() {
        return similar;
    }

    public void setSimilar(int similar) {
        this.similar = similar;
    }

    public String getPackageSize() {
        return packageSize;
    }

    public void setPackageSize(String packageSize) {
        this.packageSize = packageSize;
    }

    public String getLeft_degree() {
        return left_degree;
    }

    public void setLeft_degree(String left_degree) {
        this.left_degree = left_degree;
    }

    public String getRight_degree() {
        return right_degree;
    }

    public void setRight_degree(String right_degree) {
        this.right_degree = right_degree;
    }

    public int getLeft_amount() {
        return left_amount;
    }

    public void setLeft_amount(int left_amount) {
        this.left_amount = left_amount;
    }

    public int getRight_amount() {
        return right_amount;
    }

    public void setRight_amount(int right_amount) {
        this.right_amount = right_amount;
    }
}
