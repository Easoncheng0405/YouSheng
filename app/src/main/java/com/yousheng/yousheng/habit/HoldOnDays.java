package com.yousheng.yousheng.habit;

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

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;

import static com.yousheng.yousheng.uitl.TitleBarUtils.changeTitleImageLeftMargin;
import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;


//打卡activity
public class HoldOnDays extends AppCompatActivity {

    private Habit habit;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private SuperTextView time;
    private SuperTextView notify;
    private CustomDatePicker mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hold_on_days);
        long id = getIntent().getLongExtra("id", -1);
        habit = LitePal.find(Habit.class, id);
        //必须传入一个合法的并且从数据库查到此习惯
        if (id == -1 || habit == null) {
            finish();
            return;
        }

        final SuperTextView superTextView = findViewById(R.id.ok);
        superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });
        if (habit.isSigned())
            superTextView.setCenterString("取消打卡");
        else
            superTextView.setCenterString("打卡");

        CommonTitleBar titleBar = findViewById(R.id.title);
        changeTitleImageLeftMargin(this, titleBar);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                finish();
            }
        });

        titleBar.getLeftTextView().setText("  " + habit.getMainTitle());
        ((TextView) findViewById(R.id.title1)).setText(habit.getMainTitle());
        ((TextView) findViewById(R.id.title1)).setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));

        if (habit.isOfficial()) {
            ((TextView) findViewById(R.id.title2)).setText(habit.getSubTitle());
            ((TextView) findViewById(R.id.content)).setText(habit.getContent());
        }
        ((TextView) findViewById(R.id.number)).setText(habit.getKeepDays() + "");

        calendar.setTimeInMillis(habit.getClockTime());
        time = findViewById(R.id.time);
        notify = findViewById(R.id.notify);

        int px = dip2px(this, 10);
        ((SuperTextView) findViewById(R.id.notify)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.time)).getLeftTextView().setPadding(px, 0, 0, 0);
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

        final ScrollView scrollView = findViewById(R.id.scrollView);

        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                if (notify.getSwitchIsChecked())
                    time.setVisibility(View.VISIBLE);
                else
                    time.setVisibility(View.GONE);
                Habit h = LitePal.find(Habit.class, habit.getId());
                h.setNotify(notify.getSwitchIsChecked());
                h.save();
                ViewGroup.LayoutParams params = scrollView.getLayoutParams();
                params.height = (int) ((superTextView.getTop() - notify.getBottom()) * 0.9);
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                scrollView.setLayoutParams(params);
            }
        });

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
            }
        });


        scrollView.post(new Runnable() {
            @Override
            public void run() {
                ViewGroup.LayoutParams params = scrollView.getLayoutParams();
                params.height = (int) ((superTextView.getTop() - notify.getBottom()) * 0.9);
                params.width = WindowManager.LayoutParams.MATCH_PARENT;
                scrollView.setLayoutParams(params);
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

    }

    //打卡
    private void record() {
        if (habit.isSigned()) {
            Habit h = LitePal.find(Habit.class, habit.getId());
            h.setSignTime(1000);
            h.setKeepDays(habit.getKeepDays() - 1);
            h.save();
        } else {
            Habit h = LitePal.find(Habit.class, habit.getId());
            h.setSignTime(System.currentTimeMillis());
            h.setKeepDays(habit.getKeepDays() + 1);
            h.save();
        }
        finish();
    }
}
