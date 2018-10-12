package com.yousheng.yousheng.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.yousheng.yousheng.R;
import com.yousheng.yousheng.habit.AllHabitActivity;
import com.yousheng.yousheng.notify.NewItemActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.settings).setOnClickListener(this);
        findViewById(R.id.newItem).setOnClickListener(this);
        findViewById(R.id.weightRecord).setOnClickListener(this);
        findViewById(R.id.habit).setOnClickListener(this);
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
            default:
                break;
        }
    }
}
