package com.yousheng.yousheng.habit;

import com.yousheng.yousheng.model.Habit;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HabitHelper {

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
            if (habit.isNeedSign()) {
                if (habit.isOfficial())
                    officialIn.add(habit);
                else
                    customIn.add(habit);
            } else {
                if (habit.isOfficial())
                    officialOut.add(habit);
                else
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
