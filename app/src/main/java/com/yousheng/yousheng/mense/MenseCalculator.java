package com.yousheng.yousheng.mense;

import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.SPSingleton;

import java.util.Calendar;

/****根据当前时间推算经期，安全期等*/
public class MenseCalculator {
    private final static long ONE_DAY_TS = 24 * 60 * 60 * 1000L;


    /***四种状态*/
    public final static int STATE_NORMAL = 0;
    public final static int STATE_PAILUAN_DURATION = 1;
    public final static int STATE_MENSE = 2;
    public final static int STATE_PAILUAN_DATE = 3;

    /**
     * 是否处于经期内
     */
    public static boolean isInMense(long endDateTs) {
        if (!checkDatelegal(endDateTs)) {
            return false;
        }

        int menseDuration = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DAYS,
                Constants.DEFAULT_MENSE_DURAION));
        int menseGap = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_GAP,
                Constants.DEFAULT_MENSE_GAP));
        Calendar todayCalendar = Calendar.getInstance();
        Calendar temp = Calendar.getInstance();
        temp.set(todayCalendar.get(Calendar.YEAR),
                todayCalendar.get(Calendar.MONTH),
                todayCalendar.get(Calendar.DAY_OF_MONTH),
                0, 0, 0);

        long lastMenseStartTs = SPSingleton.get().getLong(PrefConstants.PREFS_KEY_MENSE_START_DAY,
                temp.getTimeInMillis());
        return (endDateTs - lastMenseStartTs) > 0
                && ((endDateTs - lastMenseStartTs) % ((menseGap) * ONE_DAY_TS) <= menseDuration * ONE_DAY_TS);
    }

    /**
     * 是否正处于排卵日
     */
    public static boolean isPaiLuanDate(long dateTs) {
        if (!checkDatelegal(dateTs)) {
            return false;
        }

        return !isInMense(dateTs + 13 * ONE_DAY_TS) && isInMense(dateTs + 14 * ONE_DAY_TS);
    }

    private static boolean checkDatelegal(long dateTs) {
        if (dateTs < SPSingleton.get().getLong(PrefConstants.PREFS_KEY_MENSE_START_DAY,
                System.currentTimeMillis())) {
            return false;
        }
        return true;
    }


    /***
     * 是否正处于排卵期
     * */
    public static boolean isInPaiLuanDuration(long dateTs) {
        if (!checkDatelegal(dateTs)) {
            return false;
        }

        for (int i = -4; i <= 5; i++) {
            if (isPaiLuanDate((ONE_DAY_TS) * i + dateTs))
                return true;
        }
        return false;
    }


    /***
     * 计算怀孕概率
     * */
    public static int calculateLucky(long dateTs) {
        //处于非经期非排卵期
        if (!isInMense(dateTs) && !isInPaiLuanDuration(dateTs)) return 5;
        //处于经期
        if (isInMense(dateTs)) return 1;
        //处于排卵期
        if (isInPaiLuanDuration(dateTs)) {
            int gapDays;
            for (gapDays = -4; gapDays <= 5; gapDays++) {
                if (isPaiLuanDate(gapDays * ONE_DAY_TS + dateTs))
                    break;
            }
            //好孕率极高
            switch (gapDays) {
                case 0:
                    if ((gapDays * ONE_DAY_TS + dateTs) > 0) {
                        return 32;
                    } else {
                        return 26;
                    }
                case -1:
                    return 27;
                case -2:
                    return 22;
                case -3:
                    return 18;
                case -4:
                    return 15;
                case 1:
                    return 26;
                case 2:
                    return 22;
                case 3:
                    return 18;
                case 4:
                    return 15;
                case 5:
                    return 12;
            }

        }
        return 5;
    }

    /**
     * 获取概率描述
     */
    public static String getPercentDescription(long dateTs) {
        int percent = calculateLucky(dateTs);
        switch (percent) {
            case 1:
                return "极低";
            case 5:
                return "低";
            case 32:
                return "极高";
            default:
                return "高";
        }
    }

    /**
     * 获取当前日期的类型，为安全期，经期，排卵期，排卵日
     */
    public static String getMenseStateString(long dateTs) {
        if (isInMense(dateTs)) return "经期";
        if (isPaiLuanDate(dateTs)) return "排卵日";
        if (isInPaiLuanDuration(dateTs)) return "排卵期";
        return "安全期";
    }

    /**
     * 获取经期，安全期，排卵期等各种类型的int值
     */
    public static int getMenseState(long dateTs) {
        if (dateTs < CalendarUtils.getTodayTimeMillis()) {
            return MenseCalculator.STATE_NORMAL;
        }
        if (isInMense(dateTs)) return MenseCalculator.STATE_MENSE;
        if (isPaiLuanDate(dateTs)) return MenseCalculator.STATE_PAILUAN_DATE;
        if (isInPaiLuanDuration(dateTs)) return MenseCalculator.STATE_PAILUAN_DURATION;
        return MenseCalculator.STATE_NORMAL;
    }
}
