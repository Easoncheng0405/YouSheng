package com.yousheng.yousheng.habit;


import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.widget.TextView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.yousheng.yousheng.R;
import com.zjun.widget.RuleView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;


public class WeightActivity extends AppCompatActivity {

    private LineChart lineChart;
    private TextView weight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        weight = findViewById(R.id.number);
        lineChart = findViewById(R.id.lineChart);
        RuleView ruleView = findViewById(R.id.ruler);

        ruleView.setOnValueChangedListener(new RuleView.OnValueChangedListener() {
            @Override
            public void onValueChanged(float value) {
                weight.setText(value + "");
            }
        });

        //测试数据
        {
            for (int i = 0; i < 10; i++) {
                Weight weight = new Weight(i + 1);
                weight.setWeight(100 + i);
                weight.setTime(System.currentTimeMillis());
                if (LitePal.find(Weight.class, weight.getId()) == null)
                    weight.save();
            }
        }

        initLineChart(LitePal.findAll(Weight.class));

    }

    //初始化体重折线图
    private void initLineChart(final List<Weight> list) {
        if (list.size() == 0) {
            //无数据时显示的文字
            lineChart.setNoDataText("暂无数据，快去记录体重吧！");
            //图标刷新
            lineChart.invalidate();
            return;
        }

        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i, list.get(i).getWeight()));
        }
        //一个LineDataSet就是一条线
        LineDataSet lineDataSet = new LineDataSet(entries, "");
        //线颜色
        lineDataSet.setColor(Color.parseColor("#E91E63"));
        //线宽度
        lineDataSet.setLineWidth(3.0f);
        //显示圆点
        lineDataSet.setDrawCircles(true);
        lineDataSet.setCircleColor(Color.parseColor("#E91E63"));
        //线条平滑
        lineDataSet.setMode(LineDataSet.Mode.HORIZONTAL_BEZIER);
        //设置折线图填充
        lineDataSet.setDrawFilled(true);
        lineDataSet.setFillColor(Color.parseColor("#FCE4EC"));
        LineData data = new LineData(lineDataSet);
        //折线图不显示数值
        data.setDrawValues(true);
        //得到X轴
        XAxis xAxis = lineChart.getXAxis();
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），但是可能值导致不均匀，默认（6，false）
        xAxis.setLabelCount(list.size() / 2, false);
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        //xAxis.setAxisMinimum(0f);
        //xAxis.setAxisMaximum((float) list.size());
        //不显示网格线
        xAxis.setDrawGridLines(false);
        // 标签倾斜
        xAxis.setLabelRotationAngle(0);
        //设置X轴值为字符串
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return DateFormat.format("MM/dd", list.get((int) value).getTime()).toString();
            }
        });
        //得到Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //显示网格线
        yAxis.setDrawGridLines(true);
        yAxis.setAxisMinimum(minWeight(list) - 5);

        //图例：得到Legend
        Legend legend = lineChart.getLegend();
        //隐藏Legend
        legend.setEnabled(false);

        //隐藏描述
        Description description = new Description();
        description.setEnabled(false);
        lineChart.setDescription(description);

        //设置数据
        lineChart.setData(data);
        //图标刷新
        lineChart.invalidate();
    }

    private float minWeight(List<Weight> list) {
        float res = Float.MAX_VALUE;
        for (Weight w : list)
            if (w.getWeight() < res)
                res = w.getWeight();
        return res;
    }
}
