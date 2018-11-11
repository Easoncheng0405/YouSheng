package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.wuhenzhizao.titlebar.utils.ScreenUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.adapter.PregnantCheckListAdapter;
import com.yousheng.yousheng.adapter.RecyclerViewSpacesItemDecoration;
import com.yousheng.yousheng.uitl.TitleBarUtils;

import java.util.HashMap;

public class PregnantCheckActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pregnant_check);
        initView();
    }

    private void initView() {
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        TitleBarUtils.addTitleBarListener(this, titleBar);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PregnantCheckListAdapter adapter = new PregnantCheckListAdapter(this);
        recyclerView.setAdapter(adapter);
        HashMap<String, Integer> map = new HashMap<>();
        int padding = (int) ScreenUtils.dp2Px(PregnantCheckActivity.this,8);
        map.put(RecyclerViewSpacesItemDecoration.TOP_DECORATION, padding);
        map.put(RecyclerViewSpacesItemDecoration.LEFT_DECORATION, padding);
        map.put(RecyclerViewSpacesItemDecoration.RIGHT_DECORATION, padding);

        RecyclerViewSpacesItemDecoration itemDecoration = new RecyclerViewSpacesItemDecoration(map);
        recyclerView.addItemDecoration(itemDecoration);
    }
}
