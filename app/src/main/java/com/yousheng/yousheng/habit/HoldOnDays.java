package com.yousheng.yousheng.habit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;


//打卡activity
public class HoldOnDays extends AppCompatActivity {

    private Habit habit;
    private Record record;

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
        titleBar.getLeftTextView().setText("  " + habit.getMainTitle());
        ((TextView) findViewById(R.id.title1)).setText(habit.getMainTitle());
        if (habit.isOfficial())
            ((TextView) findViewById(R.id.title2)).setText(habit.getSubTitle());
        ((TextView) findViewById(R.id.number)).setText(record.getDays());
    }

    //打卡
    private void record() {
        long millis = System.currentTimeMillis();
        //首次打卡
        if (record == null) {
            record=new Record();
            record.setId(habit.getId());
            record.setDays(1);
            record.setTime(millis);
            record.save();
            return;
        }
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
        ((TextView) findViewById(R.id.number)).setText(record.getDays());
    }
}
