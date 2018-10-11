package com.yousheng.yousheng.notify;

import android.app.AlarmManager;
import android.app.DatePickerDialog;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.activities.BaseActivity;
import com.yousheng.yousheng.receiver.AlarmReceiver;
import com.yousheng.yousheng.uitl.ToastUtil;


import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class NewItemActivity extends BaseActivity implements View.OnClickListener {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    private SuperTextView time;
    private SuperTextView notify;

    private EditText content;

    private Context context;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    private boolean legal = true;

    private long id = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itme);
        context = this;

        time = findViewById(R.id.time);
        notify = findViewById(R.id.notify);
        content = findViewById(R.id.content);

        findViewById(R.id.ok).setOnClickListener(this);
        notify.setOnClickListener(this);

        //默认提醒时间是明天早上8点
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        //要更新在start activity时传入要更新的id
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);

        time.setLeftString(format.format(calendar.getTime()));

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                legal = calendar.getTimeInMillis() > System.currentTimeMillis();
                time.setLeftString(format.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);


        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        notify.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b)
                    time.setVisibility(View.VISIBLE);
                else
                    time.setVisibility(View.INVISIBLE);
            }
        });

        //用于测试
        HashMap<NewItemHelper.TimeRange, ArrayList<NewItem>> map = NewItemHelper.getAllNewItemInRange();
        for (NewItemHelper.TimeRange range : map.keySet()) {
            Log.d("NewItemActivity", range + "|" + map.get(range));
        }
    }

    private void addNewItem() {
        String str = content.getText().toString();
        if (TextUtils.isEmpty(str)) {
            ToastUtil.showMsg(context, "请填写提醒内容");
            return;
        }

        if (str.length() > 250) {
            ToastUtil.showMsg(context, "内容不能超过250字符哦");
            return;
        }

        if (!legal) {
            ToastUtil.showMsg(context, "请选择一个将来的时间");
            return;
        }


        long time = 0;
        if (notify.getSwitchIsChecked()) {
            time = calendar.getTimeInMillis();
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(AlarmReceiver.NOTIFY_IN_TIME);
            intent.putExtra("str", str);
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
            am.set(AlarmManager.RTC, time, sender);
        }
        if (id < 0) {
            NewItem item = new NewItem(str, time);
            item.save();
        } else {
            //未测试
            NewItem item = new NewItem(id, str, time);
            item.update(id);
        }
        ToastUtil.showMsg(context, "成功添加提醒事项！");
        finish();
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
        }
    }

}
