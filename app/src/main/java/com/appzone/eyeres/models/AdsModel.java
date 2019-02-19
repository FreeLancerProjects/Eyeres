package com.appzone.eyeres.models;

import java.io.Serializable;
import java.util.List;

public class AdsModel implements Serializable {

    private List<Ads> data;

    public List<Ads> getData() {
        return data;
    }

    public class Ads implements Serializable
    {
        private String title_ar;
        private String title_en;
        private String image;

        public String getTitle_ar() {
            return title_ar;
        }

        public String getTitle_en() {
            return title_en;
        }

        public String getImage() {
            return image;
        }
    }
}
