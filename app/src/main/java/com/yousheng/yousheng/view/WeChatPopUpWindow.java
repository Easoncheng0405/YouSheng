package com.yousheng.yousheng.view;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;

import com.yousheng.yousheng.R;

public class WeChatPopUpWindow extends PopupWindow {
    public WeChatPopUpWindow(Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View contentView = inflater.inflate(R.layout.layout_popup_window_wechat, null);
        setContentView(contentView);
        this.update();
    }

}
