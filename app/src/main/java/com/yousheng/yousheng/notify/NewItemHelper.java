package com.yousheng.yousheng.notify;


import com.yousheng.yousheng.model.NewItem;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

//api
public class NewItemHelper {


    public enum TimeRange {
        NO_DATE(0),    //没有日期
        UP_TO_DATE(1), //过期
        TODAY(2),      //今天
        TOMORROW(3),   //明天
        IN_WEEK(4), //一周内
        FUTURE(5);     //更久

        int value;

        TimeRange(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public static TimeRange getRange(long compareTimeMillis, NewItem item) {

        if (item.getTime() <= 0)
            return TimeRange.NO_DATE;
        if (item.getTime() < compareTimeMillis)
            return TimeRange.UP_TO_DATE;
        //现在的日期
        Calendar now = Calendar.getInstance(Locale.CHINA);
        now.setTimeInMillis(compareTimeMillis);
        //待办事项的日期
        Calendar todo = Calendar.getInstance(Locale.CHINA);
        todo.setTimeInMillis(item.getTime());
        //在同一年
        if (now.get(Calendar.YEAR) == todo.get(Calendar.YEAR)) {
            int days = todo.get(Calendar.DAY_OF_YEAR) - now.get(Calendar.DAY_OF_YEAR);
            if (days == 0)
                return TimeRange.TODAY;
            if (days == 1)
                return TimeRange.TOMORROW;
            if (days <= 7)
                return TimeRange.IN_WEEK;
            return TimeRange.FUTURE;
        }
        //跨年直接返回更久
        return TimeRange.FUTURE;
    }

    //返回所有时间范围的提醒事项的hashmap，key为六个时间范围的枚举，值为属于此范围的数组
    public static HashMap<TimeRange, ArrayList<NewItem>> getAllNewItemInRange() {
        ArrayList<NewItem> upToDate = new ArrayList<>();
        ArrayList<NewItem> today = new ArrayList<>();
        ArrayList<NewItem> tomorrow = new ArrayList<>();
        ArrayList<NewItem> inWeek = new ArrayList<>();
        ArrayList<NewItem> future = new ArrayList<>();
        ArrayList<NewItem> noDate = new ArrayList<>();

        HashMap<TimeRange, ArrayList<NewItem>> map = new HashMap<>();

        //查出所有代办事项
        List<NewItem> items = LitePal.findAll(NewItem.class);

        long millis = System.currentTimeMillis();
        for (NewItem item : items) {
            switch (getRange(millis, item)) {
                case FUTURE:
                    future.add(item);
                    break;
                case TODAY:
                    today.add(item);
                    break;
                case TOMORROW:
                    tomorrow.add(item);
                    break;
                case UP_TO_DATE:
                    upToDate.add(item);
                    break;
                case IN_WEEK:
                    inWeek.add(item);
                    break;
                case NO_DATE:
                    noDate.add(item);
                    break;
                default:
                    break;
            }
        }
        map.put(TimeRange.FUTURE, future);
        map.put(TimeRange.IN_WEEK, inWeek);
        map.put(TimeRange.TODAY, today);
        map.put(TimeRange.TOMORROW, tomorrow);
        map.put(TimeRange.UP_TO_DATE, upToDate);
        map.put(TimeRange.NO_DATE, noDate);
        return map;
    }

}
