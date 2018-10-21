package com.yousheng.yousheng.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.yousheng.yousheng.habit.Habit;
import com.yousheng.yousheng.notify.NewItem;

import org.litepal.LitePal;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null)
            return;
        //收到开机，网络切换广播，打开所有闹钟
        Log.d("AlarmReceiver", "intent aciton = " + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            AlarmHelper.notifyAllAlarm(context);
        }
        //自己人
        switch (intent.getAction()) {
            case AlarmHelper.HABIT_IN_TIME:
                Habit habit = LitePal.find(Habit.class, (long) getResultCode());
                Log.d("AlarmReceiver", "onReceive habit notify, id=" + habit.getId() + ",content=" + habit.getMainTitle());
                break;
            case AlarmHelper.ITEM_IN_TIME:
                NewItem item = LitePal.find(NewItem.class, (long) getResultCode());
                //提醒事项只提醒一次
                item.setNotify(false);
                item.save();
                Log.d("AlarmReceiver", "onReceive item notify, id=" + item.getId() + ",content=" + item.getContent());
                break;
        }
    }
}
