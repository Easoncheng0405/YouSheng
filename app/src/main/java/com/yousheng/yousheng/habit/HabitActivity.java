package com.yousheng.yousheng.habit;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.receiver.AlarmHelper;
import com.yousheng.yousheng.uitl.SPSingleton;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;

import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;

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

    private TextView t1, t2;
    private boolean needSign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

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
        ((SuperTextView)findViewById(R.id.notify)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView)findViewById(R.id.time)).getLeftTextView().setPadding(px, 0, 0, 0);
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
                if (id != -1)
                    needSign = !needSign;
                finish();
                break;
            case R.id.notify:
                notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                break;
            case R.id.time:
                timePickerDialog.show();
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
