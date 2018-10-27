package com.yousheng.yousheng.weight;


import android.graphics.Color;
import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
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
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Weight;
import com.yousheng.yousheng.uitl.ToastUtil;
import com.zjun.widget.RuleView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class WeightActivity extends AppCompatActivity {

    private LineChart lineChart;
    private TextView weight;
    private RuleView ruleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weight);

        weight = findViewById(R.id.number);
        lineChart = findViewById(R.id.lineChart);
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

        //测试数据
//        {
//            for (int i = 0; i < 10; i++) {
//                Weight weight = new Weight(i + 1);
//                weight.setWeight(100 + i);
//                weight.setTime(System.currentTimeMillis());
//                if (LitePal.find(Weight.class, weight.getId()) == null)
//                    weight.save();
//            }
//        }
//
        initLineChart(LitePal.findAll(Weight.class));

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
            entries.add(new Entry(i * 1.0f, list.get(i).getWeight() * 1.0f));
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
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），
        xAxis.setLabelCount(list.size());
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
                int index = (int) value;
                if (index < 0 || index >= list.size())
                    return "";
                return DateFormat.format("MM/dd", list.get(index).getTime()).toString();
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
