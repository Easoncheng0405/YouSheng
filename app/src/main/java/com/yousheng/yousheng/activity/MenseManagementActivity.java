package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.PrefConstants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.SPSingleton;
import com.yousheng.yousheng.uitl.TitleBarUtils;

@Route(path = "/mensemanagement/activity")
public class MenseManagementActivity extends AppCompatActivity {
    private CustomDatePicker mMenseDurationPicker;
    private CustomDatePicker mMenseDaysPicker;
    private CustomDatePicker mMenseStartDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mense_management);
        initView();
    }

    private void initView() {
        CommonTitleBar titleBar = findViewById(R.id.mainTitle);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
//        TitleBarUtils.addTitleBarListener(this, titleBar);

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
        final TextView tvDaysMenseDays = layoutMenseDays.findViewById(R.id.tv_days);
        final TextView tvDaysMenseStart = layoutStartDay.findViewById(R.id.tv_days);
        tvDaysMenseDuration.setText("28");
        tvDaysMenseDays.setText("6");
        tvDaysMenseStart.setText(CalendarUtils.formatDateString(System.currentTimeMillis(), "MM/dd"));

        //init listener
        layoutMenseDuration.setOnClickListener(mClickListener);
        layoutMenseDays.setOnClickListener(mClickListener);
        layoutStartDay.setOnClickListener(mClickListener);

        //init date picker
        mMenseDurationPicker =
                new CustomDatePicker.Builder()
                        .setContext(this)
                        .setStartDate("2012-12-12")
                        .setEndDate("2018-10-19")
                        .setDayModeOn(true)
                        .setDayMode(CustomDatePicker.DAY_MODE.DURATION)
                        .setTitle(getResources().getString(R.string.mense_management))
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String time, long timeMills) {
                                tvDaysMenseDuration.setText(time);
                                SPSingleton.get()
                                        .putString(PrefConstants.PREFS_KEY_MENSE_DURATION, time);
                            }
                        })
                        .create();

        mMenseDaysPicker =
                new CustomDatePicker.Builder()
                        .setContext(this)
                        .setStartDate("2012-12-12")
                        .setEndDate("2018-10-19")
                        .setDayModeOn(true)
                        .setDayMode(CustomDatePicker.DAY_MODE.DAYS)
                        .setTitle(getResources().getString(R.string.mense_days_main_title))
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String time, long timeMills) {
                                tvDaysMenseDays.setText(time);
                                SPSingleton.get()
                                        .putString(PrefConstants.PREFS_KEY_MENSE_DAYS, time);
                            }
                        })
                        .create();

        mMenseStartDatePicker =
                new CustomDatePicker.Builder()
                        .setContext(this)
                        .setTitle(getResources().getString(R.string.mense_start_day_main_title))
                        .setStartDate("2010-01-01 01:01")
                        .setEndDate(CalendarUtils
                                .formatDateString(System.currentTimeMillis(), "yyyy-MM-dd hh:mm"))
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String time, long timeMills) {
                                tvDaysMenseStart.setText(time.split(" ")[0]);
                                SPSingleton.get()
                                        .putLong(PrefConstants.PREFS_KEY_MENSE_START_DAY, timeMills);
                            }
                        })
                        .create();
        mMenseStartDatePicker.hideTimeUnit(CustomDatePicker.SCROLL_TYPE.HOUR,
                CustomDatePicker.SCROLL_TYPE.MINUTE);

        Button btnStart = findViewById(R.id.btn_start);
        btnStart.setOnClickListener(mClickListener);

        //init switch
        final SwitchCompat switchNotify = layoutMenseNotify.findViewById(R.id.switch_mense);
        final SwitchCompat switchPregnant = layoutMenseMode.findViewById(R.id.switch_mense);

        Switch.OnCheckedChangeListener checkListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (compoundButton.equals(switchNotify)) {
                    SPSingleton.get().putBoolean(PrefConstants.PREFS_KEY_MENSE_NOTIFY, !isChecked);
                } else if (compoundButton.equals(switchPregnant)) {
                    SPSingleton.get().putBoolean(PrefConstants.PREFS_KEY_MENSE_MODE, !isChecked);
                }
            }
        };
        switchNotify.setOnCheckedChangeListener(checkListener);
        switchPregnant.setOnCheckedChangeListener(checkListener);

        boolean isNotifyOn = SPSingleton.get().getBoolean(PrefConstants.PREFS_KEY_MENSE_NOTIFY,
                true);
        boolean isMenseModeOn = SPSingleton.get().getBoolean(PrefConstants.PREFS_KEY_MENSE_MODE,
                true);

        switchNotify.setChecked(true);
        switchPregnant.setChecked(true);

        tvDaysMenseDuration.setText(SPSingleton.get()
                .getString(PrefConstants.PREFS_KEY_MENSE_DURATION, Constants.DEFAULT_MENSE_GAP));
        tvDaysMenseDays.setText(SPSingleton.get()
                .getString(PrefConstants.PREFS_KEY_MENSE_DAYS, Constants.DEFAULT_MENSE_DURAION));
        tvDaysMenseStart.setText(CalendarUtils.formatDateString(SPSingleton.get()
                .getLong(PrefConstants.PREFS_KEY_MENSE_START_DAY, System.currentTimeMillis()), "MM/dd"));
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.layout_mense_duration:
                    mMenseDurationPicker.show("2018-10-10");
                    break;
                case R.id.layout_mense_days:
                    mMenseDaysPicker.show("2018-10-10");
                    break;

                case R.id.layout_mense_start:
                    mMenseStartDatePicker.show(CalendarUtils
                            .formatDateString(System.currentTimeMillis(), "yyyy-MM-dd"));
                    break;
                case R.id.layout_mense_notify:
                    break;
                case R.id.layout_mense_mode:
                    break;

                case R.id.btn_start:
                    SPSingleton.get()
                            .putBoolean(PrefConstants.PREFS_KEY_MENSE_SAVED, true);
                    setResult(Constants.RESULT_CODE_MENSE_START_DAY_CHANGED);
                    finish();
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
