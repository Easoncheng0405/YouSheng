package com.yousheng.yousheng.model;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;

import java.util.Objects;

public class MenseInfo extends LitePalSupport {
    private long id;

    //当前日期
    private long date;

    //月经开始
    private boolean isMenseStart;

    //月经结束
    private boolean isMenseEnd;

    //今天是否make love
    private boolean hasMakeLove;

    //今天的备注
    private String comment;

    public MenseInfo() {
        this.id = LitePal.count(MenseInfo.class) + 1;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public boolean isMenseStart() {
        return isMenseStart;
    }

    public void setMenseStart(boolean menseStart) {
        isMenseStart = menseStart;
    }

    public boolean isMenseEnd() {
        return isMenseEnd;
    }

    public void setMenseEnd(boolean menseEnd) {
        isMenseEnd = menseEnd;
    }

    public boolean isHasMakeLove() {
        return hasMakeLove;
    }

    public void setHasMakeLove(boolean hasMakeLove) {
        this.hasMakeLove = hasMakeLove;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenseInfo menseInfo = (MenseInfo) o;
        return id == menseInfo.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
