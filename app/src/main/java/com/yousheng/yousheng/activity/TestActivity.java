package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.yousheng.yousheng.R;
import com.yousheng.yousheng.habit.AllHabitActivity;
import com.yousheng.yousheng.model.Habit;
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
//                ARouter.getInstance().build("/splash/activity").navigation();
                startActivity(new Intent(this, SplashActivity.class));
                break;
            case R.id.mense_management:
                startActivity(new Intent(this, MenseManagementActivity.class));
//                ARouter.getInstance().build("/mensemanagement/activity").navigation();
                break;
            case R.id.holdDays:
                Intent intent = new Intent(this, HoldOnDays.class);
                intent.putExtra("id", LitePal.findFirst(Habit.class).getId());
                startActivity(intent);
                break;
            case R.id.btn_ready:
                startActivity(new Intent(this, ReadyActivity.class));
                break;
        }
    }
}
