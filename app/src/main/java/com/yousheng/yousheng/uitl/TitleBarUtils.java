package com.yousheng.yousheng.uitl;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.wuhenzhizao.titlebar.utils.ScreenUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class TitleBarUtils {

    /**
     * 修改左边箭头的左边距
     */
    public static void changeTitleImageLeftMargin(final Context context, final CommonTitleBar titleBar) {
        titleBar.post(new Runnable() {
            @Override
            public void run() {
                TextView textView = titleBar.getLeftTextView();
                if (textView != null) {
                    textView.setPadding((int) ScreenUtils.dp2Px(context, 15),
                            textView.getPaddingTop(),
                            textView.getPaddingRight(),
                            textView.getPaddingBottom());
                }
            }
        });
    }

    public static void addTitleBarListener(final Activity activity, CommonTitleBar titleBar) {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_TEXT:
                        activity.finish();
                        break;
                }
            }
        });
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}

