package com.yousheng.yousheng.weight;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Comparator;

public class Weight extends LitePalSupport implements Comparator<Weight> {

    private long id;
    //重量，单位斤
    private float weight;
    //记录时间
    private long time;


    public Weight() {
        this.id = LitePal.count(Weight.class) + 1;
    }

    //按照记录时间排序
    @Override
    public int compare(Weight o1, Weight o2) {
        return Long.compare(o1.time, o2.time);
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
}
