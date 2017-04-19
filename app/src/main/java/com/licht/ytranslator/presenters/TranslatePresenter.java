package com.licht.ytranslator.presenters;

import com.licht.ytranslator.R;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.DictionaryObject;
import com.licht.ytranslator.data.sources.TranslatePreferences;
import com.licht.ytranslator.loaders.TranslateLoader;
import com.licht.ytranslator.presenters.base.IPresenter;
import com.licht.ytranslator.ui.TranslateView.ITranslateView;
import com.licht.ytranslator.utils.Utils;

import java.util.ArrayList;

public class TranslatePresenter implements IPresenter<ITranslateView>, OnTranslateResultListener {
    private final DataManager dataManager;
    private final TranslatePreferences translatePreferences;
    private final TranslateLoader translateLoader;

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
        // Получаем использованный в последний раз переводимый текст
        // Если его нет (возможно только в том случае, если приложение открывается в первый раз),
        // то устанавливаем и возвращаем пустой текст
        String input = translatePreferences.getInputText();
        if (input == null) {
            input = "";
            translatePreferences.setInputText(input);
        }

        // Получаем использованное в последний раз направление перевода
        // Если его нет (возможно только в том случае, если приложение открывается в первый раз),
        // то устанавливаем и возвращаем стандартное направление (с английского на русский)
        String translateDirection = translatePreferences.getTranslateDirection();
        if (translateDirection == null || "".equals(translateDirection)) {
            translateDirection = "en-ru";
            translatePreferences.setDirectionText(translateDirection);

            // Сразу же добавляем эти языки в список недавно использованных
            translatePreferences.updateRecentlyUsedLanguage(dataManager.getLanguageByCode("en"));
            translatePreferences.updateRecentlyUsedLanguage(dataManager.getLanguageByCode("ru"));
        }

