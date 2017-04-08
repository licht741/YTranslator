package com.licht.ytranslator.utils.ExtendedEditText;
public interface ExtendedEditTextListener {
    /**
     * Вызывается при закрытии клавиатуры
     *
     * @param ctrl Элемент управление
     * @param text Текстовое содержимое
     */
    void onImeBack(ExtendedEditText ctrl, String text);
}