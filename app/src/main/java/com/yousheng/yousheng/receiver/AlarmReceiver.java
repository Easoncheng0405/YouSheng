package com.yousheng.yousheng.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.yousheng.yousheng.uitl.ToastUtil;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null)
            return;
        //收到开机，网络切换广播，打开所有闹钟
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            AlarmHelper.notifyAllAlarm(context);
        }
        String action = intent.getAction();
        //自己人
        if (action.contains("yousheng")) {
            try {
                String[] words = action.split("\\.");
                ToastUtil.showMsg(context, "type = " + words[2] + " ,id = " + words[3]);
            } catch (Exception e) {
                Log.e("AlarmReceiver", "onReceive exception", e);
            }
        }
    }
}
