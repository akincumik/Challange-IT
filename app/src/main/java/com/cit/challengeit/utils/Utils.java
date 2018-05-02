package com.cit.challengeit.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cit.challengeit.R;

public class Utils {

    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void hideKeyboard(Activity activity) {
        try {
            View view = activity.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setUserPhoto(String photo, ImageView iv) {
        if (TextUtils.isEmpty(photo)) {
            iv.setImageResource(R.drawable.ic_person_black_24dp);
        } else {
            Glide.with(iv.getContext()).load(photo).into(iv);
        }
    }
}
