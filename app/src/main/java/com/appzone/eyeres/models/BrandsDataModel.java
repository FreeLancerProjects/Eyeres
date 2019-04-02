package com.appzone.eyeres.models;

import java.io.Serializable;
import java.util.List;

public class BrandsDataModel implements Serializable{

    private List<BrandModel> data;
    private Meta meta;

    public List<BrandModel> getData() {
        return data;
    }

    public Meta getMeta() {
        return meta;
    }

    public class BrandModel implements Serializable
    {
        private int id;
        private String name_ar;
        private String name_en;
        private String image;
        private List<Trends> trends;
        public int getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public String getImage() {
            return image;
        }

        public List<Trends> getTrends() {
            return trends;
        }
    }

    public class Trends implements Serializable
    {
        private int id;
        private String name_ar;
        private String name_en;
        private String image;
        private String brand_id;

        public int getId() {
            return id;
        }

        public String getName_ar() {
            return name_ar;
        }

        public String getName_en() {
            return name_en;
        }

        public String getImage() {
            return image;
        }

        public String getBrand_id() {
            return brand_id;
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
