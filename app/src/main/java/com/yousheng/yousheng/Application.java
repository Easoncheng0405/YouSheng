package com.yousheng.yousheng;

import com.alibaba.android.arouter.launcher.ARouter;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class Application extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化litepal配置
        LitePal.initialize(this);
        ARouter.init(this);
    }
}
