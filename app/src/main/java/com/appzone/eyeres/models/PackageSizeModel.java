package com.appzone.eyeres.models;

import java.io.Serializable;
import java.util.List;

public class PackageSizeModel implements Serializable{

    private List<Integer> sizes;

    public List<Integer> getSizes() {
        return sizes;
    }
}
