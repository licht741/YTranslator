package com.licht.ytranslator.ui.LoadingScreen;

public interface ILoadingView {
    /**
     * Загрузка успешно завершена
     */
    void finishLoading();

    /**
     * Загрузка закончилась ошибкой
     */
    void onLoadingFailure();
}
