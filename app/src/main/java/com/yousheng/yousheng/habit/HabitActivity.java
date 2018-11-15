package com.yousheng.yousheng.habit;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;

import static com.yousheng.yousheng.timepickerlib.CustomDatePicker.SCROLL_TYPE;

import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.SPSingleton;
import com.yousheng.yousheng.uitl.TextUtils;
import com.yousheng.yousheng.uitl.ToastUtil;
import com.yousheng.yousheng.uitl.time.Api;

import org.litepal.LitePal;

import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;

public class HabitActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText content;
    private SuperTextView time;
    private SuperTextView notify;
    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private long id = -1;

    private Context context;
    private boolean isNotify = false;
    private Habit habit;

    private TextView t1, t2;
    private boolean needSign;
    private Api api;
    private CustomDatePicker mDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);
        try {
            AssetManager manager = getAssets();
            InputStream inputStream = manager.open("TimeExp.m");
            api = new Api(inputStream);
        } catch (Exception e) {
            Log.e("HabitActivity", "onCreate:  exception ", e);
        }
        context = this;
        ((TextView) findViewById(R.id.title)).setCompoundDrawablePadding(dip2px(context, 25));

        t1 = findViewById(R.id.t1);
        t2 = findViewById(R.id.t2);
        calendar.set(Calendar.HOUR_OF_DAY, 8);
        calendar.set(Calendar.MINUTE, 0);
        notify = findViewById(R.id.notify);

        content = findViewById(R.id.content);
        time = findViewById(R.id.time);
        time.setOnClickListener(this);
        int px = dip2px(this, 10);
        ((SuperTextView) findViewById(R.id.notify)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.time)).getLeftTextView().setPadding(px, 0, 0, 0);
        time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
        Intent intent = getIntent();
        id = intent.getLongExtra("id", -1);

        //初始化数据
        if (id != -1) {
            habit = LitePal.find(Habit.class, id);
            needSign = habit.isNeedSign();
            content.setText(habit.getMainTitle());
            if (habit.getClockTime() > 0) {
                calendar.setTimeInMillis(habit.getClockTime());
            }
            time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
            if (habit.isNotify()) {
                time.setVisibility(View.VISIBLE);
                notify.setSwitchIsChecked(true);
            }
            isNotify = habit.isNotify();
            String str = habit.isNeedSign() ? "放回" : "添加";
            SuperTextView superTextView = findViewById(R.id.ok);
            superTextView.setCenterString(str.equals("放回") ? "放回到习惯库" : "添加到首页");
            t1.setVisibility(View.VISIBLE);
            t2.setText(str);
        } else {
            needSign = true;
            SuperTextView superTextView = findViewById(R.id.ok);
            superTextView.setCenterString("添加到首页");
            t2.setText("添加");
            t1.setVisibility(View.GONE);
        }


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

        if (api != null)
            content.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    Date[] dates = api.getDate(s.toString());
                    if (dates.length > 0) {
                        isNotify = true;
                        calendar.setTime(dates[0]);
                        if ((calendar.getTimeInMillis() - 24 * 60 * 60 * 1000) > System.currentTimeMillis())
                            calendar.add(Calendar.DAY_OF_YEAR, -1);
                        notify.setSwitchIsChecked(true);
                        time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));
                    }
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
                            }
                        })
                        .create();
        mDatePicker.showSpecificTime(true);
        mDatePicker.hideTimeUnit(new SCROLL_TYPE[]{SCROLL_TYPE.YEAR, SCROLL_TYPE.MONTH, SCROLL_TYPE.DAY});
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ok:
                if (content.getText().toString().trim().length() == 0) {
                    ToastUtil.showMsg(context, "输入内容不能为空");
                    return;
                }
                if (id != -1)
                    needSign = !needSign;
                finish();
                break;
            case R.id.notify:
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                break;
            case R.id.time:
                mDatePicker.show(CalendarUtils.formatDateString(System.currentTimeMillis(),
                        "yyyy-MM-dd"));
                break;
            case R.id.t1:
                new AlertDialog.Builder(context).setTitle("注意")
                        .setMessage("确定要清除此习惯吗？")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Habit habit = LitePal.find(Habit.class, id);
                                if (habit != null)
                                    habit.delete();
                                ToastUtil.showMsg(context, "成功删除习惯");
                                finish();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .show();
                break;
            case R.id.t2:
                if (content.getText().toString().trim().length() == 0) {
                    ToastUtil.showMsg(context, "输入内容不能为空");
                    return;
                }
                if (id != -1)
                    needSign = !needSign;
                finish();
                break;
            case R.id.title:
                finish();
                break;
        }
    }

    @Override
    protected void onPause() {
        if (calendar.getTimeInMillis() < System.currentTimeMillis())
            calendar.add(Calendar.DATE, 1);
        long time = calendar.getTimeInMillis();
        String str = content.getText().toString();
        Habit habit;
        if (id == -1) {
            habit = new Habit();
        } else {
            habit = LitePal.find(Habit.class, id);
        }

        if (habit != null && str.trim().length() != 0) {
            habit.setMainTitle(str);
            habit.setClockTime(time);
            habit.setNotify(isNotify);
            habit.setYouSheng(SPSingleton.get().
                    getBoolean(PrefConstants.PREFS_KEY_MENSE_MODE, true));
            habit.setNeedSign(needSign);
            habit.save();
            AlarmHelper.notifyHabit(context, habit);
        }
        super.onPause();
    }


}
