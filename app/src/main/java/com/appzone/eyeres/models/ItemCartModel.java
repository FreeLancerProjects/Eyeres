package com.appzone.eyeres.models;

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
    private String left_degree;
    private String right_degree;
    private String left_deviation;
    private String right_deviation;
    private String left_axis;
    private String right_axis;
    private int left_amount;
    private int right_amount;
    private int type;
    private int base_amount_left;
    private int base_amount_right;



    public ItemCartModel(int product_id, String product_image, String product_name_ar, String product_name_en, double product_cost, int quantity, double total, int similar, String left_degree, String right_degree, String left_deviation, String right_deviation, String left_axis, String right_axis, int left_amount, int right_amount, int type,int base_amount_left,int base_amount_right) {
        this.product_id = product_id;
        this.product_image = product_image;
        this.product_name_ar = product_name_ar;
        this.product_name_en = product_name_en;
        this.product_cost = product_cost;
        this.quantity = quantity;
        this.total = total;
        this.similar = similar;
        this.left_degree = left_degree;
        this.right_degree = right_degree;
        this.left_deviation = left_deviation;
        this.right_deviation = right_deviation;
        this.left_axis = left_axis;
        this.right_axis = right_axis;
        this.left_amount = left_amount;
        this.right_amount = right_amount;
        this.type = type;
        this.base_amount_left= base_amount_left;
        this.base_amount_right = base_amount_right;
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

    public int getProduct_type() {
        return type;
    }

    public void setProduct_type(int product_type) {
        this.type = product_type;
    }

    public String getLeft_deviation() {
        return left_deviation;
    }

    public void setLeft_deviation(String left_deviation) {
        this.left_deviation = left_deviation;
    }

    public String getRight_deviation() {
        return right_deviation;
    }

    public void setRight_deviation(String right_deviation) {
        this.right_deviation = right_deviation;
    }

    public String getLeft_axis() {
        return left_axis;
    }

    public void setLeft_axis(String left_axis) {
        this.left_axis = left_axis;
    }

    public String getRight_axis() {
        return right_axis;
    }

    public void setRight_axis(String right_axis) {
        this.right_axis = right_axis;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getBase_amount_left() {
        return base_amount_left;
    }

    public void setBase_amount_left(int base_amount_left) {
        this.base_amount_left = base_amount_left;
    }

    public int getBase_amount_right() {
        return base_amount_right;
    }

    public void setBase_amount_right(int base_amount_right) {
        this.base_amount_right = base_amount_right;
    }


}
