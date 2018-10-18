package com.yousheng.yousheng.habit;

import org.litepal.crud.LitePalSupport;

//打卡表
public class Record extends LitePalSupport {

    //习惯id
    private long id;
    //打卡时间
    private long time;
    //已经坚持的天数
    private int days;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }
}
