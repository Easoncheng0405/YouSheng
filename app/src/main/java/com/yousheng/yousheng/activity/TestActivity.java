package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.habit.AllHabitActivity;
import com.yousheng.yousheng.habit.Habit;
import com.yousheng.yousheng.habit.HoldOnDays;
import com.yousheng.yousheng.weight.WeightActivity;
import com.yousheng.yousheng.notify.NewItemActivity;

import org.litepal.LitePal;

public class TestActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.newItem).setOnClickListener(this);
        findViewById(R.id.weightRecord).setOnClickListener(this);
        findViewById(R.id.main).setOnClickListener(this);
        findViewById(R.id.habit).setOnClickListener(this);
        findViewById(R.id.holdDays).setOnClickListener(this);
        findViewById(R.id.btn_ready).setOnClickListener(this);

        //默认文案
        {

            Habit a = new Habit(1);
            a.setOfficial(true);
            a.setMainTitle("补充叶酸");
            a.setSubTitle("避免胎儿畸形，应提前3个月坚持每天补充400微克");
            if (LitePal.find(Habit.class, 1) == null)
                a.save();
            Habit b = new Habit(2);
            b.setOfficial(true);
            b.setMainTitle("不喝咖啡");
            b.setSubTitle("咖啡影响孕育，每天喝一杯咖啡以上的育龄女性，怀孕的可能性只是不喝咖啡者的一半");

            if (LitePal.find(Habit.class, 2) == null)
                b.save();

            Habit c = new Habit(3);
            c.setMainTitle("补充鱼油");
            c.setClockTime(1539612919000L);

            if (LitePal.find(Habit.class, 3) == null)
                c.save();

            Habit d = new Habit(4);
            d.setNeedSign(true);
            d.setOfficial(true);
            d.setMainTitle("坚持运动");
            d.setSubTitle("运动使人快乐");

            if (LitePal.find(Habit.class, 4) == null)
                d.save();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.settings:
                startActivity(new Intent(this, SettingsActivity.class));
                break;
            case R.id.newItem:
                startActivity(new Intent(this, NewItemActivity.class));
                break;
            case R.id.weightRecord:
                startActivity(new Intent(this, WeightActivity.class));
                break;
            case R.id.habit:
                startActivity(new Intent(this, AllHabitActivity.class));
                break;
            case R.id.main:
                startActivity(new Intent(this, MainActivity.class));
                break;
            case R.id.splash:
                ARouter.getInstance().build("/splash/activity").navigation();
                break;
            case R.id.mense_management:
                ARouter.getInstance().build("/mensemanage/activity").navigation();
                break;
            case R.id.holdDays:
                startActivity(new Intent(this, HoldOnDays.class));
                break;
            case R.id.btn_ready:
                startActivity(new Intent(this, ReadyActivity.class));
                break;
        }
    }
}
