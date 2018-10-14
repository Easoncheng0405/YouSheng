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

    enum HabitState {
        OFFICIAL_IN,
        OFFICIAL_OUT,
        CUSTOM_IN,
        CUSTOM_OUT,
    }

    //返回官方的所有四种状态下的habit数组
    public static HashMap<HabitState, List<Habit>> getAllHabitInState(List<Habit> habits) {

        List<Habit> officialIn = new ArrayList<>();
        List<Habit> officialOut = new ArrayList<>();
        List<Habit> customIn = new ArrayList<>();
        List<Habit> customOut = new ArrayList<>();

        for (Habit habit : habits) {
            if (habit.getState() == IN) {
                if (habit.getType() == OFFICIAL)
                    officialIn.add(habit);
                if (habit.getType() == CUSTOM)
                    customIn.add(habit);
            } else {
                if (habit.getType() == OFFICIAL)
                    officialOut.add(habit);
                if (habit.getType() == CUSTOM)
                    customOut.add(habit);
            }
        }

        HashMap<HabitState, List<Habit>> map = new HashMap<>();
        map.put(HabitState.OFFICIAL_IN, officialIn);
        map.put(HabitState.OFFICIAL_OUT, officialOut);
        map.put(HabitState.CUSTOM_IN, customIn);
        map.put(HabitState.CUSTOM_OUT, customOut);
        return map;
    }

    public static List<Habit> getAll() {
        return LitePal.findAll(Habit.class);
    }
}
