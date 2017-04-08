package com.licht.ytranslator.presenters;

import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.DictionaryObject;

public interface OnTranslateResultListener {
    void onTranslateResult(HistoryObject historyObject);
    void onTranslateFailure();

    void onDictionaryResult(DictionaryObject w);
}
