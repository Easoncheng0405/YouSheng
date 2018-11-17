package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.alibaba.android.arouter.launcher.ARouter;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.adapter.OvulationRecordAdapter;
import com.yousheng.yousheng.model.OvulationRecord;
import com.yousheng.yousheng.uitl.TitleBarUtils;

import org.litepal.LitePal;
import org.litepal.crud.callback.FindMultiCallback;

import java.util.List;

public class OvulationActivity extends AppCompatActivity implements View.OnClickListener {
    private RecyclerView mRecyclerView;
    private OvulationRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovulation);
        initView();
    }

    private void initView() {
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        TitleBarUtils.addTitleBarListener(this, titleBar);
        mRecyclerView = findViewById(R.id.listview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        updateList();
    }

    private void updateList() {
        LitePal.select()
                .order("date desc")
                .findAsync(OvulationRecord.class)
                .listen(new FindMultiCallback<OvulationRecord>() {
                    @Override
                    public void onFinish(List<OvulationRecord> list) {
                        if (list != null && list.size() > 0) {
                            findViewById(R.id.tv_empty).setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            mAdapter = new OvulationRecordAdapter(OvulationActivity.this, list);
                            mRecyclerView.setAdapter(mAdapter);
                        } else {
                            findViewById(R.id.tv_empty).setVisibility(View.VISIBLE);
                            mRecyclerView.setVisibility(View.GONE);
                        }
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_record_ovulation:
                startActivityForResult(new Intent(OvulationActivity.this,
                        RecordOvulationActivity.class), Constants.REQUEST_CODE_OVULATION_TO_RECORD);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Constants.RESULT_CODE_HAS_CHANGE:
                updateList();
                break;
            case Constants.RESULT_CODE_NO_CHANGE:
                break;
            default:
                break;
        }
    }
}
