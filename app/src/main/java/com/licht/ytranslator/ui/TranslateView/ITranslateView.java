package com.licht.ytranslator.ui.TranslateView;

public interface ITranslateView {
    void setTranslatedText(String text);
    void setInputText(String text);

    void setLanguagePair(String source, String destination);
    void openDictionary(String word, String direction);
}
