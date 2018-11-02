package com.yousheng.yousheng.habit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;

import static com.yousheng.yousheng.uitl.TitleBarUtils.changeTitleImageLeftMargin;

public class AllHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_habit);

        CommonTitleBar titleBar = findViewById(R.id.habit_all_title);
        changeTitleImageLeftMargin(this, titleBar);
        titleBar.getRightImageButton().setPadding(0, 0, 80, 0);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case 1:
                        finish();
                        break;
                    case 4:
                        Intent intent = new Intent(AllHabitActivity.this, HabitActivity.class);
                        intent.putExtra("id", -1L);
                        startActivity(intent);
                        break;
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        ListView habits = findViewById(R.id.habits);
        HabitAdapter adapter = new HabitAdapter(this, HabitHelper.getAllHabitInState());
        habits.setAdapter(adapter);
        habits.setDivider(null);
    }
}
