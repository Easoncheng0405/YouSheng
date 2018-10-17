package com.yousheng.yousheng.receiver;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.yousheng.yousheng.habit.Habit;
import com.yousheng.yousheng.notify.NewItem;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.List;

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
    private static final String ITEM_IN_TIME = "com.yousheng.item";
    private static final String HABIT_IN_TIME = "com.yousheng.habit";
    private static final long DAY = 24 * 60 * 60 * 1000;

    //依次打开所有闹钟，PendingIntent.FLAG_CANCEL_CURRENT会取消掉已有闹钟重新设置
    public static void notifyAllAlarm(Context context) {
        List<NewItem> items = LitePal.findAll(NewItem.class);
        for (NewItem item : items)
            if (item.getTime() > 0)
                notifyNewItem(context, item);
        List<Habit> habits = LitePal.findAll(Habit.class);
        for (Habit habit : habits)
            if (habit.getTime() > 0)
                notifyHabit(context, habit);
    }

    public static void notifyNewItem(Context context, NewItem item) {
        try {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmHelper.ITEM_IN_TIME);
            intent.putExtra("id", item.getId());
            intent.putExtra("type", "item");
            PendingIntent sender = PendingIntent.getBroadcast(context, (int) item.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setExact(AlarmManager.RTC_WAKEUP, item.getTime(), sender);
        } catch (Exception e) {
            Log.e("AlarmHelper", "notifyNewItem exception", e);
        }
    }


    public static void notifyHabit(Context context, Habit habit) {
        try {
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmHelper.HABIT_IN_TIME);
            intent.putExtra("id", habit.getId());
            intent.putExtra("type", "habit");
            PendingIntent sender = PendingIntent.getBroadcast(context, (int) habit.getId(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.setRepeating(AlarmManager.RTC_WAKEUP, habit.getTime(), DAY, sender);
        } catch (Exception e) {
            Log.e("AlarmHelper", "notifyHabit exception", e);
        }
    }
}
