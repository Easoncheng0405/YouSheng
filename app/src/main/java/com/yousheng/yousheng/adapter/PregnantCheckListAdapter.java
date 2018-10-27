package com.yousheng.yousheng.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.yousheng.yousheng.R;
import com.yousheng.yousheng.activity.PregnantCheckDetailActivity;

public class PregnantCheckListAdapter extends
        RecyclerView.Adapter<PregnantCheckListAdapter.PregnantCheckViewHolder> {
    private String[] mMainList;
    private String[] mSubList;
    private Context mContext;

    public PregnantCheckListAdapter(Context context) {
        this.mContext = context;
        this.mMainList = context.getResources().getStringArray(R.array.pregnant_check_main_title);
        this.mSubList = context.getResources().getStringArray(R.array.pregnant_check_sub_title);
    }

    @NonNull
    @Override
    public PregnantCheckViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        return new PregnantCheckViewHolder
                (LayoutInflater.from(mContext)
                        .inflate(R.layout.layout_item_pregnant_checkout_list, null));
    }

    @Override
    public void onBindViewHolder(@NonNull PregnantCheckViewHolder viewHolder, final int index) {
        String mainTitle = mMainList[index];
        String subTitle = mSubList[index];
        viewHolder.tvMainTitle.setText(mainTitle);
        viewHolder.tvSubTitle.setText(subTitle);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, PregnantCheckDetailActivity.class);
                intent.putExtra("title", mMainList[index]);
                intent.putExtra("index",index);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMainList.length;
    }

    static class PregnantCheckViewHolder extends RecyclerView.ViewHolder {
        View itemView;
        TextView tvMainTitle;
        TextView tvSubTitle;

        public PregnantCheckViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.tvMainTitle = itemView.findViewById(R.id.tv_main_title);
            this.tvSubTitle = itemView.findViewById(R.id.tv_sub_title);
        }
    }
}
