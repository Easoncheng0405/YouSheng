package com.yousheng.yousheng.weight;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Weight;
import com.yousheng.yousheng.uitl.ToastUtil;
import com.zjun.widget.RuleView;

import org.litepal.LitePal;

import java.util.Calendar;
import java.util.Locale;


public class WeightActivity extends AppCompatActivity {

    private TextView weight;
    private RuleView ruleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        weight = findViewById(R.id.number);
        ruleView = findViewById(R.id.ruler);

        ruleView.setOnValueChangedListener(new RuleView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                weight.setText(value + "");
            }
        });

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

        findViewById(R.id.ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                record();
            }
        });

    }

    private void record() {
        Weight weight = LitePal.findLast(Weight.class);
        long millis = System.currentTimeMillis();
        if(weight==null){
            Weight w = new Weight();
            w.setTime(millis);
            w.setWeight(ruleView.getCurrentValue());
            w.save();
            ToastUtil.showMsg(this, "打卡成功！");
            return;
        }

        Calendar db = Calendar.getInstance(Locale.CHINA);
        Calendar now = Calendar.getInstance(Locale.CHINA);
        db.setTimeInMillis(weight.getTime());
        now.setTimeInMillis(millis);
        if (now.get(Calendar.DAY_OF_YEAR) <= db.get(Calendar.DAY_OF_YEAR)) {
            weight.setWeight(ruleView.getCurrentValue());
            weight.setTime(millis);
            weight.save();
            ToastUtil.showMsg(this, "体重已更新！");
        } else {
            Weight w = new Weight();
            w.setTime(millis);
            w.setWeight(ruleView.getCurrentValue());
            w.save();
            ToastUtil.showMsg(this, "打卡成功！");
        }
    }
}
