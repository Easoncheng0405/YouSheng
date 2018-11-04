package com.yousheng.yousheng.uitl;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {
    public static String formatDateString(long timeMills, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = new Date();
        date.setTime(timeMills);
        return simpleDateFormat.format(date);
    }

    public static long getTodayTimeMillis() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH),
                0, 0);
        return calendar.getTimeInMillis();
    }
}
