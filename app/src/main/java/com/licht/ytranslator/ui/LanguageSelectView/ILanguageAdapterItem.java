package com.licht.ytranslator.ui.LanguageSelectView;

/**
 * Интерфейс элементов, которые могут быть в адаптере списка доступных языков
 */
interface ILanguageAdapterItem {
    int LANGUAGE_ITEM_TYPE = 1;
    int TITLE_ITEM_TYPE = 2;

    /**
     * Возвращает список введённого элемента
     * Конструкция instanceof работает медленно, поэтому определили функцию, которая позволяет
     * узнать тип
     */
    int getItemType();
}

class LanguageItem implements ILanguageAdapterItem {
    private String content;

    LanguageItem(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int getItemType() {
        return ILanguageAdapterItem.LANGUAGE_ITEM_TYPE;
    }
}

class TitleItem implements ILanguageAdapterItem {
    private String content;

    TitleItem(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    @Override
    public int getItemType() {
        return ILanguageAdapterItem.TITLE_ITEM_TYPE;
    }

    public void setContent(String content) {
        this.content = content;
    }
}