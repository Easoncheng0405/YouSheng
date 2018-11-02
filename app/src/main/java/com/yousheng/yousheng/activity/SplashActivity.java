package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.yousheng.yousheng.R;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initTimer();
    }

    private void initTimer() {
        final Timer timer = new Timer();
        timer.schedule(new TimerTask() {
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
                    timer.cancel();
                    startActivity(new Intent(SplashActivity.this
                            , MainActivity.class));
                    finish();
                }
            }
        }, 1000, 1000);
    }
}
