package com.appzone.eyeres.models;

import java.io.Serializable;

public class OrderStatusModel implements Serializable {
    int status;

    public OrderStatusModel(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}
