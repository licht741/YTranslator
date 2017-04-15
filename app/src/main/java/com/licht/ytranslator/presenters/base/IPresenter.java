package com.licht.ytranslator.presenters.base;

public interface IPresenter<T> {
    void bindView(T t);
    void unbindView();
}
