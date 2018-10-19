package com.yousheng.yousheng.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.wuhenzhizao.titlebar.utils.ScreenUtils;
import com.yousheng.yousheng.R;

public class WeChatPopUpWindow extends PopupWindow {

    public WeChatPopUpWindow(Activity activity) {
        LayoutInflater inflater = LayoutInflater.from(activity);
        View contentView = inflater.inflate(R.layout.layout_popup_window_wechat, null);
        setContentView(contentView);

        this.setHeight(WindowManager.LayoutParams.MATCH_PARENT);
        this.setWidth(WindowManager.LayoutParams.MATCH_PARENT);
        this.setFocusable(true);
        this.setOutsideTouchable(false);
        this.setBackgroundDrawable(new ColorDrawable(Color.BLACK));
        this.update();

        //set listener
        contentView.findViewById(R.id.btn_make_sure).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

}
