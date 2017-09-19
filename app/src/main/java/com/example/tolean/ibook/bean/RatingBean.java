package com.example.tolean.ibook.bean;

/**
 * Created by Tolean on 2017/9/12.
 */

public  class RatingBean {
    /**
     * max : 10
     * numRaters : 224575
     * average : 9.0
     * min : 0
     */

    private int max;
    private int numRaters;
    private String average;
    private int min;

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getNumRaters() {
        return numRaters;
    }

    public void setNumRaters(int numRaters) {
        this.numRaters = numRaters;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
