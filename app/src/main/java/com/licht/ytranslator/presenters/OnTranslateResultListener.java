package com.licht.ytranslator.presenters;

import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.Word;

public interface OnTranslateResultListener {
    void onTranslateResult(HistoryObject historyObject);
    void onDictionaryResult(Word w);
}
