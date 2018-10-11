package com.yousheng.yousheng.notify;

import android.support.annotation.NonNull;

import org.litepal.LitePal;
import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 新事项
 */
public class NewItem extends LitePalSupport {


    private long id;

    //提醒事项内容
    private String content;

    //提醒时间戳(未设置为0)
    private long time;

    NewItem(String content, long time) {
        this.content = content;
        this.time = time;
        this.id = LitePal.count(NewItem.class);
    }

    NewItem(long id, String content, long time) {
        this.id = id;
        this.content = content;
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "{id=" + id + ", content='" + content + '\'' + ", time=" + time + '}';
    }
}
