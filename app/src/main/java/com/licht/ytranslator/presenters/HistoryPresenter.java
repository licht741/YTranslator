package com.licht.ytranslator.presenters;

import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.ui.HistoryView.IHistoryView;

import java.util.ArrayList;
import java.util.List;

public class HistoryPresenter implements IPresenter<IHistoryView> {

    private DataManager dataManager;

    private IHistoryView view;

    public HistoryPresenter(DataManager dataManager) {
        super();
        this.dataManager = dataManager;
    }

    @Override
    public void bindView(IHistoryView view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        view = null;
    }

    /**
     * Вызывается для получения слов из истории
     *
     * @param starredOnly Вернуть список из избранных слов
     */
    public void requestData(boolean starredOnly) {
        List<HistoryObject> allItems = dataManager.getHistoryWords();

        if (starredOnly)
            view.setData(extractStarredWords(allItems));
        else
            view.setData(allItems);
    }

    /**
     * Добавление или удаление слова из списка избранного
     *
     * @param text            Переводимый текст
     * @param direction       Направление перевода
     * @param newStarredState True, если слово добавляется в избранное, иначе False
     */
    public void setWordStarredState(String text, String direction, boolean newStarredState) {
        dataManager.setWordStarred(text, direction, newStarredState);
    }

    /**
     * Извлекает из списка слова, попавшие в избранное
     */
    private List<HistoryObject> extractStarredWords(List<HistoryObject> items) {
        final List<HistoryObject> starred = new ArrayList<>();
        for (HistoryObject item : items)
            if (item.isFavorites())
                starred.add(item);

        return starred;
    }
}
