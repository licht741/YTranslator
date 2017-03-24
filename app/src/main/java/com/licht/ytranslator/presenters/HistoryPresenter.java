package com.licht.ytranslator.presenters;

import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.DataManager;
import com.licht.ytranslator.ui.HistoryView.IHistoryView;

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
}
