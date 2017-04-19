package com.licht.ytranslator.ui.LanguageSelectView;

interface ILanguageAdapterItem {
    int LANGUAGE_ITEM_TYPE = 1;
    int TITLE_ITEM_TYPE = 2;

    int getItemType();
}

class LanguageItem implements ILanguageAdapterItem {
    private String content;

    public LanguageItem(String content) {
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

    public TitleItem(String content) {
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