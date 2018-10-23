package com.yousheng.yousheng.uitl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CalendarUtils {
    public static String formatDateString(long timeMills, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.CHINA);
        Date date = new Date();
        date.setTime(timeMills);
        return simpleDateFormat.format(date);
    }
}
