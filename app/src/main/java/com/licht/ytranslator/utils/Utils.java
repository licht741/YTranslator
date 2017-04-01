package com.licht.ytranslator.utils;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.licht.ytranslator.YTransApp;

public class Utils {
    public static void hideKeyboard(Activity activity)
    {
        if (activity.getCurrentFocus() == null)
            return;

        InputMethodManager inputMethodManager =
                (InputMethodManager)  activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }
}
