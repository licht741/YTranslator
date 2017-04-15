package com.licht.ytranslator.data.model;

import io.realm.RealmObject;

/**
 * Обёртка над строками.
 * Если мы хотим сохранять в БД поле-список строк, то у нас ничего не получится
 * Они должны быть вынесены в другую таблицу, а Realm не поддерживает строки как объект БД,
 * т.к. все объекты Realm должны наследоваться от RealmObject.
 * Поэтому приходится использовать обёртку для хранения строки.
 */
public class StringWrapper extends RealmObject {
    private String content;

    public StringWrapper() {
        super();
    }

    public StringWrapper(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
