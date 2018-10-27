package com.yousheng.yousheng.model;

import org.litepal.crud.LitePalSupport;

public class OvulationRecord extends LitePalSupport {
    public final static int STATE_DEFAULT = -1;
    public final static int STATE_WEAKEST = 0;
    public final static int STATE_WEAK = 1;
    public final static int STATE_STRONG = 3;
    public final static int STATE_STRONGEST = 4;

    /****time millis*/
    private long date;

    /****ovulation recor state from 0~4***/
    private int state;

    private boolean hasMakeLove;


    public OvulationRecord() {
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public boolean isHasMakeLove() {
        return hasMakeLove;
    }

    public void setHasMakeLove(boolean hasMakeLove) {
        this.hasMakeLove = hasMakeLove;
    }
}
