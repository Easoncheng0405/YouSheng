package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.TitleBarUtils;

@Route(path = "/mensemanagement/activity")
public class MenseManagementActivity extends AppCompatActivity {
    private CustomDatePicker mMenseDurationPicker;
    private CustomDatePicker mMenseDaysPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mense_management);
        initView();
    }

    private void initView() {
        CommonTitleBar titleBar = findViewById(R.id.mainTitle);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        TitleBarUtils.addTitleBarListener(this, titleBar);

        //layout_item_one
        View layoutMenseDuration = findViewById(R.id.layout_mense_duration);
        ((ImageView) layoutMenseDuration.findViewById(R.id.iv_logo)).setImageResource(R.drawable.mense_days);
        ((TextView) layoutMenseDuration.findViewById(R.id.tv_main_title)).setText(getResources()
                .getText(R.string.mense_duration_main_title));
        ((TextView) layoutMenseDuration.findViewById(R.id.tv_sub_title)).setText(getResources()
                .getText(R.string.mense_duration_sub_title));
        View layoutMenseDays = findViewById(R.id.layout_mense_days);
        ((TextView) layoutMenseDays.findViewById(R.id.tv_main_title)).setText(getResources()
                .getText(R.string.mense_days_main_title));
        ((TextView) layoutMenseDays.findViewById(R.id.tv_sub_title)).setText(getResources()
                .getText(R.string.mense_days_sub_title));
        View layoutStartDay = findViewById(R.id.layout_mense_start);
        ((TextView) layoutStartDay.findViewById(R.id.tv_main_title)).setText(getResources()
                .getText(R.string.mense_start_day_main_title));
        ((TextView) layoutStartDay.findViewById(R.id.tv_sub_title)).setText(getResources()
                .getText(R.string.mense_start_day_sub_title));

        //layout_item_two
        View layoutMenseNotify = findViewById(R.id.layout_mense_notify);
        View layoutMenseMode = findViewById(R.id.layout_mense_mode);
        ((ImageView) layoutMenseNotify.findViewById(R.id.iv_logo)).setImageResource(R.drawable.clock);
        ((ImageView) layoutMenseMode.findViewById(R.id.iv_logo)).setImageResource(R.drawable.mense);
        ((TextView) layoutMenseNotify.findViewById(R.id.tv_main_title))
                .setText(getResources().getString(R.string.mense_notify_main_title));
        ((TextView) layoutMenseNotify.findViewById(R.id.tv_sub_title))
                .setText(getResources().getString(R.string.mense_notify_sub_title));
        ((TextView) layoutMenseMode.findViewById(R.id.tv_main_title))
                .setText(getResources().getString(R.string.mense_pregnant_main_title));
        ((TextView) layoutMenseMode.findViewById(R.id.tv_sub_title))
                .setText(getResources().getString(R.string.mense_pregnant_sub_title));

        final TextView tvDaysMenseDuration = layoutMenseDuration.findViewById(R.id.tv_days);
        TextView tvDaysMenseDays = layoutMenseDays.findViewById(R.id.tv_days);
        TextView tvDaysMenseStart = layoutStartDay.findViewById(R.id.tv_days);

        //init listener
        layoutMenseDuration.setOnClickListener(mClickListener);

        //init date picker
        mMenseDurationPicker =
                new CustomDatePicker.Builder()
                        .setContext(this)
                        .setStartDate("2012-12-12")
                        .setEndDate("2018-10-19")
                        .setColor(getResources().getColor(R.color.text_color_red_theme))
                        .setDayModeOn(true)
                        .setTitle(getResources().getString(R.string.mense_management))
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String time) {
                                tvDaysMenseDuration.setText(time);
                            }
                        })
                        .create();
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_mense_duration:
                    mMenseDurationPicker.show("2018-10-10");
                    break;
                case R.id.layout_mense_days:
                    break;
                case R.id.layout_mense_start:
                    break;
                case R.id.layout_mense_notify:
                    break;
                case R.id.layout_mense_mode:
                    break;
            }
        }
    };

}
