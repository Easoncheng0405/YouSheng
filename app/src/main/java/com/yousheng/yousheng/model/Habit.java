package com.yousheng.yousheng.model;

import org.litepal.crud.LitePalSupport;

import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;


//习惯
public class Habit extends LitePalSupport implements Comparable<Habit> {

    private long id;

    //标题
    private String mainTitle;

    //副标题,官方习惯才有
    private String subTitle;

    //习惯的好处,官方习惯才有
    private String content;

    //是否需要打卡?
    private boolean needSign;

    //官方习惯?
    private boolean official;

    //优生打卡?
    private boolean youSheng;

    //提醒?
    private boolean notify;

    //闹钟提醒时间
    private long clockTime;

    //坚持的时间
    private int keepDays;

    //打卡时间
    private long signTime;

    //优先级
    private int level;


    public long getSignTime() {
        return signTime;
    }

    public void setSignTime(long signTime) {
        this.signTime = signTime;
    }

    public int getKeepDays() {
        return keepDays;
    }

    public void setKeepDays(int keepDays) {
        this.keepDays = keepDays;
    }

    public Habit(long id) {
        this.id = id;
    }

    public Habit() {
        level = Integer.MAX_VALUE;
    }

    public long getId() {
        return id;
    }

    public String getMainTitle() {
        return mainTitle;
    }

    public void setMainTitle(String mainTitle) {
        this.mainTitle = mainTitle;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isNeedSign() {
        return needSign;
    }

    public void setNeedSign(boolean needSign) {
        this.needSign = needSign;
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

    public boolean isNotify() {
        return notify;
    }

    public void setNotify(boolean notify) {
        this.notify = notify;
    }

    public long getClockTime() {
        return clockTime;
    }

    public void setClockTime(long clockTime) {
        this.clockTime = clockTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public boolean isSigned() {
        Calendar db = Calendar.getInstance(Locale.CHINA);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        db.setTimeInMillis(signTime);
        now.setTimeInMillis(System.currentTimeMillis());
        return now.get(Calendar.DAY_OF_YEAR) <= db.get(Calendar.DAY_OF_YEAR);
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
    public int compareTo(Habit o) {
        return level - o.level;
    }
}
