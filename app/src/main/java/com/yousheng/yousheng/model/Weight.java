package com.yousheng.yousheng.model;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Comparator;

public class Weight extends LitePalSupport implements Comparable<Weight> {

    private long id;
    //重量，单位斤
    private float weight;
    //记录时间
    private long time;


    public Weight() {
        this.id = LitePal.count(Weight.class) + 1;
    }

    public long getId() {
        return id;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public int compareTo(Weight o) {
        return Long.compare(time, o.time);
    }
}
