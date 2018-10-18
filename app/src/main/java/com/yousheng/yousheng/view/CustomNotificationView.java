package com.yousheng.yousheng.view;

import android.content.Context;
import android.widget.RemoteViews;

import com.yousheng.yousheng.R;

/**
 * 自定义通知栏通知view
 */
public class CustomNotificationView {
    private RemoteViews notificationView;

    public CustomNotificationView(Context context) {
        notificationView = new RemoteViews(context.getPackageName(),
                R.layout.layout_remote_notification);
    }

}
