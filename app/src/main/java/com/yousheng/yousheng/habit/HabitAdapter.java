package com.yousheng.yousheng.habit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.Habit;

import org.litepal.LitePal;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HabitAdapter extends BaseAdapter {

    private Context context;
    private List<Habit> officialIn;
    private List<Habit> customIn;
    private List<Habit> officialOut;
    private List<Habit> customOut;
    private int oi, oo, ci, co;


    HabitAdapter(Context context, HashMap<HabitHelper.HabitState, List<Habit>> map) {
        this.context = context;
        this.officialOut = map.get(HabitHelper.HabitState.OFFICIAL_OUT);
        this.officialIn = map.get(HabitHelper.HabitState.OFFICIAL_IN);
        this.customOut = map.get(HabitHelper.HabitState.CUSTOM_OUT);
        this.customIn = map.get(HabitHelper.HabitState.CUSTOM_IN);
        oi = officialIn.size();
        oo = officialOut.size();
        ci = customIn.size();
        co = customOut.size();
    }


    @Override
    public int getCount() {
        //最后一个是添加自定义按钮
        return oi + oo + ci + co + 1;
    }

    @Override
    public Object getItem(int i) {
        if (getCount() == 1)
            return null;
        Object habit;
        switch (getStateByIndex(i)) {
            case OFFICIAL_OUT:
                habit = officialOut.get(i);
                break;
            case OFFICIAL_IN:
                habit = officialIn.get(i - oo - co);
                break;
            case CUSTOM_OUT:
                habit = customOut.get(i - oo);
                break;
            //自定义已添加在最后面
            default:
                habit = customIn.get(i - oo - co - oi);
                break;
        }
        return habit;
    }

    @Override
    public long getItemId(int position) {
        if (getCount() == 1)
            return 0;
        return ((Habit) getItem(position)).getId();
    }


    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.habit_detail, null);
        SuperTextView superTextView = convertView.findViewById(R.id.habit);
        TextView textView = convertView.findViewById(R.id.title);
        if (i + 1 == getCount()) {
            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            margin.setMargins(20, 70, 20, 80);
            superTextView.setLayoutParams(margin);
            superTextView.setCenterTextColor(Color.WHITE);
            superTextView.setCenterString("自定义添加");
            superTextView.setShapeSelectorPressedColor(Color.rgb(188, 181, 181));
            superTextView.setShapeSelectorNormalColor(Color.rgb(216, 27, 96));
            superTextView.useShape();
            superTextView.setPadding(0, 30, 0, 30);
            superTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, HabitActivity.class);
                    intent.putExtra("id", -1);
                    context.startActivity(intent);
                }
            });
            return convertView;
        }
        final Habit habit = (Habit) getItem(i);
        switch (getStateByIndex(i)) {
            case OFFICIAL_OUT:
                if (i == 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("未添加");
                }
                superTextView.setLeftString(habit.getMainTitle());
                superTextView.setLeftBottomString(habit.getSubTitle());
                superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.add_red));
                superTextView.getRightIconIV().setPadding(40, 40, 22, 40);
                break;
            case OFFICIAL_IN:
                if (i == oo + co) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("已添加");
                }
                superTextView.setLeftString(habit.getMainTitle());
                superTextView.setLeftBottomString(habit.getSubTitle());
                superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.dev_red));
                superTextView.getRightIconIV().setPadding(40, 40, 22, 40);
                break;
            case CUSTOM_OUT:
                if (i == oo) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("自定义未添加");
                }
                superTextView.setLeftString(habit.getMainTitle());
                superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.add_red));
                superTextView.setPadding(0, 20, 0, 20);
                superTextView.getRightIconIV().setPadding(40, 35, 35, 35);
                break;
            //自定义已添加一定在最后面
            case CUSTOM_IN:
                if (i == oo + co && oi == 0) {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText("已添加");
                }
                superTextView.setLeftString(habit.getMainTitle());
                superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.dev_red));
                superTextView.setPadding(0, 35, 0, 35);
                superTextView.getRightIconIV().setPadding(40, 40, 40, 40);
                break;
        }

        superTextView.getLeftBottomTextView().setPadding(0, 0, 150, 0);
        superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habit.isOfficial()) {
                    //官方的去详情页面
                    Intent intent = new Intent(context, HabitDetailActivity.class);
                    intent.putExtra("id", habit.getId());
                    context.startActivity(intent);
                } else {
                    //自定义的去编辑页面
                    Intent intent = new Intent(context, HabitActivity.class);
                    intent.putExtra("id", habit.getId());
                    //自定义习惯只有标题
                    context.startActivity(intent);
                }
            }
        });

        superTextView.getRightIconIV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habit.isNeedSign()) {
                    if (habit.isOfficial()) {
                        officialIn.remove(habit);
                        habit.setNeedSign(false);
                        oi--;
                        officialOut.add(habit);
                        oo++;
                    } else {
                        customIn.remove(habit);
                        habit.setNeedSign(false);
                        ci--;
                        customOut.add(habit);
                        co++;
                    }
                } else {
                    if (habit.isOfficial()) {
                        officialOut.remove(habit);
                        habit.setNeedSign(true);
                        oo--;
                        officialIn.add(habit);
                        oi++;

                    } else {
                        customOut.remove(habit);
                        habit.setNeedSign(true);
                        co--;
                        customIn.add(habit);
                        ci++;
                    }
                }
                Habit h = LitePal.find(Habit.class, habit.getId());
                h.setNeedSign(habit.isNeedSign());
                h.save();
                Collections.sort(customIn);
                Collections.sort(customOut);
                Collections.sort(officialIn);
                Collections.sort(officialOut);
                notifyDataSetInvalidated();
            }
        });
        return convertView;
    }


    private HabitHelper.HabitState getStateByIndex(int i) {
        if (i >= 0 && i < oo)
            return HabitHelper.HabitState.OFFICIAL_OUT;
        if (i >= oo && i < oo + co)
            return HabitHelper.HabitState.CUSTOM_OUT;
        if (i >= oo + co && i < oo + co + oi)
            return HabitHelper.HabitState.OFFICIAL_IN;
        return HabitHelper.HabitState.CUSTOM_IN;
    }
}
