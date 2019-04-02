package com.appzone.eyeres.models;

import java.io.Serializable;

public class CouponModel implements Serializable {

    private CouponData data;

    public CouponData getData() {
        return data;
    }

    public class CouponData implements Serializable
    {
        private String code;
        private double value;

        public String getCode() {
            return code;
        }

        public double getValue() {
            return value;
        }
    }
}
