package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.Constants;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.model.OvulationRecord;
import com.yousheng.yousheng.timepickerlib.CustomDatePicker;
import com.yousheng.yousheng.uitl.CalendarUtils;
import com.yousheng.yousheng.uitl.TitleBarUtils;

import org.litepal.LitePal;

@Route(path = "/recordovulation/activity")
public class RecordOvulationActivity extends AppCompatActivity implements View.OnClickListener {
    private CustomDatePicker mDatePicker;

    /****检测时间*/
    private TextView tvRecordDate;

    /****做爱开关**/
    private SwitchCompat switchMakeLove;

    /****当前选中的时间*/
    private long mTimeMillis;

    /****试纸结果*/
    private int mOvulationResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_ovulation);
        initView();
    }

    private void initView() {
        CommonTitleBar titleBar = findViewById(R.id.title);
        TitleBarUtils.addTitleBarListener(this, titleBar);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);

        RadioButton rdLevelOne = findViewById(R.id.rd_one);
        RadioButton rdLevelTwo = findViewById(R.id.rd_two);
        RadioButton rdLevelThree = findViewById(R.id.rd_three);
        RadioButton rdLevelFour = findViewById(R.id.rd_four);

        final RadioButton[] buttons = new RadioButton[4];
        buttons[0] = rdLevelOne;
        buttons[1] = rdLevelTwo;
        buttons[2] = rdLevelThree;
        buttons[3] = rdLevelFour;

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeRadioButtonState((RadioButton) buttonView, isChecked, buttons);
            }
        };

        rdLevelOne.setOnCheckedChangeListener(listener);
        rdLevelTwo.setOnCheckedChangeListener(listener);
        rdLevelThree.setOnCheckedChangeListener(listener);
        rdLevelFour.setOnCheckedChangeListener(listener);

        //init datePicker
        mDatePicker =
                new CustomDatePicker
                        .Builder()
                        .setContext(this)
                        .setStartDate("2010-01-01 00:00")
                        .setEndDate(CalendarUtils.formatDateString(System.currentTimeMillis(), "yyyy-MM-dd hh:mm"))
                        .setTitle("试纸测试时间录入")
                        .setResultHandler(new CustomDatePicker.ResultHandler() {
                            @Override
                            public void handle(String time, long timeMills) {
                                updateRecordTime(timeMills);
                            }
                        })
                        .create();
        mDatePicker.showSpecificTime(true);

        tvRecordDate = findViewById(R.id.tv_record_date);
        switchMakeLove = findViewById(R.id.switch_make_love);
        updateRecordTime(System.currentTimeMillis());
    }

    private void updateRecordTime(long timeMillis) {
        mTimeMillis = timeMillis;
        tvRecordDate.setText("检测时间:");
        tvRecordDate.append(CalendarUtils.formatDateString(timeMillis, "yyyy/MM/dd hh:mm"));
    }

    /**
     * @param isChecked radioButton的最终状态，是否被选中
     */
    private void changeRadioButtonState(RadioButton changedRadioButton, boolean isChecked, RadioButton[] radioButtons) {
        if (isChecked) {
            for (RadioButton button : radioButtons) {
                if (button != changedRadioButton) {
                    button.setChecked(false);
                }
            }

            switch (changedRadioButton.getId()) {
                case R.id.rd_one:
                    mOvulationResult = OvulationRecord.STATE_WEAKEST;
                    break;
                case R.id.rd_two:
                    mOvulationResult = OvulationRecord.STATE_WEAK;
                    break;
                case R.id.rd_three:
                    mOvulationResult = OvulationRecord.STATE_STRONG;
                    break;
                case R.id.rd_four:
                    mOvulationResult = OvulationRecord.STATE_STRONGEST;
                    break;
            }
        } else {
            mOvulationResult = OvulationRecord.STATE_DEFAULT;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout_record_ovulation_time:
                mDatePicker.show(CalendarUtils.formatDateString(System.currentTimeMillis(),
                        "yyyy-MM-dd"));
                break;
            case R.id.btn_ensure:
                OvulationRecord record = new OvulationRecord();
                record.setDate(mTimeMillis);
                record.setState(mOvulationResult);
                record.setHasMakeLove(switchMakeLove.isChecked());
                record.save();
                setResult(Constants.RESULT_CODE_HAS_CHANGE);
                finish();
                break;
            case R.id.layout_instruction:
                startActivity(new Intent(RecordOvulationActivity.this,
                        OvulationInstructionActivity.class));
                break;
        }
    }
}
