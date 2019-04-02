package com.appzone.eyeres.models;

import java.io.Serializable;

public class Special_Lenses_Model implements Serializable {

    private Special_Lenses special_lenses;

    public Special_Lenses getSpecial_lenses() {
        return special_lenses;
    }

    public class Special_Lenses implements Serializable
    {
        private String phone;
        private Description description;

        public String getPhone() {
            return phone;
        }

        public Description getDescription() {
            return description;
        }
    }

    public class Description implements Serializable
    {
        private String ar;
        private  String en;

        public String getAr() {
            return ar;
        }

        public String getEn() {
            return en;
        }
    }
}
