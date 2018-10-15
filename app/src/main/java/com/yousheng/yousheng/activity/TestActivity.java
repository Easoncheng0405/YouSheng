package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.habit.AllHabitActivity;
import com.yousheng.yousheng.habit.HoldOnDays;
import com.yousheng.yousheng.notify.NewItemActivity;

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
        }
    }
}
