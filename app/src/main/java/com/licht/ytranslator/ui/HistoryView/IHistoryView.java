package com.licht.ytranslator.ui.HistoryView;

import com.licht.ytranslator.data.model.HistoryObject;

import java.util.List;

public interface IHistoryView {
    void setData(List<HistoryObject> items);
    void updateData();
    void onItemSelected(String word, String direction);
    void onStarredChanged(String word, String direction, boolean newStarredState);
}
