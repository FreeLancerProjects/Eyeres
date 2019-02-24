package com.appzone.eyeres.models;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderDataModel implements Serializable {

    private List<OrderModel> data;
    private Meta meta;

    public List<OrderModel> getData() {
        return data;
    }
    public Meta getMeta() {
        return meta;
    }
    public class OrderModel implements Serializable
    {
        private int id;
        private double total;
        private int status;
        private long updated_at;
        List<ProductModel> itemsList;

        public int getId() {
            return id;
        }

        public double getTotal() {
            return total;
        }

        public int getStatus() {
            return status;
        }

        public List<ProductModel> getItemsList() {
            return itemsList;
        }

        public long getUpdated_at() {
            return updated_at;
        }
    }

    public class ProductModel implements Serializable
    {
        private int id;
        private String name_ar;
        private String name_en;
        private String description_ar;
        private String description_en;
        private double price;
        private double price_after_discount;
        private double discount_percentage;
        private List<String> images;
        private int featured;
        private int is_favorite;
        private int favorite_id;
        private int sales;
        private int has_sizes;
        private Brand brand;

        private int similar;
        private String right_degree;
        private String left_degree;
        private int right_amount;
        private int left_amount;
        @SerializedName("package")
        private int package_size;
        private int quantity;
        private double total;



        public int getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public String getDescription_ar() {
            return description_ar;
        }

        public String getDescription_en() {
            return description_en;
        }

        public double getPrice() {
            return price;
        }

        public double getPrice_after_discount() {
            return price_after_discount;
        }

        public double getDiscount_percentage() {
            return discount_percentage;
        }

        public List<String> getImages() {
            return images;
        }

        public int getFeatured() {
            return featured;
        }

        public int getIs_favorite() {
            return is_favorite;
        }

        public int getFavorite_id() {
            return favorite_id;
        }

        public int getSales() {
            return sales;
        }

        public int getHas_sizes() {
            return has_sizes;
        }

        public Brand getBrand() {
            return brand;
        }

        public void setIs_favorite(int is_favorite) {
            this.is_favorite = is_favorite;
        }

        public void setFavorite_id(int favorite_id) {
            this.favorite_id = favorite_id;
        }

        public int getSimilar() {
            return similar;
        }

        public String getRight_degree() {
            return right_degree;
        }

        public String getLeft_degree() {
            return left_degree;
        }

        public int getRight_amount() {
            return right_amount;
        }

        public int getLeft_amount() {
            return left_amount;
        }

        public int getPackage_size() {
            return package_size;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getTotal() {
            return total;
        }
    }

    public class Brand implements Serializable
    {
        private String name_ar;
        private String name_en;

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }
    }

    public class Meta implements Serializable
    {
        private int current_page;

        public int getCurrent_page() {
            return current_page;
        }
    }
}
