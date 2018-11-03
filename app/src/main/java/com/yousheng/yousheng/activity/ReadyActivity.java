package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.Constants;
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
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ready);
        initView();
        queryCommentText();
    }

    private void queryCommentText() {
        String date = getIntent().getStringExtra("date");
        List<MenseInfo> menseInfos =
                LitePal.select()
                        .where("date = ?", date)
                        .find(MenseInfo.class);
        mMenseInfo = menseInfos.get(0);
        if (!TextUtils.isEmpty(mMenseInfo.getComment())) {
            ((EditText) findViewById(R.id.et_ready)).setText(mMenseInfo.getComment());
        }
    }

    private void goBack() {
        String content = editText.getText().toString();
        Intent resultData = new Intent();
        if (!TextUtils.isEmpty(content.trim())) {
            mMenseInfo.setComment(content);
            mMenseInfo.save();
            resultData.putExtra("text", editText.getText().toString());
            setResult(Constants.RESULT_CODE_MAIN_TO_COMMENT, resultData);
            finish();
        } else {
            Toast.makeText(ReadyActivity.this, "请输入内容", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    private void initView() {
        editText = findViewById(R.id.et_ready);
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_TEXT:
                        finish();
                        break;
                    case CommonTitleBar.ACTION_RIGHT_TEXT:
                        goBack();
                        break;
                }
            }
        });
    }
}
