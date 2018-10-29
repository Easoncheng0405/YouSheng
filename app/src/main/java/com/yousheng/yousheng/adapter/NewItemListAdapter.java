package com.yousheng.yousheng.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.NewItem;
import com.yousheng.yousheng.notify.NewItemActivity;
import com.yousheng.yousheng.notify.NewItemHelper;
import com.yousheng.yousheng.uitl.CalendarUtils;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class NewItemListAdapter extends RecyclerView.Adapter<NewItemListAdapter.NewItemViewHolder> {
    private Activity mContext;
    private List<NewItem> mDatas;
    private NewItemHelper.TimeRange mTimeRange;

    public NewItemListAdapter(Activity context, NewItemHelper.TimeRange timeRange) {
        this.mDatas = NewItemHelper.getAllNewItemInRange().get(timeRange);
        this.mContext = context;
        this.mTimeRange = timeRange;
    }

    @NonNull
    @Override
    public NewItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new NewItemViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.layout_item_newitem, null));
    }

    @Override
    public void onBindViewHolder(@NonNull final NewItemViewHolder viewHolder, final int index) {
        final NewItem newItem = mDatas.get(index);
        viewHolder.tvTitle.setText(newItem.getContent());
        viewHolder.checkBox.setChecked(false);
        if (mTimeRange == NewItemHelper.TimeRange.TODAY || mTimeRange == NewItemHelper.TimeRange.TOMORROW) {
            viewHolder.tvDate.setText(DateFormat.format("HH:mm", newItem.getTime()));
        } else if (mTimeRange != NewItemHelper.TimeRange.NO_DATE) {
            viewHolder.tvDate.setText(CalendarUtils.formatDateString(newItem.getTime(), "MM/dd"));
        }

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                LitePal.find(NewItem.class, newItem.getId()).delete();
                notifyItemRemoved(index);
                mDatas.remove(newItem);
                viewHolder.checkBox.setChecked(false);
                initItemTitle();
            }
        });

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewItemActivity.class);
                intent.putExtra("id", newItem.getId());
                mContext.startActivityForResult(intent, Constants.REQUEST_CODE_MAIN_TO_NEWITEM);
            }
        });
    }

    public void notifyDataUpdate() {
        mDatas = NewItemHelper.getAllNewItemInRange().get(mTimeRange);
        this.notifyDataSetChanged();

    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    static class NewItemViewHolder extends RecyclerView.ViewHolder {
        private View itemView;
        private TextView tvTitle;
        private TextView tvDate;
        private CheckBox checkBox;

        NewItemViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvTitle = itemView.findViewById(R.id.tv_title);
            this.tvDate = itemView.findViewById(R.id.tv_date);
            this.checkBox = itemView.findViewById(R.id.cb_complete);
        }
    }

    private void initItemTitle() {
        HashMap<NewItemHelper.TimeRange, ArrayList<NewItem>> map = NewItemHelper.getAllNewItemInRange();
        if (map.get(NewItemHelper.TimeRange.NO_DATE).size() > 0)
            mContext.findViewById(R.id.noDate).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.noDate).setVisibility(View.GONE);

        if (map.get(NewItemHelper.TimeRange.UP_TO_DATE).size() > 0)
            mContext.findViewById(R.id.upToDate).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.upToDate).setVisibility(View.GONE);


        if (map.get(NewItemHelper.TimeRange.TOMORROW).size() > 0)
            mContext.findViewById(R.id.tomorrow).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.tomorrow).setVisibility(View.GONE);


        if (map.get(NewItemHelper.TimeRange.TODAY).size() > 0)
            mContext.findViewById(R.id.today).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.today).setVisibility(View.GONE);


        if (map.get(NewItemHelper.TimeRange.IN_WEEK).size() > 0)
            mContext.findViewById(R.id.week).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.week).setVisibility(View.GONE);

        if (map.get(NewItemHelper.TimeRange.FUTURE).size() > 0)
            mContext.findViewById(R.id.future).setVisibility(View.VISIBLE);
        else
            mContext.findViewById(R.id.future).setVisibility(View.GONE);
    }
}
