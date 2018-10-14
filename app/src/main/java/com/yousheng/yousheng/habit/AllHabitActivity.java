package com.yousheng.yousheng.habit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;

import java.util.ArrayList;

public class AllHabitActivity extends AppCompatActivity {

    private ListView habits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit);


        CommonTitleBar titleBar = findViewById(R.id.habit_all_title);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case 1:
                        finish();
                        break;
                    case 3:
                        //TODO 添加新习惯
                        break;
                }
            }
        });

        habits = findViewById(R.id.habits);

        ArrayList<Habit> list = new ArrayList<>();
        Habit a=new Habit(1);
        a.setState(HabitHelper.OUT);
        a.setType(HabitHelper.OFFICIAL);
        a.setTitle("补充叶酸");
        a.setTitle2("避免胎儿畸形，应提前3个月坚持每天补充400微克");
        list.add(a);
        Habit b=new Habit(2);
        b.setState(HabitHelper.OUT);
        b.setType(HabitHelper.OFFICIAL);
        b.setTitle("不喝咖啡");
        b.setTitle2("咖啡影响孕育，每天喝一杯咖啡以上的育龄女性，怀孕的可能性只是不喝咖啡者的一半");
        list.add(b);

        Habit c=new Habit(3);
        c.setState(HabitHelper.OUT);
        c.setType(HabitHelper.CUSTOM);
        c.setTitle("补充鱼油");
        list.add(c);

        Habit d=new Habit(4);
        d.setState(HabitHelper.IN);
        d.setType(HabitHelper.OFFICIAL);
        d.setTitle("坚持运动");
        d.setTitle2("运动使人快乐");
        list.add(d);

        HabitAdapter adapter = new HabitAdapter(this, HabitHelper.getAllHabitInState(list));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        habits.setAdapter(adapter);
        habits.setDivider(null);

    }
}
