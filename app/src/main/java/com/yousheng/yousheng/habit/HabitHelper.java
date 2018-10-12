package com.yousheng.yousheng.habit;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HabitHelper {

    //官方
    public static final int OFFICIAL = 0;
    //自定义
    public static final int CUSTOM = 1;

    //已加入打卡
    public static final int IN = 0;
    //未加入打卡
    public static final int OUT = 1;

    //返回官方的:1，自定义的:0两种类型下的已加入打开的数组
    public static HashMap<Integer, List<Habit>> getAllInHabit() {
        List<Habit> habits = getAll();

        List<Habit> official = new ArrayList<>();
        List<Habit> custom = new ArrayList<>();

        for (Habit habit : habits) {
            if (habit.getState() == IN) {
                if (habit.getType() == OFFICIAL)
                    official.add(habit);
                if (habit.getType() == CUSTOM)
                    custom.add(habit);
            }
        }

        HashMap<Integer, List<Habit>> map = new HashMap<>();
        map.put(OFFICIAL, official);
        map.put(CUSTOM, custom);
        return map;
    }

    public static List<Habit> getAll() {
        return LitePal.findAll(Habit.class);
    }
}
