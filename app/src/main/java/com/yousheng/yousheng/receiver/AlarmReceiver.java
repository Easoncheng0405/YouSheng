package com.yousheng.yousheng.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;


public class AlarmReceiver extends BroadcastReceiver {

    public static final String NOTIFY_IN_TIME="com.yousheng.newitem";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() != null)
            switch (intent.getAction()) {
                case NOTIFY_IN_TIME:
                    Log.d("AlarmReceiver", intent.getStringExtra("str"));
                    break;
            }
    }
}
