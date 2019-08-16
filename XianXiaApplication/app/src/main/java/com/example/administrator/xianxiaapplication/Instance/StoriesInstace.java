package com.example.administrator.xianxiaapplication.Instance;

/**
 * Created by Administrator on 2016/7/28.
 */

public class StoriesInstace {
    private  String title;//新闻标题
    private  int ga_prefix;//供 Google Analytics 使用
    private  String[] images;//
    private int type;//作用未知
    private int  id;//url 与 share_url 中最后的数字（应为内容的 id）

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getGa_prefix() {
        return ga_prefix;
    }

    public void setGa_prefix(int ga_prefix) {
        this.ga_prefix = ga_prefix;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
