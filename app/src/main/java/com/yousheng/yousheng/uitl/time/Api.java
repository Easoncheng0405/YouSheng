package com.yousheng.yousheng.uitl.time;


import java.io.InputStream;
import java.util.Date;


public class Api {

    private TimeNormalizer normalizer;

    public Api(InputStream inputStream) throws Exception {
        normalizer = new TimeNormalizer(inputStream);
    }

    public Date[] getDate(String text) {
        normalizer.parse(text);
        TimeUnit[] unit = normalizer.getTimeUnit();
        Date[] dates = new Date[unit.length];
        for (int i = 0; i < dates.length; i++)
            dates[i] = unit[i].getTime();
        return dates;
    }
}
