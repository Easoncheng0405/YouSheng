package com.yousheng.yousheng.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewItemActivity extends BaseActivity {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    private SuperTextView time;
    private SuperTextView notify;

    private Context context;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itme);
        context=this;

        time = findViewById(R.id.time);
        notify = findViewById(R.id.notify);
        //默认提醒时间是明天早上8点
        calendar.add(Calendar.DATE, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                timePickerDialog.show();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                if (calendar.getTimeInMillis() < System.currentTimeMillis())
                    ToastUtil.showMsg(context, "请选择一个将来的日期");
                else
                    time.setLeftString(format.format(calendar.getTime()));
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);


        time.setLeftString(format.format(calendar.getTime()));

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        notify.setSwitchCheckedChangeListener(new SuperTextView.OnSwitchCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                ToastUtil.showMsg(context, "switch state now is " + b);
            }
        });

    }
}
