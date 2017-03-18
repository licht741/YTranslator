package com.licht.ytranslator.presenters;

public interface IPresenter<T> {
    void bindView(T t);
    void unbindView();
}
