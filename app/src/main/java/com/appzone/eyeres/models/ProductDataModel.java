package com.appzone.eyeres.models;

import java.io.Serializable;
import java.util.List;

public class ProductDataModel implements Serializable {

    private List<ProductModel> data;
    private Meta meta;

    public List<ProductModel> getData() {
        return data;
    }
    public Meta getMeta() {
        return meta;
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
        private int last_page;

        public int getCurrent_page() {
            return current_page;
        }

        public int getLast_page() {
            return last_page;
        }
    }
}
