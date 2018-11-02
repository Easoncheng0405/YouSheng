package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.MenseInfo;
import com.yousheng.yousheng.uitl.TitleBarUtils;

import org.litepal.LitePal;
import org.litepal.crud.LitePalSupport;
import org.litepal.crud.callback.SaveCallback;

import java.util.List;

@Route(path = "/activity/ready")
public class ReadyActivity extends AppCompatActivity {
    private MenseInfo mMenseInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        queryCommentText();
        initView();
    }

    private void queryCommentText() {
        String date = getIntent().getStringExtra("date");
        List<MenseInfo> menseInfos =
                LitePal.select()
                        .where("date = ?", date)
                        .find(MenseInfo.class);
        mMenseInfo = menseInfos.get(0);
    }

    private void initView() {
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_TEXT:
                        finish();
                        break;
                    case CommonTitleBar.ACTION_RIGHT_TEXT: {
                        EditText editText = findViewById(R.id.et_ready);
                        String content = editText.getText().toString();
                        if (!TextUtils.isEmpty(content.trim())) {
                            mMenseInfo.setComment(content);
                            mMenseInfo.saveAsync().listen(new SaveCallback() {
                                @Override
                                public void onFinish(boolean success) {
                                    finish();
                                }
                            });
                        } else {
                            Toast.makeText(ReadyActivity.this, "请输入内容", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    }
                    break;
                }
            }
        });
        if (!TextUtils.isEmpty(mMenseInfo.getComment())) {
            ((EditText) findViewById(R.id.et_ready)).setText(mMenseInfo.getComment());
        }
    }
}
