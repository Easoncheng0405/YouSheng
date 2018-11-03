package com.yousheng.yousheng.manager;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.View;

import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.uitl.SPSingleton;
import com.yousheng.yousheng.view.EvaluationPopupWindow;

public class FeedBackManager {

    /***
     * 记录app启动次数
     * */
    public static void recordAppLaunchTimes(Context context) {
        int times = SPSingleton.get().getInt(PrefConstants.PREFS_KEY_APP_LAUNCH_TIMES, 0);
        SPSingleton.get().putInt(PrefConstants.PREFS_KEY_APP_LAUNCH_TIMES, times + 1);
        SPSingleton.get().putBoolean(PrefConstants.PREFS_KEY_WINDOW_SHOWED, false);
    }

    /***
     *记录反馈界面按钮是否点击
     * */
    public static void recordFeedBackButtonClicked() {
        SPSingleton.get().putBoolean(PrefConstants.PREFS_KEY_FEEDBACK_BUTTON_CLICKED, true);
    }

    /***
     * 反馈界面弹出规则为：第三次打开app时弹出界面。
     * 后面如果没有点击按钮，那么第十次打开界面时弹出评价window
     * */
    public static void showEvaluationPopupWindow(Activity context, View rootView) {
        boolean isWindowShowed = SPSingleton.get().getBoolean(PrefConstants.PREFS_KEY_WINDOW_SHOWED, false);
        boolean isFeedButtonClicked = SPSingleton.get()
                .getBoolean(PrefConstants.PREFS_KEY_FEEDBACK_BUTTON_CLICKED, false);
        if (isFeedButtonClicked || isWindowShowed) {
            return;
        }

        int appLaunchTimes = SPSingleton.get().getInt(PrefConstants.PREFS_KEY_APP_LAUNCH_TIMES, 0);
        if (appLaunchTimes == 3 || appLaunchTimes == 10) {
            EvaluationPopupWindow window = new EvaluationPopupWindow(context);
            window.showAtLocation(rootView, Gravity.CENTER, 0, 0);
            SPSingleton.get().putBoolean(PrefConstants.PREFS_KEY_WINDOW_SHOWED, true);
        }
    }

}
