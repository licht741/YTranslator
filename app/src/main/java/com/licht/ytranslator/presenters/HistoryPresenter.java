package com.licht.ytranslator.presenters;

import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.presenters.base.IPresenter;
import com.licht.ytranslator.ui.HistoryView.IHistoryView;

import java.util.ArrayList;

public class HistoryPresenter implements IPresenter<IHistoryView> {

    private final DataManager dataManager;

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
        if (starredOnly)
            view.setData(dataManager.getStarredWords());
        else
            view.setData(dataManager.getHistoryWords());
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
     * Очистка истории от переводов
     *
     * @param starredOnly True, если требуется удалить только избранные переводы. False, если удаляются все
     */
    public void clearHistory(boolean starredOnly) {
        dataManager.clearHistory(starredOnly);

        if (view != null)
            view.setData(new ArrayList<>());
    }

}
