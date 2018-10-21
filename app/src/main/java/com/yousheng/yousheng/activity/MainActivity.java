package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.yousheng.yousheng.R;

import com.yousheng.yousheng.adapter.HabitAdapter;
import com.yousheng.yousheng.habit.Habit;
import com.yousheng.yousheng.receiver.AlarmHelper;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //view

    /***优生打卡recyclerview*/
    private RecyclerView mRvHabitiList;
    private HabitAdapter mHabitAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //唤醒所有闹钟
        AlarmHelper.notifyAllAlarm(this);

        init();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_ovulation:
                startActivity(new Intent(this, OvulationActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 查询打卡事项的详情
     */
    private void queryHabitData() {
        LitePal.findAllAsync(Habit.class).listen(new FindMultiCallback<Habit>() {
            @Override
            public void onFinish(List<Habit> list) {
                mHabitAdapter = new HabitAdapter(list, MainActivity.this);
                mRvHabitiList.setAdapter(mHabitAdapter);
            }
        });
    }

    /**
     * 初始化activity中的变量，从数据库中查询数据
     * */
    private void init() {
        mRvHabitiList = findViewById(R.id.rv_good_habbit);
        mRvHabitiList.setLayoutManager(new LinearLayoutManager(this));
        queryHabitData();
    }
}
