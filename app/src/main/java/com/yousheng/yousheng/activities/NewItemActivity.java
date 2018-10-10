package com.yousheng.yousheng.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.ToastUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewItemActivity extends BaseActivity implements View.OnClickListener, CheckBox.OnCheckedChangeListener {

    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);

    private SuperTextView time;
    private SuperTextView repeat;
    private SuperTextView notify;

    private Dialog bottomDialog;
    private Dialog customDialog;

    private Context context;

    private int[] weekend = new int[7];

    private SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_itme);
        context = this;

        time = findViewById(R.id.time);
        notify = findViewById(R.id.notify);
        repeat = findViewById(R.id.repeat);
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

        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRepeatDialog();
            }
        });
    }

    private void showRepeatDialog() {
        bottomDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.notify_select, null);
        bottomDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);

        bottomDialog.findViewById(R.id.once).setOnClickListener(this);
        bottomDialog.findViewById(R.id.everyday).setOnClickListener(this);
        bottomDialog.findViewById(R.id._1to5).setOnClickListener(this);
        bottomDialog.findViewById(R.id.custom).setOnClickListener(this);

        bottomDialog.getWindow().setGravity(Gravity.BOTTOM);
        bottomDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        bottomDialog.show();
    }

    private void showCustomRepeatDialog() {
        customDialog = new Dialog(this, R.style.BottomDialog);
        View contentView = LayoutInflater.from(this).inflate(R.layout.custom_notify, null);
        customDialog.setContentView(contentView);
        ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        contentView.setLayoutParams(layoutParams);
        for (int i = 0; i < weekend.length; i++)
            weekend[i] = 0;
        ((CheckBox) customDialog.findViewById(R.id.w1)).setOnCheckedChangeListener(this);
        ((CheckBox) customDialog.findViewById(R.id.w2)).setOnCheckedChangeListener(this);
        ((CheckBox) customDialog.findViewById(R.id.w3)).setOnCheckedChangeListener(this);
        ((CheckBox) customDialog.findViewById(R.id.w4)).setOnCheckedChangeListener(this);
        ((CheckBox) customDialog.findViewById(R.id.w5)).setOnCheckedChangeListener(this);
        ((CheckBox) customDialog.findViewById(R.id.w6)).setOnCheckedChangeListener(this);
        ((CheckBox) customDialog.findViewById(R.id.w7)).setOnCheckedChangeListener(this);

        customDialog.findViewById(R.id.ok).setOnClickListener(this);

        customDialog.getWindow().setGravity(Gravity.BOTTOM);
        customDialog.getWindow().setWindowAnimations(R.style.BottomDialog_Animation);
        customDialog.show();
    }

    @Override
    public void onClick(View v) {
        String str = "单次提醒";
        switch (v.getId()) {
            case R.id.once:
                bottomDialog.dismiss();
                repeat.setRightString(str);
                break;
            case R.id.everyday:
                str = "每天提醒";
                bottomDialog.dismiss();
                repeat.setRightString(str);
                break;
            case R.id._1to5:
                str = "周一到周五";
                bottomDialog.dismiss();
                repeat.setRightString(str);
                break;
            case R.id.custom:
                bottomDialog.dismiss();
                showCustomRepeatDialog();
                break;
            case R.id.ok:
                customDialog.dismiss();
                repeat.setRightString(getSelectString());
                break;
            default:
                break;
        }

    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.w1:
                weekend[0] = isChecked ? 1 : 0;
                break;
            case R.id.w2:
                weekend[1] = isChecked ? 1 : 0;
                break;
            case R.id.w3:
                weekend[2] = isChecked ? 1 : 0;
                break;
            case R.id.w4:
                weekend[3] = isChecked ? 1 : 0;
                break;
            case R.id.w5:
                weekend[4] = isChecked ? 1 : 0;
                break;
            case R.id.w6:
                weekend[5] = isChecked ? 1 : 0;
                break;
            case R.id.w7:
                weekend[6] = isChecked ? 1 : 0;
                break;
        }
    }

    private String getSelectString() {
        String res = "";
        if (weekend[0] == 1)
            res += "周一 ";
        if (weekend[1] == 1)
            res += "周二 ";
        if (weekend[2] == 1)
            res += "周三 ";
        if (weekend[3] == 1)
            res += "周四 ";
        if (weekend[4] == 1)
            res += "周五 ";
        if (weekend[5] == 1)
            res += "周六 ";
        if (weekend[6] == 1)
            res += "周日 ";
        if (res.equals("周一 周二 周三 周四 周五 周六 周日 "))
            return "每天提醒";
        if (res.equals("周一 周二 周三 周四 周五 "))
            return "周一到周五";
        return res;

    }
}
