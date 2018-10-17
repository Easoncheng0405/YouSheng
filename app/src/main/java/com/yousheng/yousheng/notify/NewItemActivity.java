package com.yousheng.yousheng.notify;

import android.app.AlarmManager;
import android.app.DatePickerDialog;

import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import android.util.Log;
import android.view.View;

import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.receiver.AlarmReceiver;
import com.yousheng.yousheng.uitl.ToastUtil;


import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;


public class NewItemActivity extends AppCompatActivity implements View.OnClickListener {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);


    private SuperTextView notify;
    private SuperTextView time;
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

        CommonTitleBar titleBar = findViewById(R.id.title);
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

        time = findViewById(R.id.time);
        notify = findViewById(R.id.notify);
        content = findViewById(R.id.content);

        findViewById(R.id.ok).setOnClickListener(this);
        time.setOnClickListener(this);
        notify.setOnClickListener(this);

        //默认提醒时间是明天早上8点
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        //要更新在start activity时传入要更新的id
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);
        //初始化数据
        if (id != -1) {
            content.setText(intent.getStringExtra("content"));
            long l = intent.getLongExtra("time", 0);
            if (l > 0) {
                calendar.setTimeInMillis(l);
                time.setVisibility(View.VISIBLE);
                notify.setSwitchIsChecked(true);
            }
        }
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

        //用于测试
        HashMap<NewItemHelper.TimeRange, ArrayList<NewItem>> map = NewItemHelper.getAllNewItemInRange();
        for (NewItemHelper.TimeRange range : map.keySet()) {
            Log.d("NewItemActivity", range + "|" + map.get(range));
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

        if (!legal) {
            ToastUtil.showMsg(context, "请选择一个将来的时间");
            return;
        }


        long time = -1;
        if (notify.getSwitchIsChecked()) {
            time = calendar.getTimeInMillis();
        }
        NewItem item;
        if (id == -1) {
            item = new NewItem(str, time);
            item.save();
        } else {
            item = LitePal.find(NewItem.class, id);
            item.setContent(str);
            item.setTime(time);
            item.save();
        }
        AlarmHelper.notifyNewItem(context, item);
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
            case R.id.time:
                datePickerDialog.show();
                break;
        }
    }

}
