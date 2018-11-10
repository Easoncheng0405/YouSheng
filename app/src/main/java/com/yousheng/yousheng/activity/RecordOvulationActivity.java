package com.yousheng.yousheng.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.List;

@Route(path = "/recordovulation/activity")
public class RecordOvulationActivity extends AppCompatActivity implements View.OnClickListener {
    private CommonTitleBar titleBar;

    private CustomDatePicker mDatePicker;
    /****检测时间*/
    private TextView tvRecordDate;

    /****做爱开关**/
    private SwitchCompat switchMakeLove;

    /****当前选中的时间*/
    private long mTimeMillis;

    /****试纸结果*/
    private int mOvulationResult;

    /***RadioButtons*/
    private RadioButton rdLevelOne;
    private RadioButton rdLevelTwo;
    private RadioButton rdLevelThree;
    private RadioButton rdLevelFour;
    private RadioButton[] radioButtons;

    /***当前的记录*/
    private OvulationRecord mRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_ovulation);
        initView();
        initViewState();
    }

    private void initViewState() {
        Intent intent = getIntent();
        if (intent != null && intent.getBooleanExtra("change", false)) {
            long date = intent.getLongExtra("date", 0);
            List<OvulationRecord> list = LitePal.select()
                    .where("date = ?", String.valueOf(date))
                    .find(OvulationRecord.class);
            if (list != null && list.size() > 0) {
                mRecord = list.get(0);

                //change view state
                titleBar.getRightTextView().setVisibility(View.VISIBLE);
                titleBar.getLeftTextView().setText("试纸结果修改");
                updateRecordTime(mRecord.getDate());
                switchMakeLove.setChecked(mRecord.isHasMakeLove());

                switch (mRecord.getState()) {
                    case OvulationRecord.STATE_WEAKEST:
                        changeRadioButtonState(rdLevelOne, true);
                        break;
                    case OvulationRecord.STATE_WEAK:
                        changeRadioButtonState(rdLevelTwo, true);
                        break;
                    case OvulationRecord.STATE_STRONG:
                        changeRadioButtonState(rdLevelThree, true);
                        break;
                    case OvulationRecord.STATE_STRONGEST:
                        changeRadioButtonState(rdLevelFour, true);
                        break;
                    case OvulationRecord.STATE_DEFAULT:
                        break;
                }
            }
        }
    }

    private void initView() {
        //init title-bar
        titleBar = findViewById(R.id.title);
        TitleBarUtils.changeTitleImageLeftMargin(this, titleBar);
        titleBar.getRightTextView().setVisibility(View.GONE);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                switch (action) {
                    case CommonTitleBar.ACTION_LEFT_TEXT:
                        finish();
                        break;
                    case CommonTitleBar.ACTION_RIGHT_TEXT:
                        if (mRecord != null) {
                            mRecord.delete();
                            setResult(Constants.RESULT_CODE_HAS_CHANGE);
                            finish();
                        }
                        break;
                }
            }
        });

        rdLevelOne = findViewById(R.id.rd_one);
        rdLevelTwo = findViewById(R.id.rd_two);
        rdLevelThree = findViewById(R.id.rd_three);
        rdLevelFour = findViewById(R.id.rd_four);

        radioButtons = new RadioButton[4];
        radioButtons[0] = rdLevelOne;
        radioButtons[1] = rdLevelTwo;
        radioButtons[2] = rdLevelThree;
        radioButtons[3] = rdLevelFour;

        CompoundButton.OnCheckedChangeListener listener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked) return;
                changeRadioButtonState((RadioButton) buttonView, isChecked);
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
        tvRecordDate.append(CalendarUtils.formatDateString(timeMillis, "yyyy/MM/dd HH:mm"));
    }

    /**
     * @param isChecked radioButton的最终状态，是否被选中
     */
    private void changeRadioButtonState(RadioButton changedRadioButton, boolean isChecked) {
        if (isChecked) {
            for (RadioButton button : radioButtons) {
                if (button != changedRadioButton) {
                    button.setChecked(false);
                }
            }

            switch (changedRadioButton.getId()) {
                case R.id.rd_one:
                    mOvulationResult = OvulationRecord.STATE_WEAKEST;
                    rdLevelOne.setChecked(isChecked);
                    break;
                case R.id.rd_two:
                    mOvulationResult = OvulationRecord.STATE_WEAK;
                    rdLevelTwo.setChecked(isChecked);
                    break;
                case R.id.rd_three:
                    mOvulationResult = OvulationRecord.STATE_STRONG;
                    rdLevelThree.setChecked(isChecked);
                    break;
                case R.id.rd_four:
                    mOvulationResult = OvulationRecord.STATE_STRONGEST;
                    rdLevelFour.setChecked(isChecked);
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
                OvulationRecord record = mRecord;
                if (record == null) {
                    record = new OvulationRecord();
                }
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
