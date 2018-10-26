package com.yousheng.yousheng.mense;

import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.uitl.SPSingleton;

/****根据当前时间推算经期，安全期等*/
public class MenseCalculator {
    private final static long ONE_DAY_TS = 24 * 60 * 60L;


    /***四种状态*/
    public final static int STATE_NORMAL = 0;
    public final static int STATE_PAILUAN_DURATION = 1;
    public final static int STATE_MENSE = 2;
    public final static int STATE_PAILUAN_DATE = 3;

    /**
     * 是否处于经期内
     */
    public static boolean isInMense(long endDateTs) {
        int menseDuration = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DAYS,
                Constants.DEFAULT_MENSE_DURAION));
        int menseGap = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DURATION,
                Constants.DEFAULT_MENSE_GAP));
        long lastMenseStartTs = SPSingleton.get().getLong(PrefConstants.PREFS_KEY_MENSE_START_DAY,
                System.currentTimeMillis());


        return (endDateTs - lastMenseStartTs) > 0
                && ((endDateTs - lastMenseStartTs) % ((menseDuration + menseGap) * ONE_DAY_TS) <= menseDuration * ONE_DAY_TS);
    }

    /**
     * 是否正处于排卵日
     */
    public static boolean isPaiLuanDate(long dateTs) {
        int menseDuration = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DAYS,
                Constants.DEFAULT_MENSE_DURAION));
        int menseGap = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DURATION,
                Constants.DEFAULT_MENSE_GAP));
        long lastMenseStartTs = SPSingleton.get().getLong(PrefConstants.PREFS_KEY_MENSE_START_DAY,
                System.currentTimeMillis());

        long nextMenseStartTs = lastMenseStartTs + (menseDuration + menseGap) * ONE_DAY_TS;
        long pailuanStartTs = nextMenseStartTs - 14 * ONE_DAY_TS;

        return dateTs > pailuanStartTs && dateTs < (pailuanStartTs + ONE_DAY_TS);
    }


    /***
     * 是否正处于排卵期
     * */
    public static boolean isInPaiLuanDuration(long dateTs) {
        int menseDuration = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DAYS,
                Constants.DEFAULT_MENSE_DURAION));
        int menseGap = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DURATION,
                Constants.DEFAULT_MENSE_GAP));
        long lastMenseStartTs = SPSingleton.get().getLong(PrefConstants.PREFS_KEY_MENSE_START_DAY,
                System.currentTimeMillis());

        long nextMenseStartTs = lastMenseStartTs + (menseDuration + menseGap) * ONE_DAY_TS;
        long pailuanStartTs = nextMenseStartTs - 14 * ONE_DAY_TS;

        return (dateTs > (pailuanStartTs - 5 * ONE_DAY_TS))
                && (dateTs < (pailuanStartTs + 5 * ONE_DAY_TS));
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
            int menseDuration = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DAYS,
                    Constants.DEFAULT_MENSE_DURAION));
            int menseGap = Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DURATION,
                    Constants.DEFAULT_MENSE_GAP));
            long lastMenseStartTs = SPSingleton.get().getLong(PrefConstants.PREFS_KEY_MENSE_START_DAY,
                    System.currentTimeMillis());

            long nextMenseStartTs = lastMenseStartTs + (menseDuration + menseGap) * ONE_DAY_TS;
            long pailuanStartTs = nextMenseStartTs - 14 * ONE_DAY_TS;
            long gapTs = dateTs - pailuanStartTs;
            //好孕率极高
            switch ((int) (gapTs / ONE_DAY_TS)) {
                case 0:
                    if (gapTs > 0) {
                        return 32;
                    } else {
                        return 26;
                    }
                case 1:
                    return 27;
                case 2:
                    return 22;
                case 3:
                    return 18;
                case 4:
                    return 15;
                case -1:
                    return 22;
                case -2:
                    return 18;
                case -3:
                    return 15;
                case -4:
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
        if (isInPaiLuanDuration(dateTs)) return "排卵期";
        if (isPaiLuanDate(dateTs)) return "排卵日";
        return "安全期";
    }

    /**
     * 获取经期，安全期，排卵期等各种类型的int值
     */
    public static int getMenseState(long dateTs) {
        if (isInMense(dateTs)) return MenseCalculator.STATE_MENSE;
        if (isInPaiLuanDuration(dateTs)) return MenseCalculator.STATE_PAILUAN_DURATION;
        if (isPaiLuanDate(dateTs)) return MenseCalculator.STATE_PAILUAN_DATE;
        return MenseCalculator.STATE_NORMAL;
    }
}
