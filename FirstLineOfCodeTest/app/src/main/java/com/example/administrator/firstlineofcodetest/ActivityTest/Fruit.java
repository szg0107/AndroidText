package com.example.administrator.firstlineofcodetest.ActivityTest;

/**
 * Created by Administrator on 2018/3/11.
 * ListView适配器的适配类型图文布局
 */

public class Fruit {
    private String name;
    private int imageId;

    public Fruit(String name, int imageId) {
        this.name = name;
        this.imageId = imageId;
    }

    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }
}
