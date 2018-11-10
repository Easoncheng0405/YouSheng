package com.yousheng.yousheng.view;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yousheng.yousheng.R;
import com.yousheng.yousheng.activity.FeedBackActivity;
import com.yousheng.yousheng.manager.FeedBackManager;
import com.yousheng.yousheng.uitl.time.MarketUtils;

public class EvaluationPopupWindow extends PopupWindow {
    private ImageView ivStarOne;
    private ImageView ivStarTwo;
    private ImageView ivStarThree;
    private ImageView ivStarFour;
    private ImageView ivStarFive;

    private Button btnJump;

    private enum BUTTON_STATE {
        GOOD,
        BAD
    }

    private BUTTON_STATE mState;

    private Activity mActivity;

    public EvaluationPopupWindow(Activity context) {
        this.mActivity = context;
        View view = LayoutInflater.from(context)
                .inflate(R.layout.layout_evaluation_popwindow, null);
        setContentView(view);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

        ivStarFive = view.findViewById(R.id.iv_star_five);
        ivStarFour = view.findViewById(R.id.iv_star_four);
        ivStarThree = view.findViewById(R.id.iv_star_three);
        ivStarTwo = view.findViewById(R.id.iv_star_two);
        ivStarOne = view.findViewById(R.id.iv_star_one);

        ivStarOne.setOnClickListener(listener);
        ivStarTwo.setOnClickListener(listener);
        ivStarThree.setOnClickListener(listener);
        ivStarFour.setOnClickListener(listener);
        ivStarFive.setOnClickListener(listener);

        btnJump = view.findViewById(R.id.btn_comment);
        setButtonText(BUTTON_STATE.BAD);
        view.findViewById(R.id.btn_comment).setOnClickListener(listener);
        view.setOnClickListener(listener);
    }

    private void setButtonText(BUTTON_STATE state) {
        if (state == BUTTON_STATE.GOOD) {
            mState = BUTTON_STATE.GOOD;
            btnJump.setText("去鼓励");
        } else if (state == BUTTON_STATE.BAD) {
            mState = BUTTON_STATE.BAD;
            btnJump.setText("去吐槽");
        }
    }

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.root_layout:
                    dismiss();
                    break;
                case R.id.iv_close:
                    dismiss();
                    break;
                case R.id.iv_star_one:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_empty);
                    setButtonText(BUTTON_STATE.BAD);
                    break;
                case R.id.iv_star_two:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_empty);
                    setButtonText(BUTTON_STATE.BAD);
                    break;
                case R.id.iv_star_three:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_empty);
                    setButtonText(BUTTON_STATE.BAD);
                    break;
                case R.id.iv_star_four:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_empty);
                    setButtonText(BUTTON_STATE.BAD);
                    break;
                case R.id.iv_star_five:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_fill);
                    setButtonText(BUTTON_STATE.GOOD);
                    break;

                case R.id.btn_comment:
                    FeedBackManager.recordFeedBackButtonClicked();
                    if (mState == BUTTON_STATE.GOOD) {
                        //跳转到评价界面
                        MarketUtils.navigationToAppStore(mActivity);
                        dismiss();
                    } else if (mState == BUTTON_STATE.BAD) {
                        mActivity.startActivity(new Intent(mActivity, FeedBackActivity.class));
                        dismiss();
                    }
                    break;
            }
        }
    };
}
