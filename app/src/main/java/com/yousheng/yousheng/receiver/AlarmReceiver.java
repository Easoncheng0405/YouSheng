package com.yousheng.yousheng.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.yousheng.yousheng.habit.Habit;
import com.yousheng.yousheng.notify.NewItem;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction() == null)
            return;
        //收到开机，网络切换广播，打开所有闹钟
        Log.d("onReceive", "intent aciton = "+intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED) || intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            AlarmHelper.notifyAllAlarm(context);
        }
        String action = intent.getAction();
        //自己人
        if (action.contains("yousheng")) {
            try {
                String type = intent.getStringExtra("type");
                long id = intent.getLongExtra("id", -1);
                String str;
                if (id == -1)
                    return;
                if (type.equals("item"))
                    str = LitePal.find(NewItem.class, id).getContent();
                else
                    str = LitePal.find(Habit.class, id).getTitle();
                ToastUtil.showMsg(context, "type = " + type + " ,id = " + id + " content = " + str);
            } catch (Exception e) {
                Log.e("AlarmReceiver", "onReceive exception", e);
            }
        }
    }
}
