package com.licht.ytranslator.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.licht.ytranslator.data.model.ExampleObject;
import com.licht.ytranslator.data.model.StringWrapper;
import com.licht.ytranslator.data.model.WordMeaningObject;

public class Utils {
    public static void hideKeyboard(Activity activity) {
        if (activity.getCurrentFocus() == null)
            return;

        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
    }

    public static String getFormattedTextToShare(WordMeaningObject object) {
        final StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(object.getText()).append(" (").append(object.getPos()).append(")\n");

        if (object.getMeanings().size() > 0) {
            stringBuilder.append("Значения: ");
            for (StringWrapper s : object.getMeanings())
                stringBuilder.append(s.getContent()).append(", ");
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append("\n");
        }

        if (object.getSynonimes().size() > 0) {
            stringBuilder.append("Синонимы: ");
            for (StringWrapper s : object.getSynonimes())
                stringBuilder.append(s.getContent()).append(", ");
            stringBuilder.delete(stringBuilder.length() - 2, stringBuilder.length());
            stringBuilder.append("\n");
        }

        if (object.getExampleObjects().size() > 0) {
            stringBuilder.append("Примеры использования:\n");
            for (ExampleObject example : object.getExampleObjects()) {
                stringBuilder.append(example.getPhrase().getContent());
                if (example.getTranslates().size() > 0) {
                    stringBuilder.append(" - ");
                    for (StringWrapper trans: example.getTranslates())
                        stringBuilder.append(trans.getContent()).append("; ");
                }
                stringBuilder.append("\n");
            }
        }

        return stringBuilder.toString();
    }

    public static String formattedTranslatingToShare(String text, String translate) {
        return String.format("%s - %s", text, translate);
    }

    /*
     * Сокращает слово.
     * По умолчанию оставляет 3 символа. Если 3-й символ - гласная, то добавляет букв до первой
     * согласной включительно
     */
    public static String cutWord(String word) {
        // Приложение поддерживает русскую и английскую локализацию, поэтому достаточно только их гласных
        final String englishAndRussianVowels = "aeiouауоыиэяюёе";

        int endIndex = 2;
        char currentSymbol = word.charAt(endIndex);
        while (englishAndRussianVowels.contains(String.valueOf(currentSymbol))) {
            ++endIndex;
            currentSymbol = word.charAt(endIndex);
        }

        return word.substring(0, endIndex + 1);
    }

    public static Intent createIntentToSharing(String content) {
        final Intent intent = new Intent();

        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(intent.EXTRA_TEXT, content);
        intent.setType("text/plain");

        return intent;
    }

    public static void showToast(Context context, CharSequence text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

}
