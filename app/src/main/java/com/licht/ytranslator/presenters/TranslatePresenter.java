package com.licht.ytranslator.presenters;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.Word;
import com.licht.ytranslator.data.sources.TranslatePreferences;
import com.licht.ytranslator.loaders.TranslateLoader;
import com.licht.ytranslator.ui.TranslateView.ITranslateView;

import java.util.ArrayList;

public class TranslatePresenter implements IPresenter<ITranslateView>, OnTranslateResultListener {
    private DataManager dataManager;
    private TranslatePreferences translatePreferences;
    private TranslateLoader translateLoader;

    private ITranslateView view;

    public TranslatePresenter(DataManager dataManager,
                              TranslateLoader translateLoader,
                              TranslatePreferences translatePreferences) {
        super();
        this.dataManager = dataManager;
        this.translateLoader = translateLoader;
        this.translatePreferences = translatePreferences;

        translateLoader.setOnTranslateResultListener(this);
    }

    @Override
    public void bindView(ITranslateView iTranslateView) {
        this.view = iTranslateView;
    }

    @Override
    public void unbindView() {
        this.view = null;
    }

    /**
     * Инициализирует окно перевода значениями, которые были при закрытии
     */
    public void requestData() {
        String input = translatePreferences.getInputText();
        if (input == null) {
            input = "";
            translatePreferences.setInputText(input);
        }

        String translateDirection = translatePreferences.getTranslateDirection();
        if (translateDirection == null || "".equals(translateDirection)) {
            translateDirection = "en-ru";
            translatePreferences.setDirectionText(translateDirection);
        }

        initializeData(input, translateDirection);

    }

    /**
     * Инициализирует окно перевода переданными значениями
     *
     * @param inputText          Переводимый текст
     * @param translateDirection Направление перевода
     */
    public void initializeData(String inputText, String translateDirection) {
        final String[] languages = translateDirection.split("-");

        view.setInputText(inputText);
        view.setLanguagePair(dataManager.getLanguageByCode(languages[0]),
                dataManager.getLanguageByCode(languages[1]));
    }

    /**
     * Вызывается при изменении содержимого поля ввода текста на экране перевода
     *
     * @param content Новый текст
     */
    public void onTextInput(String content) {
        if (content == null)
            return;

        if ("".equals(content)) {
            view.setTranslatedText(content, "");
            return;
        }

        translatePreferences.setInputText(content);

        if (view != null) {
            view.detailsAreAvailable(false);
            view.isStarVisible(false);
        }

        translateText();
    }


    private void translateText() {
        final String text = translatePreferences.getInputText();
        final String direction = translatePreferences.getTranslateDirection();

        final String key = YTransApp.get().getString(R.string.key_translate);
        translateLoader.translate(key, text, direction);

        final String keyDict = YTransApp.get().getString(R.string.key_dictionary);
        translateLoader.getDictionaryMeanings(keyDict, text, direction);
    }

    public void onKeyboardHide() {
        // Если пользователь закрыл клавиатуру, то он просматривает перевод слова
        // В этой ситуации мы сохраняем слово в историю

        // Помечаем, что слово, которое мы ранее закэшировали, теперь входит в историю
        addWordToHistory();

    }

    /**
     * Вызывается при изменении пользователем исходного языка
     *
     * @param newSourceLanguage Название нового исходного языка, написанное в используемой локализации
     */
    public void onUpdateSourceLanguage(String newSourceLanguage) {
        // Переводим название языка в его кодовое обозначение
        final String langSymbol = dataManager.getLanguageSymbolByName(newSourceLanguage);

        final String currentDirection = translatePreferences.getTranslateDirection();
        final String[] tokens = currentDirection.split("-");
        final String newDirection = String.format("%s-%s", langSymbol, tokens[1]);
        translatePreferences.setDirectionText(newDirection);

        updateLanguagePairInView(newDirection);
        view.onLanguageChanges();
    }

    /**
     * Вызывается при изменении пользователем языка, на который осуществляется перевод
     *
     * @param newDestinationLanguage Название языка, написанное в используемой локализации
     */
    public void onUpdateDestinationLanguage(String newDestinationLanguage) {
        // Переводим название языка в его кодовое обозначение
        final String langSymbol = dataManager.getLanguageSymbolByName(newDestinationLanguage);

        final String currentDirection = translatePreferences.getTranslateDirection();
        final String[] tokens = currentDirection.split("-");
        final String newDirection = String.format("%s-%s", tokens[0], langSymbol);

        translatePreferences.setDirectionText(newDirection);

        updateLanguagePairInView(newDirection);
        view.onLanguageChanges();
    }

    public void onSwapLanguages() {
        final String currentDirection = translatePreferences.getTranslateDirection();
        final String[] tokens = currentDirection.split("-");
        final String newDirection = String.format("%s-%s", tokens[1], tokens[0]);
        translatePreferences.setDirectionText(newDirection);

        if (view == null)
            return;

        view.setLanguagePair(dataManager.getLanguageByCode(tokens[1]), dataManager.getLanguageByCode(tokens[0]));
        view.onLanguagesSwapped();
    }


    public void onOpenDictionaryClick() {
        if (view != null)
            view.openDictionary(translatePreferences.getInputText(), translatePreferences.getTranslateDirection());
    }

    public void onStarredClick() {
        HistoryObject obj = dataManager.getHistoryWord(translatePreferences.getInputText(),
                translatePreferences.getTranslateDirection());
        if (obj == null)
            return;

        final boolean isFavorites = obj.isFavorites();

        updateStarredWord(!isFavorites);
        view.setIsStarredView(!isFavorites);
    }


    public String getSourceLanguage() {
        final String sym = translatePreferences.getTranslateDirection().split("-")[0];
        return dataManager.getLanguageByCode(sym);
    }

    public String getDestinationLanguage() {
        final String sym = translatePreferences.getTranslateDirection().split("-")[1];
        return dataManager.getLanguageByCode(sym);
    }

    public ArrayList<String> getLanguagesList() {
        return dataManager.getLanguagesList();
    }

    @Override
    public void onTranslateResult(HistoryObject historyObject) {
        if (view == null)
            return;
        view.setTranslatedText(historyObject.getWord(), historyObject.getTranslate());
        view.isStarVisible(true);
        view.setIsStarredView(historyObject.isFavorites());
    }

    @Override
    public void onDictionaryResult(Word w) {
        final boolean detailsAreAvailable = w.getDictionaries().size() > 0;
        if (view != null)
            view.detailsAreAvailable(detailsAreAvailable);
    }

    private void addWordToHistory() {
        final String text = translatePreferences.getInputText();
        final String direction = translatePreferences.getTranslateDirection();

        dataManager.updateHistoryWord(text, direction, true);
    }

    private void updateStarredWord(boolean isStarredNow) {
        final String text = translatePreferences.getInputText();
        final String direction = translatePreferences.getTranslateDirection();

        dataManager.updateStarredWord(text, direction, isStarredNow);
    }

    private void updateLanguagePairInView(String direction) {
        final String[] tokens = direction.split("-");

        final String sourceLanguage = dataManager.getLanguageByCode(tokens[0]);
        final String destinationLanguage = dataManager.getLanguageByCode(tokens[1]);

        view.setLanguagePair(sourceLanguage, destinationLanguage);
    }
}
