package com.yousheng.yousheng.html;

import android.os.Bundle;
import android.webkit.WebView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;

public class Agreement extends HtmlActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement);
        loadUrl("file:///android_asset/html/agreement.html",
                (CommonTitleBar) findViewById(R.id.title)
                ,(WebView) findViewById(R.id.agreement));
    }
}
