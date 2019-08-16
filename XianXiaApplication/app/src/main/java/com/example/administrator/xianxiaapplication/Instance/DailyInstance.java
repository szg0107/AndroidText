package com.example.administrator.xianxiaapplication.Instance;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/7/28.
 */

public class DailyInstance {
    private String date; //日期
    private ArrayList<StoriesInstace> stories;//当日新闻
    private ArrayList<Top_StoriesInstance> top_stories;//界面顶部

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<StoriesInstace> getStories() {
        return stories;
    }

    public void setStories(ArrayList<StoriesInstace> stories) {
        this.stories = stories;
    }

    public ArrayList<Top_StoriesInstance> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(ArrayList<Top_StoriesInstance> top_stories) {
        this.top_stories = top_stories;
    }
}
