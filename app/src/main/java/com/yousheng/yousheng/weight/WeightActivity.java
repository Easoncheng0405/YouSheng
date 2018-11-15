package com.yousheng.yousheng.weight;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.model.Weight;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.ToastUtil;
import com.zjun.widget.RuleView;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;

import static com.yousheng.yousheng.uitl.TitleBarUtils.changeTitleImageLeftMargin;
import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;


public class WeightActivity extends AppCompatActivity {
    private CustomDatePicker mDatePicker;
    private TextView weight;
    private RuleView ruleView;
    private SuperTextView time;
    private SuperTextView notify;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private long id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        id = getIntent().getLongExtra("id", -1);
        if (id == -1) {
            finish();
            return;
        }
        weight = findViewById(R.id.number);
        ruleView = findViewById(R.id.ruler);
        notify = findViewById(R.id.notify);
        time = findViewById(R.id.time);
        int px = dip2px(this, 10);
        ((SuperTextView) findViewById(R.id.notify)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.time)).getLeftTextView().setPadding(px, 0, 0, 0);
        final Habit habit = LitePal.find(Habit.class, id);
        calendar.setTimeInMillis(habit.getClockTime());
        time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
        notify.setSwitchIsChecked(habit.isNotify());
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatePicker.show(CalendarUtils.formatDateString(System.currentTimeMillis(),
                        "yyyy-MM-dd"));
            }
        });

        ruleView.setOnValueChangedListener(new RuleView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                weight.setText(value + "");
            }
        });

        Weight w = LitePal.findLast(Weight.class);
        if (w != null) {
            ruleView.setCurrentValue(w.getWeight());
        }

        final CommonTitleBar titleBar = findViewById(R.id.title);
        changeTitleImageLeftMargin(this, titleBar);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case 1:
                        finish();
                        break;
                }
            }
        });

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
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

                Habit habit = LitePal.find(Habit.class, id);
                habit.setNotify(b);
                habit.save();

            }
        });

        notify.setSwitchIsChecked(habit.isNotify());
        time.setVisibility(habit.isNotify() ? View.VISIBLE : View.GONE);
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                if (notify.getSwitchIsChecked())
                    AlarmHelper.notifyHabit(WeightActivity.this, habit);
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
                                calendar.setTimeInMillis(l);
                                if (l < System.currentTimeMillis())
                                    calendar.add(Calendar.DAY_OF_YEAR, 1);
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

    private void record() {
        Weight weight = LitePal.findLast(Weight.class);
        Habit habit = LitePal.find(Habit.class, getIntent().getLongExtra("id", -1L));
        if (habit == null) {
            ToastUtil.showMsg(this, "发生了一些错误！");
            finish();
            return;
        }
        long millis = System.currentTimeMillis();
        if (weight == null) {
            Weight w = new Weight();
            w.setTime(millis);
            habit.setSignTime(millis);
            w.setWeight(ruleView.getCurrentValue());
            w.save();
            habit.setKeepDays(habit.getKeepDays() + 1);
            habit.save();
            ToastUtil.showMsg(this, "打卡成功！");
            finish();
            return;
        }

        Calendar db = Calendar.getInstance(Locale.CHINA);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        db.setTimeInMillis(weight.getTime());
        now.setTimeInMillis(millis);
        if (now.get(Calendar.DAY_OF_YEAR) <= db.get(Calendar.DAY_OF_YEAR)) {
            weight.setWeight(ruleView.getCurrentValue());
            weight.setTime(millis);
            habit.setSignTime(millis);
            weight.save();
            habit.save();
            ToastUtil.showMsg(this, "体重已更新！");
        } else {
            Weight w = new Weight();
            w.setTime(millis);
            w.setWeight(ruleView.getCurrentValue());
            w.save();
            habit.setKeepDays(habit.getKeepDays() + 1);
            habit.setSignTime(millis);
            habit.save();
            ToastUtil.showMsg(this, "打卡成功！");
        }
        finish();
    }
}
