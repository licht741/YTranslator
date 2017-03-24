package com.licht.ytranslator.presenters;

import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.data.model.HistoryItem;
import com.licht.ytranslator.ui.HistoryView.IHistoryView;

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
        if (this.view != null) {
            int x = 0;
        }
        this.view = view;
    }

    @Override
    public void unbindView() {
        view = null;
    }

    public void requestData(boolean starredOnly) {
        final List<HistoryItem> items = starredOnly ?
                dataManager.getStarredWords() :
                dataManager.getHistoryWords();
        view.setData(items);
    }
}
