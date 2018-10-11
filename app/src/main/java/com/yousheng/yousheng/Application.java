package com.yousheng.yousheng;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

public class Application extends LitePalApplication {

    //初始化litepal配置
    @Override
    public void onCreate() {
        super.onCreate();
        LitePal.initialize(this);
    }
}
