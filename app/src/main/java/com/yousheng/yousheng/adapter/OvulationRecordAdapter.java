package com.yousheng.yousheng.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.OvulationRecord;
import com.yousheng.yousheng.uitl.CalendarUtils;

import java.util.List;

public class OvulationRecordAdapter extends RecyclerView.Adapter<OvulationRecordAdapter.OvulationRecordHolder> {
    private List<OvulationRecord> mRecords;
    private LayoutInflater mInflater;
    private Context mContext;


    public  OvulationRecordAdapter(Context context, List<OvulationRecord> recordList) {
        this.mInflater = LayoutInflater.from(context);
        this.mRecords = recordList;
        this.mContext = context;
    }

    @NonNull
    @Override
    public OvulationRecordHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        OvulationRecordHolder viewHolder =
                new OvulationRecordHolder(mInflater.inflate(R.layout.layout_item_ovulation_list, null));
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OvulationRecordHolder holder, int index) {
        OvulationRecord record = mRecords.get(index);
        holder.tvDate.setText(CalendarUtils.formatDateString(record.getDate(), "yyyy/MM/dd HH:mm"));

        switch (record.getState()) {
            case OvulationRecord.STATE_WEAKEST:
                holder.ivState.setImageResource(R.drawable.ovulation_level_one);
                holder.tvLevel.setText(mContext.getResources().getString(R.string.ovulation_level_one));
                break;
            case OvulationRecord.STATE_WEAK:
                holder.ivState.setImageResource(R.drawable.ovulation_level_two);
                holder.tvLevel.setText(mContext.getResources().getString(R.string.ovulation_level_two));
                break;
            case OvulationRecord.STATE_STRONG:
                holder.ivState.setImageResource(R.drawable.ovulation_level_three);
                holder.tvLevel.setText(mContext.getResources().getString(R.string.ovulation_level_three));
                break;
            case OvulationRecord.STATE_STRONGEST:
                holder.ivState.setImageResource(R.drawable.ovulation_level_four);
                holder.tvLevel.setText(mContext.getResources().getString(R.string.ovulation_level_four));
                break;
        }
        holder.ivMakeLove.setVisibility(record.isHasMakeLove() ? View.VISIBLE : View.GONE);
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    static class OvulationRecordHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvDate;
        ImageView ivState;
        ImageView ivMakeLove;
        TextView tvLevel;

        public OvulationRecordHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvDate = itemView.findViewById(R.id.tv_date);
            this.ivState = itemView.findViewById(R.id.iv_pic);
            this.ivMakeLove = itemView.findViewById(R.id.iv_ml);
            this.tvLevel = itemView.findViewById(R.id.tv_level);
        }
    }
}
