package com.yousheng.yousheng.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import com.allen.library.SuperTextView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.html.Agreement;
import com.yousheng.yousheng.html.Disclaimer;
import com.yousheng.yousheng.model.Market;
import com.yousheng.yousheng.uitl.SPSingleton;
import com.yousheng.yousheng.uitl.ToastUtil;
import com.yousheng.yousheng.uitl.time.MarketUtils;
import com.yousheng.yousheng.view.EvaluationPopupWindow;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

import static com.yousheng.yousheng.uitl.TitleBarUtils.dip2px;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {
    private EvaluationPopupWindow mEvaluationWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        CommonTitleBar titleBar = findViewById(R.id.title);
        int px = dip2px(this, 10);
        ((SuperTextView) findViewById(R.id.menseManagement)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.stv_rating)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.bussiness)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.wechat)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.update)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.agreement)).getLeftTextView().setPadding(px, 0, 0, 0);
        ((SuperTextView) findViewById(R.id.disclaimer)).getLeftTextView().setPadding(px, 0, 0, 0);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case 1:
                        finish();
                        break;
                }
            }
        });
        mEvaluationWindow = new EvaluationPopupWindow(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wechat:
                new AlertDialog.Builder(this).setTitle("微信公众号")
                        .setMessage("请搜索 \"柚生\" 并关注")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
                break;
            case R.id.bussiness:
                new AlertDialog.Builder(this).setTitle("商务合作")
                        .setMessage("QQ: 260301369")
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                            }
                        }).show();
                break;
            case R.id.agreement:
                startActivity(new Intent(this, Agreement.class));
                break;
            case R.id.disclaimer:
                startActivity(new Intent(this, Disclaimer.class));
                break;
            case R.id.menseManagement:
                startActivityForResult(new Intent(this, MenseManagementActivity.class),
                        Constants.REQUEST_CODE_SETTING_TO_MENSE_MANAGEMENT);
                break;
            case R.id.update:
                if (!MarketUtils.navigationToAppStpre(this))
                    ToastUtil.showMsg(this, "请搜索最新版本更新");
                break;
            case R.id.stv_rating:
                if (!mEvaluationWindow.isShowing()) {
                    mEvaluationWindow
                            .showAtLocation(findViewById(R.id.layout_setting_root), Gravity.CENTER, 0, 0);
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constants.REQUEST_CODE_SETTING_TO_MENSE_MANAGEMENT:
                if (resultCode == Constants.RESULT_CODE_MENSE_START_DAY_CHANGED) {
                    SPSingleton.get().putBoolean(PrefConstants.PRFS_KEY_MENSE_START_DAY_CHANGED, true);
                }
                break;
        }
    }

}
