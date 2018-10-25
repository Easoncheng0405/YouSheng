package com.yousheng.yousheng.activity;

import org.litepal.crud.LitePalSupport;

public class Market extends LitePalSupport {

    private long id;

    private String name;

    private String packageName;

    public long getId() {
        return id;
    }

    public void setId(long id) {
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


}
