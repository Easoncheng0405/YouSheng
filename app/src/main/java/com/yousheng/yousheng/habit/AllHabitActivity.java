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
        for (int i = 0; i < 10; i++)
            list.add(new Habit(i, "补充叶酸", "避免胎儿畸形，应提前3个月每天坚持补充400微克避免胎儿畸形，应提前3个月每天坚持补充400微克", "c", 0, 0, 1));
        HabitAdapter adapter = new HabitAdapter(this, list);
        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        ((LinearLayoutManager) manager).setOrientation(LinearLayout.VERTICAL);
        habits.setLayoutManager(manager);
        habits.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}
