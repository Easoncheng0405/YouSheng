package com.yousheng.yousheng.habit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.ToastUtil;

import java.util.HashMap;
import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

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

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.habit_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {

        //末尾自定义添加按钮
        if (i + 1 == getItemCount()) {
            LinearLayout.LayoutParams margin = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            margin.setMargins(20, 70, 20, 80);
            viewHolder.superTextView.setLayoutParams(margin);
            viewHolder.superTextView.setCenterTextColor(Color.WHITE);
            viewHolder.superTextView.setCenterString("自定义添加");
            viewHolder.superTextView.setShapeSelectorPressedColor(Color.rgb(188, 181, 181));
            viewHolder.superTextView.setShapeSelectorNormalColor(Color.rgb(216, 27, 96));
            viewHolder.superTextView.useShape();
            viewHolder.superTextView.setPadding(0, 30, 0, 30);
            viewHolder.superTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToastUtil.showMsg(context, "自定义添加");
                }
            });
            return;
        }


        final Habit habit;
        //排列顺序从上往下依次为官方未添加，自定义未添加，官方已添加，自定义已添加
        final HabitHelper.HabitState state = getStateByIndex(i);
        switch (state) {
            case OFFICIAL_OUT:
                if (i == 0) {
                    viewHolder.textView.setVisibility(View.VISIBLE);
                    viewHolder.textView.setText("未添加");
                }
                habit = officialOut.get(i);
                viewHolder.superTextView.setLeftString(habit.getTitle());
                viewHolder.superTextView.setLeftBottomString(habit.getTitle2());
                viewHolder.superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.add_red));
                viewHolder.superTextView.getRightIconIV().setPadding(40, 40, 22, 40);
                break;
            case OFFICIAL_IN:
                if (i == oo + co) {
                    viewHolder.textView.setVisibility(View.VISIBLE);
                    viewHolder.textView.setText("已添加");
                }
                habit = officialIn.get(i - oo - co);
                viewHolder.superTextView.setLeftString(habit.getTitle());
                viewHolder.superTextView.setLeftBottomString(habit.getTitle2());
                viewHolder.superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.dev_red));
                viewHolder.superTextView.getRightIconIV().setPadding(40, 40, 22, 40);
                break;
            case CUSTOM_OUT:
                if (i == oo) {
                    viewHolder.textView.setVisibility(View.VISIBLE);
                    viewHolder.textView.setText("自定义未添加");
                }
                habit = customOut.get(i - oo);
                viewHolder.superTextView.setLeftString(habit.getTitle());
                viewHolder.superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.add_red));
                viewHolder.superTextView.setPadding(0, 20, 0, 20);
                viewHolder.superTextView.getRightIconIV().setPadding(40, 35, 35, 35);
                break;
            //自定义已添加一定在最后面
            default:
                if (i == oo + co && oi == 0) {
                    viewHolder.textView.setVisibility(View.VISIBLE);
                    viewHolder.textView.setText("已添加");
                }
                habit = customIn.get(i - oo - co - oi);
                viewHolder.superTextView.setLeftString(habit.getTitle());
                viewHolder.superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.dev_red));
                viewHolder.superTextView.setPadding(0, 35, 0, 35);
                viewHolder.superTextView.getRightIconIV().setPadding(40, 40, 40, 40);
                break;
        }

        viewHolder.superTextView.getLeftBottomTextView().setPadding(0, 0, 150, 0);
        viewHolder.superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(context, habit.getId() + "");
            }
        });

        viewHolder.superTextView.getRightIconIV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (habit.getState() == HabitHelper.IN) {
                    if (state == HabitHelper.HabitState.CUSTOM_IN) {
                        customIn.remove(habit);
                        habit.setState(HabitHelper.IN);
                        ci--;
                        customOut.add(habit);
                        co++;
                    } else {
                        officialIn.remove(habit);
                        habit.setState(HabitHelper.IN);
                        oi--;
                        officialOut.add(habit);
                        oo++;
                    }
                } else {
                    if (state == HabitHelper.HabitState.CUSTOM_OUT) {
                        customOut.remove(habit);
                        habit.setState(HabitHelper.IN);
                        co--;
                        customIn.add(habit);
                        ci++;
                    } else {
                        officialOut.remove(habit);
                        habit.setState(HabitHelper.IN);
                        oo--;
                        officialIn.add(habit);
                        oi++;
                    }
                }

                for (int i = 0; i < getItemCount(); i++)
                    notifyItemChanged(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        //最后一个是添加自定义按钮
        return oi + oo + ci + co + 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        SuperTextView superTextView;
        TextView textView;

        ViewHolder(View view) {
            super(view);
            superTextView = view.findViewById(R.id.habit);
            textView = view.findViewById(R.id.title);
        }
    }

    private HabitHelper.HabitState getStateByIndex(int i) {
        if (i < oo)
            return HabitHelper.HabitState.OFFICIAL_OUT;
        if (i < oo + co)
            return HabitHelper.HabitState.CUSTOM_OUT;
        if (i < oo + co + oi)
            return HabitHelper.HabitState.OFFICIAL_IN;
        return HabitHelper.HabitState.CUSTOM_IN;
    }
}
