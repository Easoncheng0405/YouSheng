package com.yousheng.yousheng.notify;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

/**
 * 新事项
 */
public class NewItem extends LitePalSupport {



    //提醒事项内容
    private String content;

    //提醒时间戳(未设置为0)
    private long time;

    public NewItem(String content, long time) {
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

    @Override
    public String toString() {
        return "{content='" + content + '\'' + ", time=" + time + '}';
    }
}
