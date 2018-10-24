package com.yousheng.yousheng;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yousheng.yousheng.habit.Habit;

import org.litepal.LitePal;
import org.litepal.LitePalApplication;

import java.util.ArrayList;
import java.util.List;

public class Application extends LitePalApplication {

    @Override
    public void onCreate() {
        super.onCreate();
        //初始化litepal配置
        LitePal.initialize(this);
        ARouter.init(this);
        ARouter.openLog();
        ARouter.openDebug();
        ApplicationContextHolder.setApplicationContext(this);
        //test
        insertHabitData();
    }

    //@Test
    private void insertHabitData() {
        Habit habit = new Habit();
        habit.setMainTitle("测试优生打卡主标题");
        habit.setSubTitle("测试优生活打卡付标题");
        habit.setClockTime(1540119126);
        habit.setKeepDays(18);
        habit.setNeedSign(true);
        habit.setSigned(true);
        habit.setSignTime(1520114126);


        Habit habit1 = new Habit();
        habit1.setMainTitle("测试优生打卡主标题1");
        habit1.setSubTitle("测试优生活打卡付标题1");
        habit1.setClockTime(1540119126);
        habit1.setKeepDays(18);
        habit1.setNeedSign(true);
        habit1.setSigned(false);
        habit1.setSignTime(1520114126);

        List<Habit> habits = new ArrayList<>();
        habits.add(habit);
        habits.add(habit1);

        LitePal.saveAll(habits);
    }
}
