package com.yousheng.yousheng.uitl;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;

public class TextUtils {
    public static SpannableString getSpannableString(int color, String text) {
        SpannableString spannableString = new SpannableString(text);
        spannableString.setSpan(new ForegroundColorSpan(color), 0, text.length(),
                Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        return spannableString;
    }
}
