package com.yousheng.yousheng.activities;


import android.graphics.Color;
import android.os.Bundle;

import android.widget.TextView;

import com.yousheng.yousheng.R;
import com.zjun.widget.RuleView;

import java.util.ArrayList;
import java.util.Random;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.view.LineChartView;

public class WeightActivity extends BaseActivity {


    private TextView weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        weight = findViewById(R.id.number);

        RuleView ruleView = findViewById(R.id.ruler);

        ruleView.setOnValueChangedListener(new RuleView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                weight.setText(value + "");
            }
        });

        initLineChartView();


    }

    //初始化体重折线图
    private void initLineChartView() {
        ArrayList<PointValue> values = new ArrayList<>();//折线上的点

        Random random = new Random();
        for (int i = 0; i < 7; i++) {
            PointValue pointValue = new PointValue(i, random.nextInt() % 10 + 50);
            pointValue.setLabel(pointValue.getY() + "公斤");
            values.add(pointValue);
        }


        Line line = new Line(values).setColor(Color.rgb(249, 73, 137));
        //折线
        line.setCubic(true);
        line.setHasLabels(true);

        LineChartData data = new LineChartData();
        Axis axisX = new Axis();//x轴

        Axis axisY = new Axis();//y轴
        data.setAxisXBottom(axisX);
        data.setAxisYLeft(axisY);
        ArrayList<Line> lines = new ArrayList<>();
        lines.add(line);
        data.setLines(lines);

        LineChartView chartView = findViewById(R.id.chart);
        chartView.setLineChartData(data);
        chartView.setZoomType(ZoomType.VERTICAL);
    }
}
