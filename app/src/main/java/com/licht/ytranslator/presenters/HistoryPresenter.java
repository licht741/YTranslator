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
//        allItems = dataManager.getHistoryWords();
//        starredItems = extractStarredWords(allItems);
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


//    private List<HistoryObject> allItems = null;
//    private List<HistoryObject> starredItems = null;

    //    public void requestData(boolean starredOnly) {
////        if (allItems == null) {
//            allItems = dataManager.getHistoryWords();
//            starredItems = extractStarredWords(allItems);
////        }
//
//        if (starredOnly)
//            view.setData(starredItems);
//        else
//            view.setData(allItems);
//    }
//
//    public void selectItem(String word, String direction) {
////        allItems.get(0).setFavorites(true);
//    }
//
////    public void setWordStar(String word, String direction, boolean newStarredState) {
////        dataManager.setWordStarred(word, direction, newStarredState);
////
////        // если мы добавляем в избранное
////        if (newStarredState) {
////            for (HistoryObject item : allItems)
////                if (item.getWord().equals(word) && item.getDirection().equals(direction)) {
////                    item.setFavorites(newStarredState);
////                    starredItems.add(item);
////                    break;
////                }
////        }
////        // удаляем из избранного
////        else {
////            for (HistoryObject item : allItems) {
////                if (item.getWord().equals(word) &&
////                        item.getDirection().equals(direction)) {
////                    item.setFavorites(newStarredState);
////                    break;
////                }
////            }
////            int index = -1;
////            for (int i = 0; i < starredItems.size(); ++i)
////                if (starredItems.get(i).getWord().equals(word) && starredItems.get(i).getDirection().equals(direction)) {
////                    index = i;
////                    break;
////                }
////            starredItems.remove(index);
////        }
////    }
//
    private List<HistoryObject> extractStarredWords(List<HistoryObject> items) {
        final List<HistoryObject> starred = new ArrayList<>();
        for (HistoryObject item : items)
            if (item.isFavorites())
                starred.add(item);

        return starred;
    }
}
