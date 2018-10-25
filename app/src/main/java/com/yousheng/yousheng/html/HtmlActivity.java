package com.yousheng.yousheng.html;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public  class HtmlActivity extends AppCompatActivity {

    protected void loadUrl(String url,CommonTitleBar titleBar,WebView webView){
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                finish();
            }
        });
        webView.loadUrl(url);
    }
}
