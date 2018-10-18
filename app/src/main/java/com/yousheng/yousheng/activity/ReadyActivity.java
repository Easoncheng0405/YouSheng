package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.TitleBarUtils;

public class ReadyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        initView();
    }

    private void initView() {
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        //add listener
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action){
                    case CommonTitleBar.ACTION_LEFT_BUTTON:
                        break;
                    case CommonTitleBar.ACTION_RIGHT_TEXT:
                        break;
                }
            }
        });
    }
}
