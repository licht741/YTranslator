package com.licht.ytranslator.ui.TranslateView;

public interface ITranslateView {
    void setInputText(String text);
    void setTranslatedText(String inputText, String outputText);

    void setLanguagePair(String source, String destination);
    void openDictionary(String word, String direction);

    void detailsAreAvailable(boolean isVisible);

    void setIsStarredView(boolean isStarred);
    void isStarVisible(boolean isVisible);

    void onLanguageChanges();
    void onLanguagesSwapped();
}
