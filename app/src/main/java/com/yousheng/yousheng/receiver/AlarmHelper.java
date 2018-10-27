package com.yousheng.yousheng.receiver;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.notify.NewItem;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * 在设备重启后，设置的所有闹钟都会被清除，监听到设备开机完成或网络切换的广播
 * app启动都要重新设置一遍所有闹钟
 * <p>
 * <p>
 * 针对同一个PendingIntent，AlarmManager.set()函数不能设置多个alarm。
 * 调用该函数时，假如已经有old alarm使用相同的PendingIntent，
 * 会先取消（cancel）old alarm，然后再设置新的alarm。
 */
public class AlarmHelper {

    //到提醒时间了
    static final String ITEM_IN_TIME = "com.yousheng.item";
    static final String HABIT_IN_TIME = "com.yousheng.habit";
    private static final long DAY = 24 * 60 * 60 * 1000;

    //依次打开所有闹钟，PendingIntent.FLAG_CANCEL_CURRENT会取消掉已有闹钟重新设置
    public static void notifyAllAlarm(Context context) {
        List<NewItem> items = LitePal.findAll(NewItem.class);
        for (NewItem item : items)
            if (item.getTime() > 0 && item.isNotify())
                notifyNewItem(context, item);
        List<Habit> habits = LitePal.findAll(Habit.class);
        for (Habit habit : habits)
            if (habit.getClockTime() > 0 && habit.isNotify())
                notifyHabit(context, habit);
    }

    public static void notifyNewItem(Context context, NewItem item) {
        try {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmHelper.ITEM_IN_TIME);
            intent.putExtra("id", item.getId());
            PendingIntent sender = PendingIntent.getBroadcast(context, (int) item.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP, getNextDayMillis(item.getTime()), sender);
        } catch (Exception e) {
            Log.e("AlarmHelper", "notifyNewItem exception", e);
        }
    }


    public static void notifyHabit(Context context, Habit habit) {
        try {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmHelper.HABIT_IN_TIME);
            intent.putExtra("id", habit.getId());
            PendingIntent sender = PendingIntent.getBroadcast(context, (int) habit.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, getNextDayMillis(habit.getClockTime()), DAY, sender);
        } catch (Exception e) {
            Log.e("AlarmHelper", "notifyHabit exception", e);
        }
    }

    //往后一直加一天直到时间大于当前日期
    private static long getNextDayMillis(long millis) {
        long now = System.currentTimeMillis();
        Calendar db = Calendar.getInstance(Locale.CHINA);
        db.setTimeInMillis(millis);
        while (db.getTimeInMillis() < now) {
            db.add(Calendar.DATE, 1);
        }
        return db.getTimeInMillis();
    }
}
