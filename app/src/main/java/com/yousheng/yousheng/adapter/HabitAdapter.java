package com.yousheng.yousheng.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.alibaba.android.arouter.launcher.ARouter;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.habit.Habit;

import java.text.SimpleDateFormat;
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
        this.simpleDateFormat = new SimpleDateFormat("hh:mm", Locale.CHINA);
    }

    @NonNull
    @Override
    public GoodHabitViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        View itemView = mInflater.inflate(R.layout.layout_rv_item_goodhabit, null);
        GoodHabitViewHolder holder = new GoodHabitViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final GoodHabitViewHolder viewHolder, int index) {
        final Habit habit = mDatas.get(index);
        viewHolder.tvMainTitle.setText(habit.getMainTitle());
        viewHolder.tvSubTitle.setText(habit.getSubTitle());

        SpannableString spannableString =
                new SpannableString("(已坚持".concat(String.valueOf(habit.getKeepDays())).concat("天)"));
        spannableString.setSpan(
                new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_color_red_theme)),
                4,
                4+String.valueOf(habit.getKeepDays()).length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        viewHolder.tvKeepDays.setText(spannableString);

        viewHolder.tvClock.setText(simpleDateFormat.format(new Date(habit.getClockTime())));
        viewHolder.checkBox.setChecked(habit.isSigned());
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
                if (viewHolder.checkBox.isChecked()) {
                    //已经打卡，跳转到详情页面,携带habit id
                    ARouter.getInstance().build("/habitdetail/activity")
                            .withLong("id", (int) habit.getId());
                } else {
                    viewHolder.checkBox.setChecked(true);
                    viewHolder.itemView.setBackground(mContext.getResources()
                            .getDrawable(R.drawable.shape_rv_item_selected));
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

        public GoodHabitViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvMainTitle = itemView.findViewById(R.id.tv_main_title);
            this.tvSubTitle = itemView.findViewById(R.id.tv_sub_title);
            this.checkBox = itemView.findViewById(R.id.checkbox);
            this.tvClock = itemView.findViewById(R.id.tv_clock_time);
            this.tvKeepDays = itemView.findViewById(R.id.tv_keep_on_days);
            this.itemView = itemView;
        }
    }
}


