package com.yousheng.yousheng.model;

import org.litepal.crud.LitePalSupport;

public class Market extends LitePalSupport implements Comparable<Market> {

    private int id;

    private String name;

    private String packageName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }


    @Override
    public int compareTo(Market o) {
        return id - o.id;
    }
}
