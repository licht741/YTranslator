package com.licht.ytranslator.ui.TranslateView;

public interface ITranslateView {
    /**
     * Устанавливает переданный текст в поле ввода
     *
     * @param text Текст, устанавливаемый в поле ввода
     */
    void setInputText(String text);

    /**
     * Передаёт перевод на введённый текст.
     *
     * @param inputText Введённый текст
     * @param outputText Полученный перевод
     */
    void setTranslatedText(String inputText, String outputText);

    /**
     * Передаёт пару языков используемых при переводе
     *
     * @param source Язык, с которого осуществляется перевод
     * @param destination Язык, на который осуществляется перевод
     */
    void setLanguagePair(String source, String destination);

    /**
     * Открывает детализацию перевода с переданными параметрами
     *
     * @param word Переводимый текст
     * @param direction Направление перевода
     */
    void openDictionary(String word, String direction);

    /**
     * Передаёт возможность открытия детального перевода текста (полученного через API Яндекс Словаря)
     *
     * @param available True, если детальный перевод доступен, иначе False
     */
    void detailsAreAvailable(boolean available);

    /**
     * Передаёт избранность перевода (был ли он добавлен в избранное)
     *
     * @param isStarred True, если перевод в избранном, иначе False.
     */
    void isStarredText(boolean isStarred);

    /**
     * Вызывается когда один из языков был изменён, и необходимо обновить перевод
     */
    void onLanguageChanges();

    /**
     * Вызывается, когда изменилось направление перевода
     */
    void onLanguagesSwapped();

    /**
     * Начинает голосовой ввод с указанным языком
     *
     * @param inputLanguageSym Язык, на котором осуществляется голосовой ввод
     */
    void startAudioWithInputLanguage(String inputLanguageSym);

    /**
     * Сообщает о том, что получить перевод не получилось, и необходимо сообщить об этом пользователю
     */
    void onTranslateFailure();

    /**
     * Использовать механизм шаринга текста
     *
     * @param content Текст для шаринга
     */
    void shareText(String content);
}
