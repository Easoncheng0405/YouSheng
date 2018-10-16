package com.yousheng.yousheng.habit;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.receiver.AlarmReceiver;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class HabitActivity extends AppCompatActivity implements View.OnClickListener {

    private TimePickerDialog timePickerDialog;
    private EditText content;
    private SuperTextView time;
    private SuperTextView notify;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private SimpleDateFormat format = new SimpleDateFormat("HH:mm");
    private long id = -1;

    private Context context;

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
        time.setLeftString("每天" + format.format(calendar.getTime()));
        CommonTitleBar titleBar = findViewById(R.id.title);

        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);

        //初始化数据
        if (id != -1) {
            content.setText(intent.getStringExtra("content"));
            Habit habit = LitePal.find(Habit.class, id);
            if (habit.getTime() > 0) {
                calendar.setTimeInMillis(habit.getTime());
                time.setVisibility(View.VISIBLE);
                time.setLeftString("每天" + format.format(calendar.getTime()));
                notify.setSwitchIsChecked(true);
            }
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
                time.setLeftString("每天" + format.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);


        notify.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    time.setVisibility(View.VISIBLE);
                } else {
                    time.setVisibility(View.GONE);
                }

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


        long time = 0;
        if (notify.getSwitchIsChecked()) {
            time = calendar.getTimeInMillis();
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmReceiver.HABIT_IN_TIME);
            intent.putExtra("str", str);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            //todo 这里要改成每日重复闹钟
            am.set(AlarmManager.RTC, time, sender);
        }
        if (id == -1) {
            Habit habit = new Habit();
            habit.setTitle(str);
            habit.setTime(time);
            habit.save();
        } else {
            //未测试
            Habit habit = new Habit(id);
            habit.setTitle(str);
            habit.setTime(time);
            habit.update(id);
        }
        ToastUtil.showMsg(context, "成功添加自定义习惯！");
        finish();
    }
}
