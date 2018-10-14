package com.yousheng.yousheng.habit;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;

import java.util.ArrayList;
import java.util.Random;

public class AllHabitActivity extends AppCompatActivity {

    private RecyclerView habits;

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
        Random random = new Random();
        for (int i = 0; i < 30; i++) {
            int a = Math.abs(random.nextInt() % 2);
            int b = Math.abs(random.nextInt() % 2);
            list.add(new Habit(i, "补充叶酸" + a + b, "避免胎儿畸形，应提前3个月每天坚" +
                    "持补充400微克避免胎儿畸形，应提前3个月每天坚持补充400微克",
                    "c", 0, a, b));
        }
        HabitAdapter adapter = new HabitAdapter(this, HabitHelper.getAllHabitInState(list));
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        ((LinearLayoutManager) manager).setOrientation(LinearLayout.VERTICAL);
        habits.setLayoutManager(manager);
        habits.setAdapter(adapter);
    }
}
