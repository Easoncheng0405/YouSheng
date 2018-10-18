package com.yousheng.yousheng.habit;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HabitDetailActivity extends AppCompatActivity {

    private SuperTextView time;
    private SuperTextView notify;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private TimePickerDialog timePickerDialog;

    private boolean isNotify;

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
            CommonTitleBar titleBar = findViewById(R.id.title);
            titleBar.getLeftTextView().setText("  " + habit.getTitle());
            String str = habit.isRecord() ? "移除" : "添加";
            titleBar.getRightTextView().setText(str);
            SuperTextView superTextView = findViewById(R.id.ok);
            superTextView.setCenterString(str.equals("移除") ? "从首页移除" : "添加到首页");

            time = findViewById(R.id.time);
            notify = findViewById(R.id.notify);
            isNotify = habit.isNotify();
            //初始化数据
            calendar.setTimeInMillis(habit.getTime());
            time.setLeftString("每天" + format.format(calendar.getTime()));
            if (habit.isNotify()) {
                time.setVisibility(View.VISIBLE);
                notify.setSwitchIsChecked(true);
            }
            time.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    timePickerDialog.show();
                }
            });

            notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                }
            });

            time.setLeftString("每天" + format.format(calendar.getTime()));

            timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    calendar.set(Calendar.MINUTE, minute);
                    time.setLeftString("每天" + format.format(calendar.getTime()));
                }
            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

            notify.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    isNotify = b;
                    if (b) {
                        time.setVisibility(View.VISIBLE);
                    } else {
                        time.setVisibility(View.GONE);
                    }

                }
            });

            TextView title1 = findViewById(R.id.title1);
            TextView title2 = findViewById(R.id.title2);
            title1.setText(habit.getTitle());
            title2.setText(habit.getTitle2());
            superTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    update(habit);
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
                            update(habit);
                            break;
                    }
                    finish();
                }
            });
        } catch (Exception e) {
            ToastUtil.showMsg(this, "发生了一些错误！请联系客服！");
            finish();
        }
    }

    private void update(final Habit habit) { ;
        Habit h = LitePal.find(Habit.class, habit.getId());
        h.setRecord(habit.isRecord());
        h.setTime(calendar.getTimeInMillis());
        h.setNotify(isNotify);
        h.save();
    }
}
