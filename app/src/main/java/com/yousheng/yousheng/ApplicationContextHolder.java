package com.yousheng.yousheng;

import android.content.Context;

public class ApplicationContextHolder {
    private static Context sApplicationContext;

    public static Context getApplicationContext() {
        return sApplicationContext;
    }

    public static void setApplicationContext(Context sApplicationContext) {
        ApplicationContextHolder.sApplicationContext = sApplicationContext;
    }
}
