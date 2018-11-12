package com.yousheng.yousheng.timepickerlib;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.yousheng.yousheng.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * edited by zhaoyang on 2016/9/28.
 */
public class CustomDatePicker {
    /**** 定义结果回调接口**/
    public interface ResultHandler {
        void handle(String time, long timeMills);
    }

    public enum SCROLL_TYPE {
        HOUR(1),
        MINUTE(2),
        YEAR(4),
        MONTH(8),
        DAY(16);

        SCROLL_TYPE(int value) {
            this.value = value;
        }

        public int value;
    }

    public enum DAY_MODE {
        GAP(0),
        DAYS(1);

        DAY_MODE(int value) {
            this.value = value;
        }

        public int value;
    }

    private final static String TAG = "CustomDatePicker";

    private Context context;
    private Dialog datePickerDialog;
    private DatePickerView year_pv, month_pv, day_pv, hour_pv, minute_pv;
    private TextView tv_title, tv_cancle, tv_select, year_text, month_text, day_text, hour_text, minute_text;
    private Calendar selectedCalender;
    private Calendar startCalendar;
    private Calendar endCalendar;

    private ResultHandler handler;
    private int scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
    private boolean canAccess = false;

    /***日期天数选择**/
    private boolean isDayModeOn = false;

    /***日期选择模式：周期天数 | 月经天数**/
    private DAY_MODE dayMode = DAY_MODE.GAP;

    /***日期模式下选中的天数(经期模式下为 21 ~ 40 天)*/
    private String selectedDaysInDayMode = "21";

    private static final int MAX_MINUTE = 59;
    private static final int MAX_HOUR = 23;
    private static final int MIN_MINUTE = 0;
    private static final int MIN_HOUR = 0;
    private static final int MAX_MONTH = 12;

    private ArrayList<String> yearList, monthList, dayList, hourList, minuteList;

    private int themeColor;
    private int startYear, startMonth, startDay, startHour, startMinute, endYear, endMonth, endDay, endHour, endMinute;
    private boolean spanYear, spanMon, spanDay, spanHour, spanMin;

    /***标题**/
    private String windowTitle;


    public DAY_MODE getDayMode() {
        return dayMode;
    }

    public void setDayMode(DAY_MODE daMode) {
        this.dayMode = daMode;
    }


    public boolean isDayModeOn() {
        return isDayModeOn;
    }

