package com.yousheng.yousheng.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.yousheng.yousheng.R;

public class EvaluationPopupWindow extends PopupWindow {
    private ImageView ivStarOne;
    private ImageView ivStarTwo;
    private ImageView ivStarThree;
    private ImageView ivStarFour;
    private ImageView ivStarFive;

    public EvaluationPopupWindow(Context context) {
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

        view.findViewById(R.id.btn_comment).setOnClickListener(listener);
        view.setOnClickListener(listener);
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
                    break;
                case R.id.iv_star_two:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_empty);
                    break;
                case R.id.iv_star_three:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_empty);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_empty);
                    break;
                case R.id.iv_star_four:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_empty);
                    break;
                case R.id.iv_star_five:
                    ivStarOne.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarTwo.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarThree.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFour.setImageResource(R.drawable.evaluation_star_fill);
                    ivStarFive.setImageResource(R.drawable.evaluation_star_fill);
                    break;

                case R.id.btn_comment:
                    break;
            }
        }
    };
}
