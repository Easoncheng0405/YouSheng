package com.yousheng.yousheng.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.yousheng.yousheng.R;
import com.yousheng.yousheng.uitl.TitleBarUtils;

@Route(path = "/recordovulation/activity")
public class RecordOvulationActivity extends AppCompatActivity {

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
        }
    }
}
