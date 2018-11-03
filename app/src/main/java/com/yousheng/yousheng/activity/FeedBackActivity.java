package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.TitleBarUtils;

public class FeedBackActivity extends AppCompatActivity {
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);
        initView();
        initTitleBar();
    }

    private void initTitleBar() {
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_RIGHT_TEXT:
                        //发送反馈
                        String feedback = editText.getText().toString().trim();
                        if (!TextUtils.isEmpty(feedback)) {
                            MobclickAgent.reportError(FeedBackActivity.this, feedback);
                            finish();
                        } else {
                            Toast.makeText(FeedBackActivity.this, "请填入反馈信息", Toast.LENGTH_SHORT);
                        }
                        break;
                    case CommonTitleBar.ACTION_LEFT_TEXT:
                        finish();
                        break;
                }
            }
        });
    }

    private void initView() {
        editText = findViewById(R.id.edit_text);
    }
}
