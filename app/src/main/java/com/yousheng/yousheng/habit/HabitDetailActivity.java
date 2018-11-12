package com.yousheng.yousheng.habit;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;

import static com.yousheng.yousheng.uitl.TitleBarUtils.changeTitleImageLeftMargin;
import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;

/**
 * 打卡详情页面
 */
@Route(path = "/habitdetail/activity")
public class HabitDetailActivity extends AppCompatActivity {

    private SuperTextView time;
    private SuperTextView notify;
    private CustomDatePicker mDatePicker;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit_detail);

        Intent intent = getIntent();
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        long id = intent.getLongExtra("id", -1);
        if (id == -1)
            finish();
        try {
            final Habit habit = LitePal.find(Habit.class, id);
            final CommonTitleBar titleBar = findViewById(R.id.title);
            changeTitleImageLeftMargin(this, titleBar);
            titleBar.getLeftTextView().setText("  " + habit.getMainTitle());
            String str = habit.isNeedSign() ? "移除" : "添加";
            titleBar.getRightTextView().setText(str);
            final SuperTextView superTextView = findViewById(R.id.ok);
            superTextView.setCenterString(str.equals("移除") ? "从首页移除" : "添加到首页");

            time = findViewById(R.id.time);
            notify = findViewById(R.id.notify);

            int px = dip2px(this, 10);
            ((SuperTextView) findViewById(R.id.notify)).getLeftTextView().setPadding(px, 0, 0, 0);
            ((SuperTextView) findViewById(R.id.time)).getLeftTextView().setPadding(px, 0, 0, 0);

            final ScrollView scrollView = findViewById(R.id.scrollView);

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = scrollView.getLayoutParams();
                    params.height = (int) ((superTextView.getTop() - notify.getBottom()) * 0.9);
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    scrollView.setLayoutParams(params);
                }
            });

            //初始化数据
            calendar.setTimeInMillis(habit.getClockTime());
            time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
            if (habit.isNotify()) {
                time.setVisibility(View.VISIBLE);
                notify.setSwitchIsChecked(true);
            }
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatePicker.show(CalendarUtils.formatDateString(calendar.getTimeInMillis(),
                            "yyyy-MM-dd"));
                }
            });

            notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                }
            });

            time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));


            notify.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    if (b) {
                        time.setVisibility(View.VISIBLE);
                    } else {
                        time.setVisibility(View.GONE);
                    }
                    Habit h = LitePal.find(Habit.class, habit.getId());
                    h.setNotify(b);
                    h.save();
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.LayoutParams params = scrollView.getLayoutParams();
                            params.height = (int) ((superTextView.getTop() - notify.getBottom()) * 0.9);
                            params.width = WindowManager.LayoutParams.MATCH_PARENT;
                            scrollView.setLayoutParams(params);
                        }
                    });
                }
            });

            TextView title1 = findViewById(R.id.title1);
            title1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            TextView title2 = findViewById(R.id.title2);
            TextView content = findViewById(R.id.content);
            title1.setText(habit.getMainTitle());
            title2.setText(habit.getSubTitle());
            content.setText(habit.getContent());
            superTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Habit h = LitePal.find(Habit.class, habit.getId());
                    h.setNeedSign(!habit.isNeedSign());
                    h.save();
                    finish();
                }
            });

            titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
                @Override
                public void onClicked(View v, int action, String extra) {
                    switch (action) {
                        case 1:
                            break;
                        case 3:
                            Habit h = LitePal.find(Habit.class, habit.getId());
                            h.setNeedSign(!habit.isNeedSign());
                            h.save();
                            break;
                    }
                    finish();
                }
            });
            //init datePicker
            mDatePicker =
                    new CustomDatePicker
                            .Builder()
                            .setContext(this)
                            .setStartDate("2010-01-01 00:00")
                            .setEndDate("2100-01-01 23:59")
                            .setTitle("选择一个时间")
                            .setResultHandler(new CustomDatePicker.ResultHandler() {
                                @Override
                                public void handle(String t, long l) {
                                    if (l < System.currentTimeMillis())
                                        l = l + 24 * 60 * 60 * 1000;
                                    calendar.setTimeInMillis(l);
                                    time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
                                    Habit h = LitePal.find(Habit.class, habit.getId());
                                    h.setClockTime(calendar.getTimeInMillis());
                                    h.save();
                                }
                            })
                            .create();
            mDatePicker.showSpecificTime(true);
            mDatePicker.hideTimeUnit(new CustomDatePicker.SCROLL_TYPE[]{CustomDatePicker.SCROLL_TYPE.YEAR, CustomDatePicker.SCROLL_TYPE.MONTH, CustomDatePicker.SCROLL_TYPE.DAY});
        } catch (Exception e) {
            ToastUtil.showMsg(this, "发生了一些错误！请联系客服！");
            e.printStackTrace();
            finish();
        }
    }
}
