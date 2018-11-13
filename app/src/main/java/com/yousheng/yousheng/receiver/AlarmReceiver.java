package com.yousheng.yousheng.receiver;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.activity.MainActivity;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.model.NewItem;

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

        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setDefaults(Notification.DEFAULT_SOUND)
                .setContentIntent(contentIntent);
        //发送通知请求
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel("yousheng", "柚生", NotificationManager.IMPORTANCE_HIGH);//设置唯一的渠道通知Id
            mChannel.enableLights(true);//开启灯光
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);//开启震动
            mChannel.setVibrationPattern(new long[]{0, 1000, 0, 1000});//8.0以下版本的效果一样,都是震动
            notificationManager.createNotificationChannel(mChannel);//在NotificationManager中注册渠道通知对象
        }

        switch (intent.getAction()) {
            case AlarmHelper.HABIT_IN_TIME:
                Habit habit = LitePal.find(Habit.class, (long) intent.getLongExtra("id", -1));
                if (habit == null)
                    break;
                Log.d("AlarmReceiver", "onReceive habit notify, id=" + habit.getId() + ",content=" + habit.getMainTitle());
                builder.setContentTitle("新习惯提醒");
                builder.setContentText(habit.getMainTitle());
                builder.setWhen(habit.getClockTime());
                notificationManager.notify((int) habit.getId(), builder.build());
                break;
            case AlarmHelper.ITEM_IN_TIME:
                NewItem item = LitePal.find(NewItem.class, (long) intent.getLongExtra("id", -1));
                if (item == null)
                    break;
                //提醒事项只提醒一次
                builder.setContentTitle("待办事项提醒");
                builder.setContentText(item.getContent());
                builder.setWhen(item.getTime());
                notificationManager.notify((int) item.getId(), builder.build());
                item.setNotify(false);
                item.save();
                Log.d("AlarmReceiver", "onReceive item notify, id=" + item.getId() + ",content=" + item.getContent());
                break;
            case AlarmHelper.JIN_QI:
                builder.setContentTitle(context.getString(R.string.mense_notice_main_title));
                builder.setContentText(context.getString(R.string.mense_notice_sub_title));
                notificationManager.notify(Constants.NOTICE_ID_MENSE, builder.build());
                break;
        }
    }
}