    public void setDayModeOn(boolean dayModeOn) {
        isDayModeOn = dayModeOn;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public void setWindowTitle(String windowTitle) {
        this.windowTitle = windowTitle;
    }

    public Calendar getSelectedCalender() {
        return selectedCalender;
    }

    public void setSelectedCalender(Calendar selectedCalender) {
        this.selectedCalender = selectedCalender;
    }

    public int getThemeColor() {
        return themeColor;
    }

    public void setThemeColor(int themeColor) {
        this.themeColor = themeColor;
    }

    public Calendar getStartCalendar() {
        return startCalendar;
    }

    public void setStartCalendar(String startDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        this.startCalendar = Calendar.getInstance();
        try {
            this.startCalendar.setTime(sdf.parse(startDate));
            canAccess = true;
        } catch (Exception e) {
            canAccess = false;
            e.printStackTrace();
        }
    }

    public Calendar getEndCalendar() {
        return endCalendar;
    }

    public void setEndCalendar(String endDate) {
//        if (isValidDate(endDate, "yyyy-MM-dd HH:mm")) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
        this.endCalendar = Calendar.getInstance();
        try {
            this.endCalendar.setTime(sdf.parse(endDate));
            canAccess = true;
        } catch (Exception e) {
            canAccess = false;
            e.printStackTrace();
        }
//        }
    }

    public ResultHandler getHandler() {
        return handler;
    }

    public void setHandler(ResultHandler handler) {
        this.handler = handler;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public CustomDatePicker() {
    }

    public CustomDatePicker(Context context, ResultHandler resultHandler, String startDate, String endDate) {
        if (isValidDate(startDate, "yyyy-MM-dd HH:mm") && isValidDate(endDate, "yyyy-MM-dd HH:mm")) {
            canAccess = true;
            this.context = context;
            this.handler = resultHandler;
            selectedCalender = Calendar.getInstance();
            startCalendar = Calendar.getInstance();
            endCalendar = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
            try {
                startCalendar.setTime(sdf.parse(startDate));
                endCalendar.setTime(sdf.parse(endDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            initDialog();
            initView();
        }
    }

    private void initDialog() {
        if (datePickerDialog == null) {
            datePickerDialog = new Dialog(context, R.style.date_picker_time_dialog);
            datePickerDialog.setCancelable(true);
            datePickerDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            datePickerDialog.setContentView(R.layout.custom_date_picker);
            Window window = datePickerDialog.getWindow();
            window.setGravity(Gravity.BOTTOM);
            WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            DisplayMetrics dm = new DisplayMetrics();
            manager.getDefaultDisplay().getMetrics(dm);
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = dm.widthPixels;
            window.setAttributes(lp);
        }
    }

    private void initView() {
        year_pv = (DatePickerView) datePickerDialog.findViewById(R.id.year_pv);
        month_pv = (DatePickerView) datePickerDialog.findViewById(R.id.month_pv);
        day_pv = (DatePickerView) datePickerDialog.findViewById(R.id.day_pv);
        hour_pv = (DatePickerView) datePickerDialog.findViewById(R.id.hour_pv);
        minute_pv = (DatePickerView) datePickerDialog.findViewById(R.id.minute_pv);
        tv_cancle = (TextView) datePickerDialog.findViewById(R.id.tv_cancle);
        tv_select = (TextView) datePickerDialog.findViewById(R.id.tv_select);
        hour_text = (TextView) datePickerDialog.findViewById(R.id.hour_text);
        minute_text = (TextView) datePickerDialog.findViewById(R.id.minute_text);
        year_text = (TextView) datePickerDialog.findViewById(R.id.year_text);
        month_text = (TextView) datePickerDialog.findViewById(R.id.month_text);
        day_text = (TextView) datePickerDialog.findViewById(R.id.day_text);
        hour_text = (TextView) datePickerDialog.findViewById(R.id.hour_text);
        tv_title = (TextView) datePickerDialog.findViewById(R.id.tv_title);
        tv_title.setText(windowTitle);

        tv_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog.dismiss();
            }
        });

        tv_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isDayModeOn) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
                    handler.handle(sdf.format(selectedCalender.getTime()),
                            selectedCalender.getTime().getTime());
                } else {
                    if (handler != null) {
                        handler.handle(selectedDaysInDayMode,
                                selectedCalender.getTime().getTime());
                    }
                }
                datePickerDialog.dismiss();
            }
        });

        if (isDayModeOn()) {
            year_pv.setVisibility(View.GONE);
            year_text.setVisibility(View.GONE);
            month_pv.setVisibility(View.GONE);
            month_text.setVisibility(View.GONE);
            day_text.setVisibility(View.GONE);
            hour_pv.setVisibility(View.GONE);
            hour_text.setVisibility(View.GONE);
            minute_pv.setVisibility(View.GONE);
            minute_text.setVisibility(View.GONE);
        }
    }

    private void initParameter() {
        if (!isDayModeOn) {
            startYear = startCalendar.get(Calendar.YEAR);
            startMonth = startCalendar.get(Calendar.MONTH) + 1;
            startDay = startCalendar.get(Calendar.DAY_OF_MONTH);
            startHour = startCalendar.get(Calendar.HOUR_OF_DAY);
            startMinute = startCalendar.get(Calendar.MINUTE);
            endYear = endCalendar.get(Calendar.YEAR);
            endMonth = endCalendar.get(Calendar.MONTH) + 1;
            endDay = endCalendar.get(Calendar.DAY_OF_MONTH);
            endHour = endCalendar.get(Calendar.HOUR_OF_DAY);
            endMinute = endCalendar.get(Calendar.MINUTE);
            spanYear = startYear != endYear;
            spanMon = (!spanYear) && (startMonth != endMonth);
            spanDay = (!spanMon) && (startDay != endDay);
            spanHour = (!spanDay) && (startHour != endHour);
            spanMin = (!spanHour) && (startMinute != endMinute);
            selectedCalender.setTime(startCalendar.getTime());
        }
    }

    private void initTimer() {
        initArrayList();
        if (!isDayModeOn) {
            if (spanYear) {
                for (int i = startYear; i <= endYear; i++) {
                    yearList.add(String.valueOf(i));
                }
                for (int i = startMonth; i <= MAX_MONTH; i++) {
                    monthList.add(formatTimeUnit(i));
                }

                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    dayList.add(formatTimeUnit(i));
                }

                if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                    hourList.add(formatTimeUnit(startHour));
                } else {
                    for (int i = startHour; i <= MAX_HOUR; i++) {
                        hourList.add(formatTimeUnit(i));
                    }
                }

                if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                    minuteList.add(formatTimeUnit(startMinute));
                } else {
                    for (int i = startMinute; i <= MAX_MINUTE; i++) {
                        minuteList.add(formatTimeUnit(i));
                    }
                }
            } else if (spanMon) {
                yearList.add(String.valueOf(startYear));
                for (int i = startMonth; i <= endMonth; i++) {
                    monthList.add(formatTimeUnit(i));
                }
                for (int i = startDay; i <= startCalendar.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                    dayList.add(formatTimeUnit(i));
                }

                if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                    hourList.add(formatTimeUnit(startHour));
                } else {
                    for (int i = startHour; i <= MAX_HOUR; i++) {
                        hourList.add(formatTimeUnit(i));
                    }
                }

                if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                    minuteList.add(formatTimeUnit(startMinute));
                } else {
                    for (int i = startMinute; i <= MAX_MINUTE; i++) {
                        minuteList.add(formatTimeUnit(i));
                    }
                }
            } else if (spanDay) {
                yearList.add(String.valueOf(startYear));
                monthList.add(formatTimeUnit(startMonth));
                for (int i = startDay; i <= endDay; i++) {
                    dayList.add(formatTimeUnit(i));
                }

                if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                    hourList.add(formatTimeUnit(startHour));
                } else {
                    for (int i = startHour; i <= MAX_HOUR; i++) {
                        hourList.add(formatTimeUnit(i));
                    }
                }

                if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                    minuteList.add(formatTimeUnit(startMinute));
                } else {
                    for (int i = startMinute; i <= MAX_MINUTE; i++) {
                        minuteList.add(formatTimeUnit(i));
                    }
                }
            } else if (spanHour) {
                yearList.add(String.valueOf(startYear));
                monthList.add(formatTimeUnit(startMonth));
                dayList.add(formatTimeUnit(startDay));

                if ((scrollUnits & SCROLL_TYPE.HOUR.value) != SCROLL_TYPE.HOUR.value) {
                    hourList.add(formatTimeUnit(startHour));
                } else {
                    for (int i = startHour; i <= endHour; i++) {
                        hourList.add(formatTimeUnit(i));
                    }
                }

                if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                    minuteList.add(formatTimeUnit(startMinute));
                } else {
                    for (int i = startMinute; i <= MAX_MINUTE; i++) {
                        minuteList.add(formatTimeUnit(i));
                    }
                }
            } else if (spanMin) {
                yearList.add(String.valueOf(startYear));
                monthList.add(formatTimeUnit(startMonth));
                dayList.add(formatTimeUnit(startDay));
                hourList.add(formatTimeUnit(startHour));

                if ((scrollUnits & SCROLL_TYPE.MINUTE.value) != SCROLL_TYPE.MINUTE.value) {
                    minuteList.add(formatTimeUnit(startMinute));
                } else {
                    for (int i = startMinute; i <= endMinute; i++) {
                        minuteList.add(formatTimeUnit(i));
                    }
                }
            }
        } else {
            if (getDayMode().value == DAY_MODE.GAP.value) {
                for (int i = 21; i <= 40; i++) {
                    dayList.add(formatTimeUnit(i));
                }
            } else {
                for (int i = 3; i <= 7; i++) {
                    dayList.add(formatTimeUnit(i));
                }
            }
        }
        loadComponent();
    }

    /**
     * 将“0-9”转换为“00-09”
     */
    private String formatTimeUnit(int unit) {
//        return unit < 10 ? "0" + String.valueOf(unit) : String.valueOf(unit);
        return String.valueOf(unit);
    }

    private void initArrayList() {
        if (yearList == null) yearList = new ArrayList<>();
        if (monthList == null) monthList = new ArrayList<>();
        if (dayList == null) dayList = new ArrayList<>();
        if (hourList == null) hourList = new ArrayList<>();
        if (minuteList == null) minuteList = new ArrayList<>();
        yearList.clear();
        monthList.clear();
        dayList.clear();
        hourList.clear();
        minuteList.clear();
    }

    private void loadComponent() {
        year_pv.setData(yearList);
        month_pv.setData(monthList);
        day_pv.setData(dayList);
        hour_pv.setData(hourList);
        minute_pv.setData(minuteList);
        year_pv.setSelected(0);
        month_pv.setSelected(0);
        day_pv.setSelected(0);
        hour_pv.setSelected(0);
        minute_pv.setSelected(0);
        executeScroll();
    }

    private void addListener() {
        year_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(text));
                monthChange();
            }
        });

        month_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.DAY_OF_MONTH, 1);
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(text) - 1);
                dayChange();
            }
        });

        day_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                if (!isDayModeOn) {
                    selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(text));
                    hourChange();
                } else {
                    selectedDaysInDayMode = text;
                }
            }
        });

        hour_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(text));
                minuteChange();
            }
        });

        minute_pv.setOnSelectListener(new DatePickerView.onSelectListener() {
            @Override
            public void onSelect(String text) {
                selectedCalender.set(Calendar.MINUTE, Integer.parseInt(text));
            }
        });
    }

    private void monthChange() {
        monthList.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        if (selectedYear == startYear) {
            for (int i = startMonth; i <= MAX_MONTH; i++) {
                monthList.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear) {
            for (int i = 1; i <= endMonth; i++) {
                monthList.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= MAX_MONTH; i++) {
                monthList.add(formatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.MONTH, Integer.parseInt(monthList.get(0)) - 1);
        month_pv.setData(monthList);
        month_pv.setSelected(0);
        executeAnimator(month_pv);

        month_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                dayChange();
            }
        }, 100);
    }

    private void dayChange() {
        dayList.clear();
        int selectedYear = selectedCalender.get(Calendar.YEAR);
        int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
        if (selectedYear == startYear && selectedMonth == startMonth) {
            for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                dayList.add(formatTimeUnit(i));
            }
        } else if (selectedYear == endYear && selectedMonth == endMonth) {
            for (int i = 1; i <= endDay; i++) {
                dayList.add(formatTimeUnit(i));
            }
        } else {
            for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                dayList.add(formatTimeUnit(i));
            }
        }
        selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dayList.get(0)));
        day_pv.setData(dayList);
        day_pv.setSelected(0);
        executeAnimator(day_pv);

        day_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                hourChange();
            }
        }, 100);
    }

    private void hourChange() {
        if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
            hourList.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                for (int i = startHour; i <= MAX_HOUR; i++) {
                    hourList.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                for (int i = MIN_HOUR; i <= endHour; i++) {
                    hourList.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                    hourList.add(formatTimeUnit(i));
                }
            }
            selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourList.get(0)));
            hour_pv.setData(hourList);
            hour_pv.setSelected(0);
            executeAnimator(hour_pv);
        }

        hour_pv.postDelayed(new Runnable() {
            @Override
            public void run() {
                minuteChange();
            }
        }, 100);
    }

    private void minuteChange() {
        if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
            minuteList.clear();
            int selectedYear = selectedCalender.get(Calendar.YEAR);
            int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
            int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
            int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
            if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                for (int i = startMinute; i <= MAX_MINUTE; i++) {
                    minuteList.add(formatTimeUnit(i));
                }
            } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                for (int i = MIN_MINUTE; i <= endMinute; i++) {
                    minuteList.add(formatTimeUnit(i));
                }
            } else {
                for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                    minuteList.add(formatTimeUnit(i));
                }
            }
            selectedCalender.set(Calendar.MINUTE, Integer.parseInt(minuteList.get(0)));
            minute_pv.setData(minuteList);
            minute_pv.setSelected(0);
            executeAnimator(minute_pv);
        }
        executeScroll();
    }

    private void executeAnimator(View view) {
        PropertyValuesHolder pvhX = PropertyValuesHolder.ofFloat("alpha", 1f, 0f, 1f);
        PropertyValuesHolder pvhY = PropertyValuesHolder.ofFloat("scaleX", 1f, 1.3f, 1f);
        PropertyValuesHolder pvhZ = PropertyValuesHolder.ofFloat("scaleY", 1f, 1.3f, 1f);
        ObjectAnimator.ofPropertyValuesHolder(view, pvhX, pvhY, pvhZ).setDuration(200).start();
    }

    private void executeScroll() {
        year_pv.setCanScroll(yearList.size() > 1);
        month_pv.setCanScroll(monthList.size() > 1);
        day_pv.setCanScroll(dayList.size() > 1);
        hour_pv.setCanScroll(hourList.size() > 1 && (scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value);
        minute_pv.setCanScroll(minuteList.size() > 1 && (scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value);
    }

    private int disScrollUnit(SCROLL_TYPE... scroll_types) {
        if (scroll_types == null || scroll_types.length == 0) {
            scrollUnits = SCROLL_TYPE.HOUR.value + SCROLL_TYPE.MINUTE.value;
        } else {
            for (SCROLL_TYPE scroll_type : scroll_types) {
                scrollUnits ^= scroll_type.value;
            }
        }
        return scrollUnits;
    }

    public void show(String time) {
        if ((canAccess && isValidDate(time, "yyyy-MM-dd")
                && (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()))
                ||
                isDayModeOn()) {
            initParameter();
            initTimer();
            addListener();
            setSelectedTime(time);
            datePickerDialog.show();
        } else {
            canAccess = false;
        }

      /*  if (canAccess) {
            if (isValidDate(com.yousheng.yousheng.uitl.time, "yyyy-MM-dd")) {
                if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
                    canAccess = true;
                    initParameter();
                    initTimer();
                    addListener();
                    setSelectedTime(com.yousheng.yousheng.uitl.time);
                    datePickerDialog.show();
                }
            } else {
                canAccess = false;
            }
        }*/
    }

    public void show(String time, String title) {
        if (canAccess) {
            if (isValidDate(time, "yyyy-MM-dd")) {
                if (startCalendar.getTime().getTime() < endCalendar.getTime().getTime()) {
                    canAccess = true;
                    if (!TextUtils.isEmpty(time)) {
                        tv_title.setText(title);
                    }
                    initParameter();
                    initTimer();
                    addListener();
                    setSelectedTime(time);
                    datePickerDialog.show();
                }
            } else {
                canAccess = false;
            }
        }
    }

    /**
     * 隐藏年月日时分秒中的若干项
     *
     * @param scroll_types 滚动列表的types
     */
    public void hideTimeUnit(SCROLL_TYPE... scroll_types) {
        if (scroll_types == null || scroll_types.length == 0) {
            scrollUnits =
                    SCROLL_TYPE.YEAR.value +
                            SCROLL_TYPE.MONTH.value +
                            SCROLL_TYPE.DAY.value +
                            SCROLL_TYPE.HOUR.value +
                            SCROLL_TYPE.MINUTE.value;
        } else {
            for (SCROLL_TYPE scroll_type : scroll_types) {
                scrollUnits ^= scroll_type.value;
            }
        }

        for (SCROLL_TYPE type : scroll_types) {
            if (SCROLL_TYPE.YEAR.equals(type)) {
                year_pv.setVisibility(View.GONE);
                year_text.setVisibility(View.GONE);
            } else if (SCROLL_TYPE.MONTH.equals(type)) {
                month_pv.setVisibility(View.GONE);
                month_text.setVisibility(View.GONE);
            } else if (SCROLL_TYPE.DAY.equals(type)) {
                day_pv.setVisibility(View.GONE);
                day_text.setVisibility(View.GONE);
            } else if (SCROLL_TYPE.HOUR.equals(type)) {
                hour_pv.setVisibility(View.GONE);
                hour_text.setVisibility(View.GONE);
            } else if (SCROLL_TYPE.MINUTE.equals(type)) {
                minute_pv.setVisibility(View.GONE);
                minute_text.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置日期控件是否显示时和分
     */
    public void showSpecificTime(boolean show) {
        if (canAccess) {
            if (show) {
                disScrollUnit();
                hour_pv.setVisibility(View.VISIBLE);
                hour_text.setVisibility(View.VISIBLE);
                minute_pv.setVisibility(View.VISIBLE);
                minute_text.setVisibility(View.VISIBLE);
            } else {
                disScrollUnit(SCROLL_TYPE.HOUR, SCROLL_TYPE.MINUTE);
                hour_pv.setVisibility(View.GONE);
                hour_text.setVisibility(View.GONE);
                minute_pv.setVisibility(View.GONE);
                minute_text.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 设置日期控件是否可以循环滚动
     */
    public void setIsLoop(boolean isLoop) {
        if (canAccess) {
            this.year_pv.setIsLoop(isLoop);
            this.month_pv.setIsLoop(isLoop);
            this.day_pv.setIsLoop(isLoop);
            this.hour_pv.setIsLoop(isLoop);
            this.minute_pv.setIsLoop(isLoop);
        }
    }


    /**
     * 设置日期控件默认选中的时间
     */
    public void setSelectedTime(String time) {
        if (!isDayModeOn) {
            if (canAccess) {
                String[] str = time.split(" ");
                String[] dateStr = str[0].split("-");

                year_pv.setSelected(dateStr[0]);
                selectedCalender.set(Calendar.YEAR, Integer.parseInt(dateStr[0]));

                monthList.clear();
                int selectedYear = selectedCalender.get(Calendar.YEAR);
                if (selectedYear == startYear) {
                    for (int i = startMonth; i <= MAX_MONTH; i++) {
                        monthList.add(formatTimeUnit(i));
                    }
                } else if (selectedYear == endYear) {
                    for (int i = 1; i <= endMonth; i++) {
                        monthList.add(formatTimeUnit(i));
                    }
                } else {
                    for (int i = 1; i <= MAX_MONTH; i++) {
                        monthList.add(formatTimeUnit(i));
                    }
                }
                month_pv.setData(monthList);
                month_pv.setSelected(dateStr[1]);
                selectedCalender.set(Calendar.MONTH, Integer.parseInt(dateStr[1]) - 1);
                executeAnimator(month_pv);

                dayList.clear();
                int selectedMonth = selectedCalender.get(Calendar.MONTH) + 1;
                if (selectedYear == startYear && selectedMonth == startMonth) {
                    for (int i = startDay; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        dayList.add(formatTimeUnit(i));
                    }
                } else if (selectedYear == endYear && selectedMonth == endMonth) {
                    for (int i = 1; i <= endDay; i++) {
                        dayList.add(formatTimeUnit(i));
                    }
                } else {
                    for (int i = 1; i <= selectedCalender.getActualMaximum(Calendar.DAY_OF_MONTH); i++) {
                        dayList.add(formatTimeUnit(i));
                    }
                }
                day_pv.setData(dayList);
                day_pv.setSelected(dateStr[2]);
                selectedCalender.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr[2]));
                executeAnimator(day_pv);

                if (str.length == 2) {
                    String[] timeStr = str[1].split(":");

                    if ((scrollUnits & SCROLL_TYPE.HOUR.value) == SCROLL_TYPE.HOUR.value) {
                        hourList.clear();
                        int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
                        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay) {
                            for (int i = startHour; i <= MAX_HOUR; i++) {
                                hourList.add(formatTimeUnit(i));
                            }
                        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay) {
                            for (int i = MIN_HOUR; i <= endHour; i++) {
                                hourList.add(formatTimeUnit(i));
                            }
                        } else {
                            for (int i = MIN_HOUR; i <= MAX_HOUR; i++) {
                                hourList.add(formatTimeUnit(i));
                            }
                        }
                        hour_pv.setData(hourList);
                        hour_pv.setSelected(timeStr[0]);
                        selectedCalender.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr[0]));
                        executeAnimator(hour_pv);
                    }

                    if ((scrollUnits & SCROLL_TYPE.MINUTE.value) == SCROLL_TYPE.MINUTE.value) {
                        minuteList.clear();
                        int selectedDay = selectedCalender.get(Calendar.DAY_OF_MONTH);
                        int selectedHour = selectedCalender.get(Calendar.HOUR_OF_DAY);
                        if (selectedYear == startYear && selectedMonth == startMonth && selectedDay == startDay && selectedHour == startHour) {
                            for (int i = startMinute; i <= MAX_MINUTE; i++) {
                                minuteList.add(formatTimeUnit(i));
                            }
                        } else if (selectedYear == endYear && selectedMonth == endMonth && selectedDay == endDay && selectedHour == endHour) {
                            for (int i = MIN_MINUTE; i <= endMinute; i++) {
                                minuteList.add(formatTimeUnit(i));
                            }
                        } else {
                            for (int i = MIN_MINUTE; i <= MAX_MINUTE; i++) {
                                minuteList.add(formatTimeUnit(i));
                            }
                        }
                        minute_pv.setData(minuteList);
                        minute_pv.setSelected(timeStr[1]);
                        selectedCalender.set(Calendar.MINUTE, Integer.parseInt(timeStr[1]));
                        executeAnimator(minute_pv);
                    }
                }
                executeScroll();
            }
        } else {
            int selectedDayInDayMode = Integer.valueOf(time);
            int index = 0;
            if (getDayMode().value == DAY_MODE.GAP.value) {
                if (selectedDayInDayMode < 21 || selectedDayInDayMode > 40) {
                    selectedDayInDayMode = 21;
                }

            } else {
                if (selectedDayInDayMode < 3 || selectedDayInDayMode > 7) {
                    selectedDayInDayMode = 3;
                }
            }

            for (int i = 0; i < dayList.size(); i++) {
                int day = Integer.valueOf(dayList.get(i));
                if (day == selectedDayInDayMode) {
                    index = i;
                    break;
                }
            }

            day_pv.setData(dayList);
            day_pv.setSelected(index);
            selectedCalender.set(Calendar.DAY_OF_MONTH, selectedDayInDayMode);
            executeAnimator(day_pv);
            executeScroll();
        }
    }

    /**
     * 验证字符串是否是一个合法的日期格式
     */
    private boolean isValidDate(String date, String template) {
        boolean convertSuccess = true;
        // 指定日期格式
        SimpleDateFormat format = new SimpleDateFormat(template, Locale.CHINA);
        try {
            // 设置lenient为false. 否则SimpleDateFormat会比较宽松地验证日期，比如2015/02/29会被接受，并转换成2015/03/01
            format.setLenient(false);
            format.parse(date);
        } catch (Exception e) {
            // 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
            convertSuccess = false;
        }
        return convertSuccess;
    }

    public static class Builder {
        private ResultHandler resultHandler;
        private String startDate;
        private String endDate;
        private String title;
        private int themeColor;
        private Context context;
        private boolean isDayModeOn;
        private DAY_MODE dayMode = DAY_MODE.GAP;


        public Builder setDayMode(DAY_MODE dayMode) {
            this.dayMode = dayMode;
            return this;
        }

        public Builder setDayModeOn(boolean dayModeOn) {
            isDayModeOn = dayModeOn;
            return this;
        }

        public Builder setContext(Context context) {
            this.context = context;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setColor(int color) {
            this.themeColor = color;
            return this;
        }

        public Builder setResultHandler(ResultHandler handler) {
            this.resultHandler = handler;
            return this;
        }

        public Builder setStartDate(String date) {
            this.startDate = date;
            return this;
        }

        public Builder setEndDate(String date) {
            this.endDate = date;
            return this;
        }

        public CustomDatePicker create() {
            CustomDatePicker datePicker = new CustomDatePicker();
            datePicker.setContext(context);
            datePicker.setStartCalendar(startDate);
            datePicker.setEndCalendar(endDate);
            datePicker.setSelectedCalender(Calendar.getInstance());
            datePicker.setHandler(resultHandler);
            datePicker.setThemeColor(themeColor);
            datePicker.setWindowTitle(title);
            datePicker.setDayModeOn(isDayModeOn);
            datePicker.setDayMode(dayMode);

            datePicker.initDialog();
            datePicker.initView();
            return datePicker;
        }

    }

}
