package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ViewStubCompat;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.umeng.analytics.MobclickAgent;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.manager.GDTSplashManager;
import com.yousheng.yousheng.uitl.NetworkUtils;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private Timer mTimer;

    private GDTSplashManager gdtManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        if (NetworkUtils.isNetworkConnected(this)) {
            initView();
            initGDT();
//            initTimer(true);
        } else {
            initLayoutWhenNoNetwork();
            initTimer(false);
        }
    }

    private void initGDT() {
        final ViewStubCompat viewStubCompat = findViewById(R.id.stub_launch);
        FrameLayout adContainer = findViewById(R.id.iv_ad_pic);
        final TextView skipButton = findViewById(R.id.tv_count_down);

        gdtManager = new GDTSplashManager();
        gdtManager.init(this, adContainer, skipButton, 3000);
        gdtManager.setmAdStateListener(new GDTSplashManager.SplashADStateListener() {
            @Override
            public void onADTick(long timeMillis) {
                String countDownText = skipButton.getText().toString();
                final String[] texts = countDownText.split(" ");
                skipButton.setText(texts[0]
                        .concat(" ")
                        .concat(String.valueOf((timeMillis + 500) / 1000)));
            }

            @Override
            public void onADShow() {
                viewStubCompat.setVisibility(View.GONE);
                findViewById(R.id.layout_advertisement).setVisibility(View.VISIBLE);
            }

            @Override
            public void onADFetchFailed() {
                navigateToMainActivity();
            }

            @Override
            public void onADExposure() {
                navigateToMainActivity();
            }
        });
        gdtManager.fetch();
    }

    private void initLayoutWhenNoNetwork() {
        findViewById(R.id.layout_advertisement).setVisibility(View.GONE);
        ViewStubCompat viewStubCompat = findViewById(R.id.stub_launch);
        viewStubCompat.inflate();
    }

    private void initView() {
//        ViewStubCompat viewStubCompat = findViewById(R.id.stub_launch);
//        viewStubCompat.inflate();
//        findViewById(R.id.layout_advertisement).setVisibility(View.INVISIBLE);

        findViewById(R.id.tv_count_down).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer != null) {
                    mTimer.cancel();
                }
                navigateToMainActivity();
            }
        });
    }

    private void navigateToMainActivity() {
        startActivity(new Intent(SplashActivity.this
                , MainActivity.class));
        finish();
    }

    private void initTimer(boolean isNetworkConnected) {
        mTimer = new Timer();
        if (isNetworkConnected) {
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    final TextView tvCountDown = findViewById(R.id.tv_count_down);
                    String countDownText = tvCountDown.getText().toString();
                    final String[] texts = countDownText.split(" ");
                    int leftTimeSeconds = Integer.valueOf(texts[1]);
                    if (leftTimeSeconds > 0) {
                        leftTimeSeconds--;
                        final int i = leftTimeSeconds;
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                tvCountDown.setText(texts[0]
                                        .concat(" ")
                                        .concat(String.valueOf(i)));
                            }
                        });

                    } else {
                        mTimer.cancel();
                        navigateToMainActivity();
                    }
                }
            }, 1000, 1000);
        } else {
            mTimer.schedule(new TimerTask() {
                @Override
                public void run() {
                    navigateToMainActivity();
                }
            }, 3 * 1000);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
}
