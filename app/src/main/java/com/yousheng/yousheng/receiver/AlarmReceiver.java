package com.yousheng.yousheng.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFY_IN_TIME = "com.yousheng.newitem";
    public static final String HABIT_IN_TIME = "com.yousheng.habit";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null)
            Log.d("AlarmReceiver", intent.getAction() + "|"
                    + intent.getStringExtra("str"));
    }
}
