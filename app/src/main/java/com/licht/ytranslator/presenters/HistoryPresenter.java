package com.licht.ytranslator.presenters;

import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.ui.HistoryView.IHistoryView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class HistoryPresenter implements IPresenter<IHistoryView> {
    @Inject
    DataManager dataManager;

    private IHistoryView view;

    public HistoryPresenter() {
        super();
        YTransApp.getAppComponent().inject(this);
    }

    @Override
    public void bindView(IHistoryView view) {
        this.view = view;
    }

    @Override
    public void unbindView() {
        view = null;
    }

    public void requestData(boolean starredOnly) {
        List<HistoryObject> allItems = dataManager.getHistoryWords();

        if (starredOnly)
            view.setData(extractStarredWords(allItems));
        else
            view.setData(allItems);
    }

    public void setWordStarredState(String word, String direction, boolean newStarredState) {
        dataManager.setWordStarred(word, direction, newStarredState);
    }

    private List<HistoryObject> extractStarredWords(List<HistoryObject> items) {
        final List<HistoryObject> starred = new ArrayList<>();
        for (HistoryObject item : items)
            if (item.isFavorites())
                starred.add(item);

        return starred;
    }
}
