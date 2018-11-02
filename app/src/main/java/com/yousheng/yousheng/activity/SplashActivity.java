package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yousheng.yousheng.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {
    private Timer mTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initView();
        initTimer();
    }

    private void initView() {
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

    private void initTimer() {
        mTimer = new Timer();
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
    }
}
