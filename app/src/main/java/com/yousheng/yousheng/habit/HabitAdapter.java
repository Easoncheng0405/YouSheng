package com.yousheng.yousheng.habit;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.allen.library.SuperTextView;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.ToastUtil;

import java.util.List;

public class HabitAdapter extends RecyclerView.Adapter<HabitAdapter.ViewHolder> {

    private Context context;
    private List<Habit> habits;
    private static RelativeLayout.LayoutParams layoutParams;

    static {
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        layoutParams.setMargins(0, 15, 15, 0);
    }

    public HabitAdapter(Context context, List<Habit> habits) {
        this.context = context;
        this.habits = habits;
    }

    public void update(List<Habit> habits) {
        this.habits = habits;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.habit_detail, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final int id = i;
        Habit habit = habits.get(i);
        if (habit.getType() == HabitHelper.CUSTOM) {
            //官方
            viewHolder.superTextView.setLeftString(habit.getTitle());
            viewHolder.superTextView.setLeftBottomString(habit.getTitle2());
            viewHolder.superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.add_red));
            viewHolder.superTextView.getRightIconIV().setLayoutParams(layoutParams);
        } else {
            viewHolder.superTextView.setLeftString(habit.getTitle());
            viewHolder.superTextView.setRightIcon(context.getResources().getDrawable(R.drawable.add));
        }
        viewHolder.superTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(context, id + "");
                Log.d("HabitAdapter", "onClick: 0" + id);
            }
        });
        viewHolder.superTextView.getRightIconIV().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtil.showMsg(context, id + "添加到打卡！");
                Log.d("HabitAdapter", "onClick: 0" + id + "添加到打卡！");
            }
        });
    }

    @Override
    public int getItemCount() {
        return habits.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        SuperTextView superTextView;

        ViewHolder(View view) {
            super(view);
            superTextView = view.findViewById(R.id.habit);
        }
    }
}
