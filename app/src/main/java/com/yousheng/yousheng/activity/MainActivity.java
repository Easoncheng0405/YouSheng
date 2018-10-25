package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.R;

import com.yousheng.yousheng.adapter.HabitAdapter;
import com.yousheng.yousheng.calendarlib.CalendarView;
import com.yousheng.yousheng.habit.Habit;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.SPSingleton;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /***优生打卡recyclerview*/
    private RecyclerView mRvHabitiList;
    private HabitAdapter mHabitAdapter;

    /****时间选择器, 选择月份*/
    private CustomDatePicker mMonthPicker;

    /****日历控件**/
    private CalendarView mCalendarView;

    /****当前日历控件选中的时间*/
    private String mCurrentSelectedDate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //唤醒所有闹钟
        AlarmHelper.notifyAllAlarm(this);

        init();
        initMember();
    }

    private void initMember() {
        mCurrentSelectedDate = CalendarUtils.formatDateString(System.currentTimeMillis(),
                "yyyy-MM-dd");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SPSingleton.get().getBoolean(PrefConstants.PREFS_KEY_MENSE_SAVED, false)) {
            startActivity(new Intent(this, MenseManagementActivity.class));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_ovulation:
                startActivity(new Intent(this, OvulationActivity.class));
                break;
            case R.id.tv_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.tv_date:
            case R.id.iv_icon_down:
                mMonthPicker.show(mCurrentSelectedDate);
                break;
            default:
                break;
        }
    }

    /**
     * 查询打卡事项的详情
     */
    private void queryHabitData() {
        LitePal.findAllAsync(Habit.class).listen(new FindMultiCallback<Habit>() {
            @Override
            public void onFinish(List<Habit> list) {
                mHabitAdapter = new HabitAdapter(list, MainActivity.this);
                mRvHabitiList.setAdapter(mHabitAdapter);
            }
        });
    }

    /**
     * 初始化activity中的变量，从数据库中查询数据
     */
    private void init() {
        mRvHabitiList = findViewById(R.id.rv_good_habbit);
        mRvHabitiList.setLayoutManager(new LinearLayoutManager(this));
        queryHabitData();

        mCalendarView = findViewById(R.id.calendarView);

        mMonthPicker =
                new CustomDatePicker.Builder()
                        .setContext(this)
                        .setTitle("选择月份")
                        .setStartDate("2010-01-01 00:00")
                        .setEndDate(CalendarUtils.formatDateString(System.currentTimeMillis(), "yyyy-MM-dd hh:mm"))
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String time) {
                                String[] timeUnits = time.split(" ")[0].split("-");
                                int year = Integer.valueOf(timeUnits[0]);
                                int month = Integer.valueOf(timeUnits[1]);
                                mCalendarView.scrollToCalendar(year, month, 1);
                            }
                        })
                        .create();
        mMonthPicker.hideTimeUnit(CustomDatePicker.SCROLL_TYPE.HOUR,
                CustomDatePicker.SCROLL_TYPE.MINUTE);
    }
}
