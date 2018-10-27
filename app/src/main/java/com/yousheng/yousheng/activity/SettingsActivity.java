package com.yousheng.yousheng.activity;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.html.Agreement;
import com.yousheng.yousheng.html.Disclaimer;
import com.yousheng.yousheng.model.Market;
import com.yousheng.yousheng.uitl.ToastUtil;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.List;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        CommonTitleBar titleBar = findViewById(R.id.title);
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
                startActivity(new Intent(this, MenseManagementActivity.class));
                break;
            case R.id.update:
                List<Market> markets = LitePal.findAll(Market.class);
                Log.e("SettingsActivity", "counts = " + markets.size());

                Collections.sort(markets);
                boolean b = false;
                for (Market market : markets) {
                    if (startMarket(market.getPackageName())) {
                        b = true;
                        break;
                    }
                }
                if (!b)
                    ToastUtil.showMsg(this, "请搜索最新版本更新");
                break;
        }
    }

    private boolean startMarket(String packageName) {
        PackageInfo packageinfo = null;
        try {
            packageinfo = getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SettingsActivity", "startMarket: exception ", e);
            return false;
        }
        if (packageinfo == null) {
            Log.e("SettingsActivity", packageName);
            return false;
        }
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        List<ResolveInfo> resolveinfoList = getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            String name = resolveinfo.activityInfo.packageName;
            String className = resolveinfo.activityInfo.name;
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            ComponentName componentName = new ComponentName(name, className);
            intent.setComponent(componentName);
            startActivity(intent);
            return true;
        }
        return false;
    }

}
