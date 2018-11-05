package com.yousheng.yousheng.habit;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;

import static com.yousheng.yousheng.uitl.TitleBarUtils.changeTitleImageLeftMargin;
import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;

/**
 * 打卡详情页面
 */
@Route(path = "/habitdetail/activity")
public class HabitDetailActivity extends AppCompatActivity {

    private SuperTextView time;
    private SuperTextView notify;

    private Calendar calendar = Calendar.getInstance(Locale.CHINA);
    private TimePickerDialog timePickerDialog;

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
            final CommonTitleBar titleBar = findViewById(R.id.title);
            changeTitleImageLeftMargin(this, titleBar);
            titleBar.getLeftTextView().setText("  " + habit.getMainTitle());
            String str = habit.isNeedSign() ? "移除" : "添加";
            titleBar.getRightTextView().setText(str);
            final SuperTextView superTextView = findViewById(R.id.ok);
            superTextView.setCenterString(str.equals("移除") ? "从首页移除" : "添加到首页");

            time = findViewById(R.id.time);
            notify = findViewById(R.id.notify);

            int px = dip2px(this, 10);
            ((SuperTextView)findViewById(R.id.notify)).getLeftTextView().setPadding(px, 0, 0, 0);
            ((SuperTextView)findViewById(R.id.time)).getLeftTextView().setPadding(px, 0, 0, 0);

            final ScrollView scrollView = findViewById(R.id.scrollView);

            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    ViewGroup.LayoutParams params = scrollView.getLayoutParams();
                    params.height = (int) ((superTextView.getTop() - notify.getBottom()) * 0.9);
                    params.width = WindowManager.LayoutParams.MATCH_PARENT;
                    scrollView.setLayoutParams(params);
                }
            });

            //初始化数据
            calendar.setTimeInMillis(habit.getClockTime());
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

            notify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    notify.setSwitchIsChecked(!notify.getSwitchIsChecked());
                }
            });

            time.setLeftString("每天" + DateFormat.format("HH:mm", calendar.getTime()));

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
                    scrollView.post(new Runnable() {
                        @Override
                        public void run() {
                            ViewGroup.LayoutParams params = scrollView.getLayoutParams();
                            params.height = (int) ((superTextView.getTop() - notify.getBottom()) * 0.9);
                            params.width = WindowManager.LayoutParams.MATCH_PARENT;
                            scrollView.setLayoutParams(params);
                        }
                    });
                }
            });

            TextView title1 = findViewById(R.id.title1);
            title1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            TextView title2 = findViewById(R.id.title2);
            TextView content = findViewById(R.id.content);
            title1.setText(habit.getMainTitle());
            title2.setText(habit.getSubTitle());
            content.setText(habit.getContent());
            superTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Habit h = LitePal.find(Habit.class, habit.getId());
                    h.setNeedSign(!habit.isNeedSign());
                    h.save();
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
                            Habit h = LitePal.find(Habit.class, habit.getId());
                            h.setNeedSign(!habit.isNeedSign());
                            h.save();
                            break;
                    }
                    finish();
                }
            });
        } catch (Exception e) {
            ToastUtil.showMsg(this, "发生了一些错误！请联系客服！");
            e.printStackTrace();
            finish();
        }
    }
}
