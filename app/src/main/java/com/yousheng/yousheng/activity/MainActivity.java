package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.adapter.NewItemListAdapter;
import com.yousheng.yousheng.adapter.RecyclerViewSpacesItemDecoration;
import com.yousheng.yousheng.habit.AllHabitActivity;
import com.yousheng.yousheng.mense.MenseCalculator;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.R;

import com.yousheng.yousheng.adapter.HabitAdapter;
import com.yousheng.yousheng.calendarlib.Calendar;
import com.yousheng.yousheng.calendarlib.CalendarView;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.model.MenseInfo;
import com.yousheng.yousheng.notify.NewItemActivity;
import com.yousheng.yousheng.notify.NewItemHelper;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.SPSingleton;
import com.yousheng.yousheng.uitl.TextUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    /***优生打卡recyclerview*/
    private RecyclerView mRvHabitiList;
    private HabitAdapter mHabitAdapter;

    /****时间选择器, 选择月份*/
    private CustomDatePicker mMonthPicker;

    /****日历控件**/
    private CalendarView mCalendarView;

    /****顶部的日期**/
    private TextView tvDate;
    /****日期+今天你处于什么时期*/
    private TextView tvPregnantDate;
    /*****怀孕率*/
    private TextView tvPregnantPercent;
    /***怀孕率描述*/
    private TextView tvPregnantDescription;

    /****月经开始，月经结束，make love switch**/
    private SwitchCompat switchMenseStart;
    private SwitchCompat switchMenseEnd;
    private SwitchCompat switchMakeLove;

    /****当前日历控件选中的时间*/
    private String mCurrentSelectedDate;

    /****当前选中的MenseInfo**/
    private MenseInfo mMenseInfoSelected;

    /****当前选中的calendar对象**/
    private Calendar mCalendarSelected;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //唤醒所有闹钟
        AlarmHelper.notifyAllAlarm(this);
        mRvHabitiList = findViewById(R.id.rv_good_habbit);
        mRvHabitiList.setLayoutManager(new LinearLayoutManager(this));

        HashMap<String, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, 0);//top间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.BOTTOM_DECORATION, 25);//底部间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, 10);//左间距

        stringIntegerHashMap.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, 10);//右间距

        mRvHabitiList.addItemDecoration(new RecyclerViewSpacesItemDecoration(stringIntegerHashMap));
        mRvHabitiList.setNestedScrollingEnabled(false);
        mCurrentSelectedDate = CalendarUtils.formatDateString(System.currentTimeMillis(),
                Constants.DATE_FORMAT);

        initCalendarView();
        initRecyclerView();
    }

    private void initRecyclerView() {
        RecyclerView rvToday = findViewById(R.id.rv_todo_today);
        RecyclerView rvTomorrow = findViewById(R.id.rv_todo_tomorrow);
        RecyclerView rvFuture = findViewById(R.id.rv_todo_previous);
        RecyclerView rvNextWeek = findViewById(R.id.rv_todo_next_week);
        RecyclerView rvObselete = findViewById(R.id.rv_todo_obeslete);

        rvToday.setLayoutManager(new LinearLayoutManager(this));
        rvTomorrow.setLayoutManager(new LinearLayoutManager(this));
        rvObselete.setLayoutManager(new LinearLayoutManager(this));
        rvFuture.setLayoutManager(new LinearLayoutManager(this));
        rvNextWeek.setLayoutManager(new LinearLayoutManager(this));

        rvFuture.setItemAnimator(new DefaultItemAnimator());
        rvToday.setItemAnimator(new DefaultItemAnimator());
        rvTomorrow.setItemAnimator(new DefaultItemAnimator());
        rvObselete.setItemAnimator(new DefaultItemAnimator());
        rvNextWeek.setItemAnimator(new DefaultItemAnimator());

        rvToday.setAdapter(new NewItemListAdapter(this, NewItemHelper.TimeRange.TODAY));
        rvTomorrow.setAdapter(new NewItemListAdapter(this, NewItemHelper.TimeRange.TOMORROW));
        rvObselete.setAdapter(new NewItemListAdapter(this, NewItemHelper.TimeRange.UP_TO_DATE));
        rvNextWeek.setAdapter(new NewItemListAdapter(this, NewItemHelper.TimeRange.IN_WEEK));
        rvFuture.setAdapter(new NewItemListAdapter(this, NewItemHelper.TimeRange.FUTURE));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!SPSingleton.get().getBoolean(PrefConstants.PREFS_KEY_MENSE_SAVED, false)) {
            startActivity(new Intent(this, MenseManagementActivity.class));
        }

        if (SPSingleton.get().getBoolean(PrefConstants.PRFS_KEY_MENSE_START_DAY_CHANGED, false)) {
            SPSingleton.get().putBoolean(PrefConstants.PRFS_KEY_MENSE_START_DAY_CHANGED, false);
            mCalendarView.updateMenseInfo();
        }
    }

    @Override
    public void onActivityReenter(int resultCode, Intent data) {
        super.onActivityReenter(resultCode, data);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.RESULT_CODE_MAIN_TO_NEWITEM:
                if (data != null) {
                    int timeRange = data.getIntExtra("timeRange", -1);
                    if (timeRange == NewItemHelper.TimeRange.TODAY.getValue()) {
                        ((NewItemListAdapter) ((RecyclerView) findViewById(R.id.rv_todo_today))
                                .getAdapter()).notifyDataUpdate();
                    } else if (timeRange == NewItemHelper.TimeRange.TOMORROW.getValue()) {
                        ((NewItemListAdapter) ((RecyclerView) findViewById(R.id.rv_todo_tomorrow))
                                .getAdapter()).notifyDataUpdate();
                    } else if (timeRange == NewItemHelper.TimeRange.NO_DATE.getValue()) {
                        ((NewItemListAdapter) ((RecyclerView) findViewById(R.id.rv_todo_no_date))
                                .getAdapter()).notifyDataUpdate();
                    } else if (timeRange == NewItemHelper.TimeRange.UP_TO_DATE.getValue()) {
                        ((NewItemListAdapter) ((RecyclerView) findViewById(R.id.rv_todo_obeslete))
                                .getAdapter()).notifyDataUpdate();
                    } else if (timeRange == NewItemHelper.TimeRange.IN_WEEK.getValue()) {
                        ((NewItemListAdapter) ((RecyclerView) findViewById(R.id.rv_todo_next_week))
                                .getAdapter()).notifyDataUpdate();
                    }
                }
                break;
        }
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
            case R.id.btn_add_new_habbit:
                startActivity(new Intent(this, AllHabitActivity.class));
                break;
            case R.id.btn_add_new_item: {
                startActivityForResult(new Intent(this, NewItemActivity.class),
                        Constants.REQUEST_CODE_MAIN_TO_NEWITEM);
                break;
            }
            case R.id.layout_comment:
                Intent intent = new Intent(this, ReadyActivity.class);
                intent.putExtra("date", mMenseInfoSelected.getDate());
                startActivity(intent);
                break;
            case R.id.layout_pregnant_check:
                startActivity(new Intent(this, PregnantCheckActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 查询打卡事项的详情
     */
    private void queryHabitData() {

        List<Habit> all = LitePal.findAll(Habit.class);
        List<Habit> list = new ArrayList<>();
        for (Habit habit : all) {
            if (habit.isNeedSign())
                list.add(habit);
        }
        mHabitAdapter = new HabitAdapter(list, MainActivity.this);
        mRvHabitiList.setAdapter(mHabitAdapter);


    }

    /**
     * 初始化Calendarview，从数据库中查询数据
     */
    private void initCalendarView() {
        mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnCalendarSelectListener(new CalendarView.OnCalendarSelectListener() {
            @Override
            public void onCalendarOutOfRange(Calendar calendar) {

            }

            @Override
            public void onCalendarSelect(Calendar calendar, boolean isClick) {
                mCalendarSelected = calendar;
                String dateString = CalendarUtils.formatDateString(calendar.getTimeInMillis(), Constants.DATE_FORMAT);
                List<MenseInfo> menseInfos = LitePal
                        .select(null)
                        .where("date = ?", dateString)
                        .find(MenseInfo.class);
                mMenseInfoSelected = (menseInfos != null && menseInfos.size() > 0) ? menseInfos.get(0)
                        : (new MenseInfo());


                updateMenseInfo(calendar.getTimeInMillis());
                mMenseInfoSelected.setDate(dateString);
                mMenseInfoSelected.setDateTs(calendar.getTimeInMillis());
                mMenseInfoSelected.save();
                switchMenseEnd.setChecked(mMenseInfoSelected.isMenseEnd());
                switchMenseStart.setChecked(mMenseInfoSelected.isMenseStart());
                switchMakeLove.setChecked(mMenseInfoSelected.isHasMakeLove());


            }
        });

        mMonthPicker =
                new CustomDatePicker.Builder()
                        .setContext(this)
                        .setTitle("选择月份")
                        .setStartDate("2010-01-01 00:00")
                        .setEndDate(CalendarUtils.formatDateString(System.currentTimeMillis(), "yyyy-MM-dd hh:mm"))
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String time, long timeMills) {
                                String[] timeUnits = time.split(" ")[0].split("-");
                                int year = Integer.valueOf(timeUnits[0]);
                                int month = Integer.valueOf(timeUnits[1]);
                                mCalendarView.scrollToCalendar(year, month, 1);
                            }
                        })
                        .create();
        mMonthPicker.hideTimeUnit(
                CustomDatePicker.SCROLL_TYPE.DAY,
                CustomDatePicker.SCROLL_TYPE.HOUR,
                CustomDatePicker.SCROLL_TYPE.MINUTE);

        tvDate = findViewById(R.id.tv_date);
        tvPregnantDate = findViewById(R.id.tv_pregnant_securedate);
        tvPregnantPercent = findViewById(R.id.tv_pregnant_percent);
        tvPregnantDescription = findViewById(R.id.tv_pregnant_percent_value);

        //initCalendarView switch
        switchMenseStart = findViewById(R.id.switch_menstruation_start);
        switchMenseEnd = findViewById(R.id.switch_menstruation_end);
        switchMakeLove = findViewById(R.id.switch_make_love);

        CompoundButton.OnCheckedChangeListener switchListener =
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (buttonView == switchMenseStart) {
                            mMenseInfoSelected.setMenseStart(isChecked);
                            mMenseInfoSelected.save();
                        } else if (buttonView == switchMenseEnd) {
                            mMenseInfoSelected.setMenseEnd(isChecked);
                            mMenseInfoSelected.save();
                        } else if (buttonView == switchMakeLove) {
                            mMenseInfoSelected.setHasMakeLove(isChecked);
                            mMenseInfoSelected.save();
                            if (mCalendarSelected != null) {
                                mCalendarSelected.setHasMakeLoveToday(isChecked);
                                mCalendarView.update();
                            }
                        }
                    }
                };

        switchMenseStart.setOnCheckedChangeListener(switchListener);
        switchMenseEnd.setOnCheckedChangeListener(switchListener);
        switchMakeLove.setOnCheckedChangeListener(switchListener);
    }

    /***
     * 当日期更新时
     * */
    private void updateMenseInfo(long timeMills) {
        tvDate.setText(CalendarUtils.formatDateString(timeMills, "yyyy年MM月"));
        //right pregnant date
        tvPregnantDate.setText(CalendarUtils.formatDateString(timeMills, "yyyy年MM月dd日\n"));
        tvPregnantDate.append("您今天处于");
        tvPregnantDate
                .append(TextUtils.getSpannableString(getResources().getColor(R.color.text_color_red_theme),
                        MenseCalculator.getMenseStateString(timeMills)));

        //left
        tvPregnantPercent.setText(String.valueOf(MenseCalculator.calculateLucky(timeMills)).concat("%"));
        tvPregnantDescription.setText("好孕率\n");
        tvPregnantDescription.append(TextUtils
                .getSpannableString(getResources().getColor(R.color.text_color_red_theme),
                        MenseCalculator.getPercentDescription(timeMills)));
    }

    @Override
    protected void onStart() {
        super.onStart();
        queryHabitData();
    }
}
