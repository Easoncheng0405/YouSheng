package com.yousheng.yousheng.habit;

import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.model.Record;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;


//打卡activity
public class HoldOnDays extends AppCompatActivity {

    private Habit habit;
    private Record record;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private SuperTextView time;
    private SuperTextView notify;
    private TimePickerDialog timePickerDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hold_on_days);
        long id = getIntent().getLongExtra("id", -1);
        habit = LitePal.find(Habit.class, id);
        record = LitePal.find(Record.class, id);
        //必须传入一个合法的并且从数据库查到此习惯
        if (id == -1 || habit == null) {
            finish();
            return;
        }
        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });
        CommonTitleBar titleBar = findViewById(R.id.title);

        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                finish();
            }
        });

        titleBar.getLeftTextView().setText("  " + habit.getMainTitle());
        ((TextView) findViewById(R.id.title1)).setText(habit.getMainTitle());
        if (habit.isOfficial())
            ((TextView) findViewById(R.id.title2)).setText(habit.getSubTitle());
        if (record == null) {
            record = new Record();
            record.setId(habit.getId());
            record.setDays(0);
            record.setTime(0);
        }
        ((TextView) findViewById(R.id.number)).setText(record.getDays() + "");

        calendar.setTimeInMillis(habit.getClockTime());
        time = findViewById(R.id.time);
        notify = findViewById(R.id.notify);
        time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
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

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
                Habit h = LitePal.find(Habit.class, habit.getId());
                h.setClockTime(calendar.getTimeInMillis());
                h.save();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);

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
    }

    //打卡
    private void record() {
        long millis = System.currentTimeMillis();
        Calendar db = Calendar.getInstance(Locale.CHINA);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        db.setTimeInMillis(record.getTime());
        now.setTimeInMillis(millis);
        if (now.get(Calendar.DAY_OF_YEAR) <= db.get(Calendar.DAY_OF_YEAR)) {
            ToastUtil.showMsg(this, "今天已经打过卡了哦！");
            return;
        }
        //记录上次打卡时间
        record.setTime(millis);
        //打卡+1
        record.setDays(record.getDays() + 1);
        record.save();
        //页面显示+1
        ((TextView) findViewById(R.id.number)).setText(record.getDays() + "");
    }
}
