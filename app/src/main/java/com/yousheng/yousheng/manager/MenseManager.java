package com.yousheng.yousheng.manager;

import com.yousheng.yousheng.ApplicationContextHolder;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.mense.MenseCalculator;
import com.yousheng.yousheng.model.MenseDurationInfo;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.SPSingleton;

import org.litepal.LitePal;

import java.util.List;

public class MenseManager {

    private static void onMenseDurationChanged(long startTs, long endTs, boolean reset) {
        if (reset) {
            SPSingleton.get().putLong(PrefConstants.PREFS_KEY_MENSE_START_DAY,
                    CalendarUtils.getTodayTimeMillis());
//            SPSingleton.get().putString(PrefConstants.PREFS_KEY_MENSE_DAYS,
//                    Constants.DEFAULT_MENSE_DURAION);
        } else {
            String monthOfYear = CalendarUtils.formatDateString(startTs,
                    Constants.DATE_FORMAT_MONTH_OF_YEAR);
            List<MenseDurationInfo> list = LitePal.select()
                    .order("startts asc").find(MenseDurationInfo.class);
            if (list != null && list.size() > 0) {
                for (int i = list.size() - 1; i >= 0; i--) {
                    MenseDurationInfo info = list.get(i);
                    if (info.getStartTs() < (CalendarUtils.getTodayTimeMillis()+Constants.ONE_DAY_IN_TS) || i == 0) {
                        SPSingleton.get().putLong(PrefConstants.PREFS_KEY_MENSE_START_DAY, info.getStartTs());
//                        SPSingleton.get().putString(PrefConstants.PREFS_KEY_MENSE_DAYS,
//                                String.valueOf((endTs - startTs) / Constants.ONE_DAY_IN_TS));
                        break;
                    }
                }
            }
        }
    }

    public static void resetMenseDuration(long dateTs) {
        MenseDurationInfo info = null;
        String monthOfYear = CalendarUtils.formatDateString(dateTs,
                Constants.DATE_FORMAT_MONTH_OF_YEAR);
        List<MenseDurationInfo> list = LitePal.select()
                .where("monthofyear=?", monthOfYear)
                .find(MenseDurationInfo.class);
        if (list != null && list.size() > 0) {
            info = list.get(0);
        }

        if (info == null) {
            info = new MenseDurationInfo();
        }

        info.setStartTs(0);
        info.setEndTs(0);
        info.setStartDate("");
        info.setEndDate("");
        info.save();

        onMenseDurationChanged(0, 0, true);
    }

    /***
     * @param isStartDate true
     * */
    public static void recordMenseDuration(long dateTs, boolean isStartDate) {
        MenseDurationInfo info = null;
        String monthOfYear = CalendarUtils.formatDateString(dateTs,
                Constants.DATE_FORMAT_MONTH_OF_YEAR);
        List<MenseDurationInfo> list = LitePal.select()
                .where("monthofyear=?", monthOfYear)
                .find(MenseDurationInfo.class);
        if (list != null && list.size() > 0) {
            info = list.get(0);
        }

        if (info == null) {
            info = new MenseDurationInfo();
        }

        long menseDaysTs =
                Integer.valueOf(SPSingleton.get().getString(PrefConstants.PREFS_KEY_MENSE_DAYS,
                        Constants.DEFAULT_MENSE_DURAION))
                        * Constants.ONE_DAY_IN_TS;

        if (isStartDate) {
            info.setStartTs(dateTs);
            info.setStartDate(CalendarUtils.formatDateString(dateTs, Constants.DATE_FORMAT));
            if (info.getEndTs() <= 0 || info.getEndTs() <= info.getStartTs()) {
                info.setEndTs(dateTs + menseDaysTs);
                info.setEndDate(CalendarUtils.formatDateString(dateTs + menseDaysTs, Constants.DATE_FORMAT));
            }
        } else {
            info.setEndTs(dateTs);
            info.setEndDate(CalendarUtils.formatDateString(dateTs, Constants.DATE_FORMAT));
            if (info.getStartTs() <= 0 || info.getStartTs() >= info.getEndTs()) {
                info.setStartTs(dateTs - menseDaysTs);
                info.setEndDate(CalendarUtils.formatDateString(dateTs - menseDaysTs, Constants.DATE_FORMAT));
            }
        }
        info.setMonthOfYear(monthOfYear);
        info.save();

        onMenseDurationChanged(info.getStartTs(), info.getEndTs(), false);
    }

    public static void showMenseNotice() {
        if (!SPSingleton.get().getBoolean(PrefConstants.PREFS_KEY_MENSE_NOTIFY, false)) {
            return;
        }
        long todayTimeMillis = CalendarUtils.getTodayTimeMillis();
        long tomorrowTimeMillis = todayTimeMillis + Constants.ONE_DAY_IN_TS;
        long dayAfterTomorrowTimeMillis = tomorrowTimeMillis + Constants.ONE_DAY_IN_TS;

        if (!MenseCalculator.isInMense(todayTimeMillis)) {
            if (MenseCalculator.isInMense(tomorrowTimeMillis) || MenseCalculator.isInMense(dayAfterTomorrowTimeMillis)) {
                AlarmHelper.notifyMenseDay(ApplicationContextHolder.getApplicationContext(),
                        System.currentTimeMillis()+5*1000);
            }
        }
    }
}
