package com.example.tolean.ibook.bean;

/**
 * Created by Tolean on 2017/9/12.
 */

public class ImagesBean {
    /**
     * small : https://img1.doubanio.com/spic/s1237549.jpg
     * large : https://img1.doubanio.com/lpic/s1237549.jpg
     * medium : https://img1.doubanio.com/mpic/s1237549.jpg
     */

    private String small;
    private String large;
    private String medium;

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }
}
