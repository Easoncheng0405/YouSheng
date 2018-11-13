package com.yousheng.yousheng.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.format.DateFormat;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
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
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.habit.HoldOnDays;
import com.yousheng.yousheng.model.Habit;
import com.yousheng.yousheng.model.Weight;
import com.yousheng.yousheng.weight.WeightActivity;


import org.litepal.LitePal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 优生打卡recyclerview的adapter
 */
public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.GoodHabitViewHolder> {
    private List<Habit> mDatas;
    private Context mContext;
    private LayoutInflater mInflater;
    private SimpleDateFormat simpleDateFormat;

    public HabitAdapter(List<Habit> mDatas, Context context) {
        this.mDatas = mDatas;
        this.mInflater = LayoutInflater.from(context);
        this.mContext = context;
        this.simpleDateFormat = new SimpleDateFormat("HH:mm", Locale.CHINA);
    }

    @NonNull
    @Override
    public GoodHabitViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        View itemView = mInflater.inflate(R.layout.layout_rv_item_goodhabit, null);
        return new GoodHabitViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final GoodHabitViewHolder viewHolder, final int index) {
        final Habit habit = mDatas.get(index);

        if (habit.getMainTitle().equals("记录体重")) {
            viewHolder.lineChart.setVisibility(View.VISIBLE);
            initLineChart(viewHolder.lineChart, habit);
        }
        //比如drawleft设置图片大小
        //获取图片
        Drawable drawable = mContext.getResources().getDrawable(R.drawable.clock);
        //第一个0是距左边距离，第二个0是距上边距离，40分别是长宽
        drawable.setBounds(0, 0, 35, 35);
        viewHolder.tvClock.setCompoundDrawables(drawable, null, null, null);

        viewHolder.tvMainTitle.setText(habit.getMainTitle());
        if (habit.isOfficial())
            viewHolder.tvSubTitle.setText(habit.getSubTitle());
        else
            viewHolder.tvSubTitle.setVisibility(View.GONE);
        SpannableString spannableString =
                new SpannableString("(已坚持".concat(String.valueOf(habit.getKeepDays())).concat("天)"));
        spannableString.setSpan(
                new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_red_theme)),
                4,
                4 + String.valueOf(habit.getKeepDays()).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewHolder.tvKeepDays.setText(spannableString);
        viewHolder.tvClock.setText(" " + simpleDateFormat.format(new Date(habit.getClockTime())));
        if (habit.isNotify())
            viewHolder.tvClock.setVisibility(View.VISIBLE);
        else
            viewHolder.tvClock.setVisibility(View.GONE);
        viewHolder.checkBox.setChecked(habit.isSigned());
        if (viewHolder.checkBox.isChecked()) {
            viewHolder.itemView.setBackground(mContext.getResources()
                    .getDrawable(R.drawable.shape_rv_item_selected));
        }
        if (habit.isSigned()) {
            viewHolder.itemView.setBackground(mContext.getResources().
                    getDrawable(R.drawable.shape_rv_item_selected));
        } else {
            viewHolder.itemView.setBackground(mContext.getResources().
                    getDrawable(R.drawable.shape_rv_item_unseleceted));
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (habit.getMainTitle().equals("记录体重"))
                    intent = new Intent(mContext, WeightActivity.class);
                else
                    intent = new Intent(mContext, HoldOnDays.class);
                intent.putExtra("id", habit.getId());
                mContext.startActivity(intent);
            }
        });

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (habit.getMainTitle().equals("记录体重")) {
                    Intent intent = new Intent(mContext, WeightActivity.class);
                    intent.putExtra("id", habit.getId());
                    mContext.startActivity(intent);
                    return;
                }

                if (isChecked) {
                    viewHolder.itemView.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.shape_rv_item_selected));
                    habit.setSignTime(System.currentTimeMillis());
                    habit.setKeepDays(habit.getKeepDays() + 1);
                    SpannableString spannableString =
                            new SpannableString("(已坚持".concat(String.valueOf(habit.getKeepDays())).concat("天)"));
                    spannableString.setSpan(
                            new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_red_theme)),
                            4,
                            4 + String.valueOf(habit.getKeepDays()).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewHolder.tvKeepDays.setText(spannableString);
                    Habit h = LitePal.find(Habit.class, habit.getId());
                    h.setKeepDays(habit.getKeepDays());
                    h.setSignTime(System.currentTimeMillis());
                    h.save();
                } else {
                    viewHolder.itemView.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.shape_rv_item_unseleceted));
                    habit.setSignTime(1000);
                    habit.setKeepDays(habit.getKeepDays() - 1);
                    SpannableString spannableString =
                            new SpannableString("(已坚持".concat(String.valueOf(habit.getKeepDays())).concat("天)"));
                    spannableString.setSpan(
                            new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_red_theme)),
                            4,
                            4 + String.valueOf(habit.getKeepDays()).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
                    viewHolder.tvKeepDays.setText(spannableString);
                    Habit h = LitePal.find(Habit.class, habit.getId());
                    h.setKeepDays(habit.getKeepDays());
                    h.setSignTime(1000);
                    h.save();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class GoodHabitViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMainTitle;
        private TextView tvSubTitle;
        private TextView tvKeepDays;
        private CheckBox checkBox;
        private TextView tvClock;
        private View itemView;
        private LineChart lineChart;

        GoodHabitViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvMainTitle = itemView.findViewById(R.id.tv_main_title);
            this.tvSubTitle = itemView.findViewById(R.id.tv_sub_title);
            this.checkBox = itemView.findViewById(R.id.checkbox);
            this.tvClock = itemView.findViewById(R.id.tv_clock_time);
            this.tvKeepDays = itemView.findViewById(R.id.tv_keep_on_days);
            this.itemView = itemView;
            this.lineChart = itemView.findViewById(R.id.lineChart);
        }
    }

    //初始化体重折线图
    private void initLineChart(LineChart lineChart, final Habit habit) {

        List<Weight> weights = LitePal.findAll(Weight.class);
        Collections.sort(weights);
        if (weights.size() > 30)
            weights = weights.subList(0, 30);
        final List<Weight> list = weights;
        final int mexWeightIndex = maxWeightIndex(list);
        if (list.size() == 0) {
            //无数据时显示的文字
            lineChart.setNoDataText("暂无数据，快去记录体重吧！");
            lineChart.setNoDataTextColor(Color.parseColor("#f94989"));
            //图标刷新
            lineChart.invalidate();
            return;
        }
        //禁止放缩
        lineChart.setPinchZoom(false);
        //显示边界
        lineChart.setDrawBorders(false);
        //设置数据
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            entries.add(new Entry(i * 1.0f, list.get(i).getWeight() * 1.0f));
        }
        //一个LineDataSet就是一条线
        final LineDataSet lineDataSet = new LineDataSet(entries, "");
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
        lineDataSet.setLineWidth(5f);
        //折线图不显示数值
        //data.setDrawValues(false);
        data.setValueFormatter(new IValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                int index = (int) entry.getX();
                if (index == 0 || index == list.size() - 1 || index == mexWeightIndex)
                    return value + "";
                return "";
            }
        });
        data.setValueTextSize(13f);
        //得到X轴
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setTextColor(Color.parseColor("#999999"));
        //设置X轴的位置（默认在上方)
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴坐标之间的最小间隔
        xAxis.setGranularity(1f);
        //设置X轴的刻度数量，第二个参数为true,将会画出明确数量（带有小数点），
        xAxis.setLabelCount(list.size());
        //设置X轴的值（最小值、最大值、然后会根据设置的刻度数量自动分配刻度显示）
        xAxis.setAxisMaximum(list.size() - 1 + 0.2f);
        xAxis.setAxisMinimum(-0.2f);
        //不显示网格线
        xAxis.setDrawGridLines(false);
        // 标签倾斜
        xAxis.setLabelRotationAngle(0);
        //设置X轴值为字符串
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                int index = (int) value;
                if (index == 0 || index == list.size() - 1 || index == mexWeightIndex)
                    return DateFormat.format("MM/dd", list.get(index).getTime()).toString();
                return "";
            }
        });

        //得到Y轴
        YAxis yAxis = lineChart.getAxisLeft();
        YAxis rightYAxis = lineChart.getAxisRight();
        yAxis.setTextColor(Color.parseColor("#999999"));
        //设置Y轴是否显示
        rightYAxis.setEnabled(false); //右侧Y轴不显示
        //显示网格线
        yAxis.setDrawGridLines(true);
        //图例：得到Legend
        Legend legend = lineChart.getLegend();
        //隐藏Legend
        legend.setEnabled(false);

        //隐藏描述
        Description description = new Description();
        description.setText("斤");
        description.setEnabled(true);
        description.setTextSize(13f);
        description.setTextColor(Color.parseColor("#999999"));
        lineChart.setDescription(description);
        //设置数据
        lineChart.setData(data);
        //图标刷新
        lineChart.invalidate();
        lineChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (habit.getMainTitle().equals("记录体重"))
                    intent = new Intent(mContext, WeightActivity.class);
                else
                    intent = new Intent(mContext, HoldOnDays.class);
                intent.putExtra("id", habit.getId());
                mContext.startActivity(intent);
            }
        });
    }

    private float minWeight(List<Weight> list) {
        float res = Float.MAX_VALUE;
        for (Weight w : list)
            if (w.getWeight() < res)
                res = w.getWeight();
        return res;
    }

    private int maxWeightIndex(List<Weight> list) {
        float res = Float.MIN_VALUE;
        int index = 0;
        for (int i = 0; i < list.size(); i++)
            if (list.get(i).getWeight() > res) {
                res = list.get(i).getWeight();
                index = i;
            }
        return index;
    }
}


