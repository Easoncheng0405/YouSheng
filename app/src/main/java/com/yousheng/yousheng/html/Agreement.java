package com.yousheng.yousheng.html;

import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.webkit.WebView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;

public class Agreement extends HtmlActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);

        CommonTitleBar titleBar = findViewById(R.id.title);
        titleBar.setRightView(new SwitchCompat(this));
        loadUrl("file:///android_asset/html/agreement.html",
                titleBar, (WebView) findViewById(R.id.agreement));
    }
}
