package com.yousheng.yousheng.habit;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Objects;


//习惯
public class Habit extends LitePalSupport {

    private long id;

    //标题
    private String title;

    //副标题,官方习惯才有
    private String title2;

    //习惯的好处,官方习惯才有
    private String content;

    //已打卡天数
    private int days;

    //是否已加入打卡
    private int state;

    //官方习惯或自定义习惯
    private int type;

    //时间
    private long time;

    public Habit(long id) {
        this.id = id;
    }

    public Habit() {
        this.id = LitePal.count(Habit.class) + 1;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getDays() {
        return days;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Habit habit = (Habit) o;
        return id == habit.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Habit{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", title2='" + title2 + '\'' +
                ", content='" + content + '\'' +
                ", days=" + days +
                ", state=" + state +
                ", type=" + type +
                '}';
    }


}
