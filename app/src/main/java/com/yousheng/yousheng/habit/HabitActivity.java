package com.yousheng.yousheng.habit;

import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;

public class HabitActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePickerDialog timePickerDialog;
    private EditText content;
    private SuperTextView time;
    private SuperTextView notify;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private long id = -1;

    private Context context;
    private boolean isNotify = false;
    private Habit habit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        context = this;

        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        notify = findViewById(R.id.notify);
        content = findViewById(R.id.content);
        time = findViewById(R.id.time);
        time.setOnClickListener(this);

        time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
        CommonTitleBar titleBar = findViewById(R.id.title);
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);

        //初始化数据
        if (id != -1) {
            habit = LitePal.find(Habit.class, id);
            content.setText(habit.getMainTitle());
            ToastUtil.showMsg(context, "" + habit.getClockTime());
            if (habit.getClockTime() > 0) {
                calendar.setTimeInMillis(habit.getClockTime());
            }
            time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
            if (habit.isNotify()) {
                time.setVisibility(View.VISIBLE);
                notify.setSwitchIsChecked(true);
            }
            isNotify = habit.isNotify();
            String str = habit.isNeedSign() ? "移除" : "添加";
            SuperTextView superTextView = findViewById(R.id.ok);
            superTextView.setCenterString(str.equals("移除") ? "从首页移除" : "添加到首页");
            titleBar.getRightTextView().setText(str);
        } else {
            SuperTextView superTextView = findViewById(R.id.ok);
            superTextView.setCenterString("添加到首页");
            titleBar.getRightTextView().setText("添加");
        }

        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case 1:
                        finish();
                        break;
                    case 3:
                        addNewItem();
                        break;
                }
            }
        });

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
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
        notify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                addNewItem();
                break;
            case R.id.notify:
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                break;
            case R.id.time:
                timePickerDialog.show();
                break;
        }
    }

    private void addNewItem() {
        String str = content.getText().toString();
        if (TextUtils.isEmpty(str) || str.trim().length() == 0) {
            ToastUtil.showMsg(context, "请填写提醒内容");
            return;
        }

        if (str.length() > 150) {
            ToastUtil.showMsg(context, "内容不能超过150字符哦");
            return;
        }


        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DATE, 1);
        long time = calendar.getTimeInMillis();

        Habit habit;
        if (id == -1) {
            habit = new Habit();
            habit.setMainTitle(str);
            habit.setClockTime(time);
            habit.setNotify(isNotify);
            habit.save();
        } else {
            habit = LitePal.find(Habit.class, id);
            habit.setMainTitle(str);
            habit.setClockTime(time);
            habit.setNotify(isNotify);
            habit.save();
        }
        AlarmHelper.notifyHabit(context, habit);
        ToastUtil.showMsg(context, "编辑已保存！");
        finish();
    }
}
