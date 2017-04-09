package com.licht.ytranslator.presenters;

interface IPresenter<T> {
    void bindView(T t);
    void unbindView();
}