        initializeData(input, translateDirection);

    }

    @Override
    public void onTranslateFailure() {
        if (view != null)
            view.onTranslateFailure();
    }

    /**
     * Инициализирует окно перевода переданными значениями
     * Источник полученных значений может быть разным (последний перевод, выбор перевода из истории)
     *
     * @param inputText Переводимый текст
     * @param translateDirection Направление перевода
     */
    public void initializeData(String inputText, String translateDirection) {
        final String[] languages = translateDirection.split("-");

        translatePreferences.setDirectionText(translateDirection);

        if (view == null)
            return;

        view.setInputText(inputText);
        view.setLanguagePair(dataManager.getLanguageByCode(languages[0]),
                             dataManager.getLanguageByCode(languages[1]));
    }

    /**
     * Вызывается при изменении содержимого поля ввода текста на экране перевода
     *
     * @param content Введённый текст
     */
    public void onTextInput(String content) {
        if (content == null)
            return;

        if (view != null)
            view.detailsAreAvailable(false);

        // Сохраняем текст как последний введённый
        translatePreferences.setInputText(content);

        if ("".equals(content)) {
            view.setTranslatedText(content, "");
            return;
        }

        // Полученный для перевода текст не пустой, и можно запрашивать для него перевод
        translateText();
    }

    private void translateText() {
        final String text = translatePreferences.getInputText();
        final String direction = translatePreferences.getTranslateDirection();

        // Делаем асинхронные запросы к Яндекс Переводчику и Яндекс Словарю через лоадер

        final String key = YTransApp.get().getString(R.string.key_translate);
        translateLoader.translate(key, text, direction);

        final String keyDict = YTransApp.get().getString(R.string.key_dictionary);
        translateLoader.getDictionaryMeanings(keyDict, text, direction);
    }

    public void onKeyboardHide() {
        // Если пользователь закрыл клавиатуру, то он просматривает перевод слова
        // В этой ситуации мы сохраняем слово в историю

        // Помечаем, что слово, которое мы ранее закэшировали, теперь входит в историю
        addExistingTranslatingToHistory();

    }

    /**
     * Вызывается при изменении пользователем исходного языка
     *
     * @param newSourceLanguage Название нового исходного языка, написанное в используемой локализации
     */
    public void onUpdateSourceLanguage(String newSourceLanguage) {

        //Обновляем список недавно использованных языков
        translatePreferences.updateRecentlyUsedLanguage(newSourceLanguage);

        // Переводим название языка в его кодовое обозначение
        final String langSymbol = dataManager.getLanguageSymbolByName(newSourceLanguage);

        final String currentDirection = translatePreferences.getTranslateDirection();
        final String[] tokens = currentDirection.split("-");
        final String newDirection = String.format("%s-%s", langSymbol, tokens[1]);
        translatePreferences.setDirectionText(newDirection);

        updateLanguagePairInView(newDirection);
        if (view != null)
            view.onLanguageChanges();
    }

    public ArrayList<String> getRecentlyUsedLanguages() {
        return translatePreferences.getRecentlyUsedLanguages();
    }

    /**
     * Вызывается при изменении пользователем языка, на который осуществляется перевод
     *
     * @param newDestinationLanguage Название языка, написанное в используемой локализации
     */
    public void onUpdateDestinationLanguage(String newDestinationLanguage) {
        //Обновляем список недавно использованных языков
        translatePreferences.updateRecentlyUsedLanguage(newDestinationLanguage);

        // Переводим название языка в его кодовое обозначение
        final String langSymbol = dataManager.getLanguageSymbolByName(newDestinationLanguage);

        final String currentDirection = translatePreferences.getTranslateDirection();
        final String[] tokens = currentDirection.split("-");
        final String newDirection = String.format("%s-%s", tokens[0], langSymbol);

        translatePreferences.setDirectionText(newDirection);

        updateLanguagePairInView(newDirection);

        if (view != null)
            view.onLanguageChanges();
    }

    public void onSwapLanguages() {
        final String currentDirection = translatePreferences.getTranslateDirection();
        final String[] tokens = currentDirection.split("-");
        final String newDirection = String.format("%s-%s", tokens[1], tokens[0]);
        translatePreferences.setDirectionText(newDirection);

        if (view == null)
            return;

        final String newSourceLanguage = dataManager.getLanguageByCode(tokens[1]);
        final String newDestLanguage = dataManager.getLanguageByCode(tokens[0]);

        translatePreferences.updateRecentlyUsedLanguage(newSourceLanguage);
        translatePreferences.updateRecentlyUsedLanguage(newDestLanguage);

        view.setLanguagePair(newSourceLanguage, newDestLanguage);
        view.onLanguagesSwapped();
    }


    public void onOpenDictionaryClick() {
        if (view != null)
            view.openDictionary(translatePreferences.getInputText(), translatePreferences.getTranslateDirection());
    }

    public void onStarredClick() {
//        HistoryObject obj = dataManager.getHistoryWord(translatePreferences.getInputText(),
//                                                       translatePreferences.getTranslateDirection());
        // Если из-за какой-то ошибки мы можем добавить в избранное слово, которое не было закэшировано,
        // то не делаем ничего
//        if (obj == null)
//            return;

        final boolean isStarredNow =  updateStarredWord();

        if (view != null)
            view.isStarredText(isStarredNow);
    }

    public void onStartAudio() {
        // Передаём исходный язык (язык, с которого осуществляется перевод) как параметр для
        // голосового ввода
        final String inputLanguageSymbol = translatePreferences.getTranslateDirection().split("-")[0];
        if (view != null)
            view.startAudioWithInputLanguage(inputLanguageSymbol);
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
        // Получили результат от асинхронного запроса к Яндекс Переводчику, обрабатываем его
        if (view == null)
            return;
        view.setTranslatedText(historyObject.getWord(), historyObject.getTranslate());
//        view.isTranslateActionsAvailable(true);
        view.isStarredText(historyObject.isFavorites());
    }

    @Override
    public void onDictionaryResult(DictionaryObject w) {
        // Получили результат от асинхронного запроса к Яндекс Словарю, обрабатываем его
        final boolean detailsAreAvailable = w.getDictionaries().size() > 0;
        if (view != null)
            view.detailsAreAvailable(detailsAreAvailable);
    }

    public void onShareText() {
        final String text = translatePreferences.getInputText();
        final String direction = translatePreferences.getTranslateDirection();

        final HistoryObject historyObject = dataManager.getHistoryWord(text, direction);
        if (historyObject == null)
            return;

        final String translate = historyObject.getTranslate();
        final String res = Utils.formattedTranslatingToShare(text, translate);

        if (view != null)
            view.shareText(res);
    }

    /**
     * Добавляет перевод (ранее закэшированный) в историю
     */
    private void addExistingTranslatingToHistory() {
        final String text = translatePreferences.getInputText();
        final String direction = translatePreferences.getTranslateDirection();
        dataManager.addWordToHistory(text, direction);
    }

    private boolean updateStarredWord() {
        final String text = translatePreferences.getInputText();
        final String direction = translatePreferences.getTranslateDirection();

        return dataManager.reverseWordStarred(text, direction);
    }

    private void updateLanguagePairInView(String direction) {
        final String[] tokens = direction.split("-");

        final String sourceLanguage = dataManager.getLanguageByCode(tokens[0]);
        final String destinationLanguage = dataManager.getLanguageByCode(tokens[1]);

        if (view != null)
            view.setLanguagePair(sourceLanguage, destinationLanguage);
    }
}
