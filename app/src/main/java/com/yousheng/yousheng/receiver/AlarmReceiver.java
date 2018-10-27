package com.yousheng.yousheng.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yousheng.yousheng.R;
import com.yousheng.yousheng.activity.MainActivity;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.notify.NewItem;

import org.litepal.LitePal;

import static android.content.Context.NOTIFICATION_SERVICE;

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

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        Intent appIntent = null;
        appIntent = new Intent(context, MainActivity.class);
        appIntent.setAction(Intent.ACTION_MAIN);
        appIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        appIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);//关键的一步，设置启动模式
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setWhen(System.currentTimeMillis())
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        //发送通知请求


        switch (intent.getAction()) {
            case AlarmHelper.HABIT_IN_TIME:
                Habit habit = LitePal.find(Habit.class, (long) intent.getLongExtra("id", -1));
                if (habit == null)
                    break;
                Log.d("AlarmReceiver", "onReceive habit notify, id=" + habit.getId() + ",content=" + habit.getMainTitle());
                builder.setContentTitle("新习惯提醒");
                builder.setContentText(habit.getMainTitle());
                notificationManager.notify((int) habit.getId(), builder.build());
                break;
            case AlarmHelper.ITEM_IN_TIME:
                NewItem item = LitePal.find(NewItem.class, (long) intent.getLongExtra("id", -1));
                if (item == null)
                    break;
                //提醒事项只提醒一次
                builder.setContentTitle("待办事项提醒");
                builder.setContentText(item.getContent());
                notificationManager.notify((int) item.getId(), builder.build());
                item.setNotify(false);
                item.save();
                Log.d("AlarmReceiver", "onReceive item notify, id=" + item.getId() + ",content=" + item.getContent());
                break;
        }
    }
}
