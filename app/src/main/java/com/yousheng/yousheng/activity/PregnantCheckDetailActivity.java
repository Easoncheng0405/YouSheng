package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.TitleBarUtils;

public class PregnantCheckDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnant_check_detail);
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        TitleBarUtils.addTitleBarListener(this, titleBar);

        int index = getIntent().getIntExtra("index", 0);
        String title = getIntent().getStringExtra("title");
        String detail = getResources().getStringArray(R.array.pregnant_check_detail)[index];
        titleBar.getLeftTextView().setText(title);
        ((TextView) findViewById(R.id.tv_detail)).setText(detail);
        ((TextView) findViewById(R.id.tv_detail)).setMovementMethod(ScrollingMovementMethod.getInstance());
    }
}
