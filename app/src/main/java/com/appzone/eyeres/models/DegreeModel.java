package com.appzone.eyeres.models;

import java.io.Serializable;
import java.util.List;

public class DegreeModel implements Serializable {

    private List<String> axis;
    private List<String> deviation;
    private List<String> myopia;

    public List<String> getAxis() {
        return axis;
    }

    public List<String> getDeviation() {
        return deviation;
    }

    public List<String> getMyopia() {
        return myopia;
    }
}
