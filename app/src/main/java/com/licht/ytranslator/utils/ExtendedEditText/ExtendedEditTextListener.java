package com.licht.ytranslator.utils.ExtendedEditText;


public interface ExtendedEditTextListener {
    /**
     * Вызывается при закрытии клавиатуры
     *
     * @param text Текстовое содержимое
     */
    void onImeBack(String text);
}