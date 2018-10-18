package com.yousheng.yousheng.habit;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
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

    //已加入打卡?
    private boolean record;

    //官方习惯?
    private boolean official;

    //优生打卡?
    private boolean youSheng;

    //时间
    @Column(defaultValue = "-1")
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

    public boolean isRecord() {
        return record;
    }

    public void setRecord(boolean record) {
        this.record = record;
    }

    public boolean isOfficial() {
        return official;
    }

    public void setOfficial(boolean official) {
        this.official = official;
    }

    public boolean isYouSheng() {
        return youSheng;
    }

    public void setYouSheng(boolean youSheng) {
        this.youSheng = youSheng;
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


}
