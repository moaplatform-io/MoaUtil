package com.moaplanet.gosing.utils;

import java.util.regex.Pattern;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.widget.Toast;

import com.moaplanet.gosing.R;

public class StringUtil {

    public static SpannableStringBuilder convertFontColor(String str, int color) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static SpannableStringBuilder convertFontColorSize(String str, int color, int size) {
        SpannableStringBuilder sp = new SpannableStringBuilder(str);
        sp.setSpan(new AbsoluteSizeSpan(size, false), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sp.setSpan(new ForegroundColorSpan(color), 0, str.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sp;
    }

    public static void clipBoard(Context context, String label, String message) {
        if (context != null && message != null) {
            ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText(label, message);
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, context.getString(R.string.common_toast_msg_copy_clip_baord), Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 핸드폰번호 유효성 체크
     */
    public static boolean isPhoneNumber(String phoneNumber) {

        if (phoneNumber.contains("-")) {
            return Pattern.matches("^01(?:0|1|[6-9])-(?:\\d{3}|\\d{4})-(\\d{4})$", phoneNumber);
        } else {
            return Pattern.matches("^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})(\\d{4})$", phoneNumber);
        }
    }

}
