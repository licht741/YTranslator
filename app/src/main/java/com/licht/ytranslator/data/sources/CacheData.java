package com.licht.ytranslator.data.sources;

import android.support.annotation.Nullable;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.licht.ytranslator.YTransApp;
import com.licht.ytranslator.data.model.DictionaryObject;
import com.licht.ytranslator.data.model.ExampleObject;
import com.licht.ytranslator.data.model.HistoryObject;
import com.licht.ytranslator.data.model.Localization;
import com.licht.ytranslator.data.model.WordMeaningObject;
import com.licht.ytranslator.data.model.WordObject;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;

/**
 * Предоставляет обёртку над базой данных
 * Используется ORM Realm.
 * При выборе ORM, я выбирал между Realm и GreenDAO, но всё таки выбрал Realm, потому что не работал
 * с ней, и было интересно попробовать в проекте
 */
public class CacheData {
    public CacheData() {
        super();
        Realm.init(YTransApp.get());

        // Инициируем библиотеку, которая позволяет смотреть содержимое базы данных и SharedPreferences
        // с помощью инструментов разработчика в Google Chrome
        Stetho.initialize(
                Stetho.newInitializerBuilder(YTransApp.get())
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(YTransApp.get()))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(YTransApp.get()).build())
                        .build());
    }

    /**
     * Возвращает список языков в указанной локализации
     * @param localSymbol символ языка
     * @return Список языков в указанной локализации
     */
    public Localization[] getLanguageList(String localSymbol) {
        final Realm realm = Realm.getDefaultInstance();
        RealmResults<Localization> localizations = realm.where(Localization.class)
                .equalTo("locale", localSymbol)
                .findAll();
        return localizations.toArray(new Localization[localizations.size()]);
    }

    public void saveLocalization(List<Localization> localizations) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();

        if (realm.where(Localization.class)
                .equalTo("locale", localizations.get(0).getLanguageSymbol())
                .count() > 0)
            realm.delete(Localization.class);

        realm.copyToRealm(localizations);
        realm.commitTransaction();
    }


    public DictionaryObject getCachedWord(String word, String dir) {
        final Realm realm = Realm.getDefaultInstance();
        DictionaryObject w = realm.where(DictionaryObject.class)
                .equalTo("word", word)
                .equalTo("direction", dir)
                .findFirst();
        if (w != null)
            w = realm.copyFromRealm(w);
        return w;
    }

    public WordObject getCachedDictionary(long id) {
        final Realm realm = Realm.getDefaultInstance();
        WordObject wordObject = realm.where(WordObject.class)
                .equalTo("id", id)
                .findFirst();
        if (wordObject != null)
            wordObject = realm.copyFromRealm(wordObject);

        return wordObject;
    }

    public void cacheDictionary(DictionaryObject dictionaryObject) {
        final Realm realm = Realm.getDefaultInstance();
        realm.beginTransaction();
        realm.copyToRealm(dictionaryObject);
        realm.commitTransaction();
    }

    public void addWordToHistory(HistoryObject item) {
        final Realm realm = Realm.getDefaultInstance();

        RealmResults<HistoryObject> it =
                realm.where(HistoryObject.class)
                .equalTo("word", item.getWord())
                .equalTo("direction", item.getDirection())
                .findAll();

        realm.beginTransaction();
        if (it.size() > 0) {
            HistoryObject historyObject = it.first();
            historyObject.setFavorites(item.isFavorites());
        } else
            realm.copyToRealm(item);

        realm.commitTransaction();
    }

    @Nullable
    public HistoryObject getWordFromHistory(String word, String direction) {
        final Realm realm = Realm.getDefaultInstance();
        HistoryObject historyObject = realm.where(HistoryObject.class)
                .equalTo("word", word)
                .equalTo("direction", direction)
                .equalTo("inHistory", true)
                .findFirst();

        if (historyObject != null)
            historyObject = realm.copyFromRealm(historyObject);
        return historyObject;
    }

    @Nullable
    public HistoryObject getWordFromCache(String word, String direction) {
        final Realm realm = Realm.getDefaultInstance();
        HistoryObject historyObject = realm.where(HistoryObject.class)
                .equalTo("word", word)
                .equalTo("direction", direction)
                .findFirst();

        if (historyObject != null)
            historyObject = realm.copyFromRealm(historyObject);
        return historyObject;
    }

    public List<HistoryObject> getHistoryWords() {
        final Realm realm = Realm.getDefaultInstance();
        List<HistoryObject> res = realm.where(HistoryObject.class)
                .equalTo("inHistory", true).findAll()
                .sort("firstUsingDate");

        // Открепляем объекты от realm, для того, чтоб модифицировать их вне транзакций
        List<HistoryObject> historyObjects = new ArrayList<>();
        for (HistoryObject obj : res)
            historyObjects.add(realm.copyFromRealm(obj));

        return historyObjects;
    }


    public HistoryObject updateHistoryWord(String word, String direction, boolean isHistoryWord) {
        final Realm realm = Realm.getDefaultInstance();


        final HistoryObject w = realm.where(HistoryObject.class)
                .equalTo("word", word)
                .equalTo("direction", direction).findFirst();

        if (w == null)
            return null;

        realm.beginTransaction();
        w.setInHistory(isHistoryWord);
        realm.commitTransaction();

        return realm.copyFromRealm(w);
    }

    public void clearHistory() {
        // Очищаем историю переводов.
        // Для этого проходимся по всем объектам и указываем, что они больше не принадлежат истории
        // и списку избранных. Однако, перевод остаётся в кэше, и будет удалён,
        // когда истечет время жизни
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(r -> {
            final RealmQuery query = r.where(HistoryObject.class).equalTo("inHistory", true);
            RealmResults results = query.findAll();
            for (Object object : results) {
                HistoryObject historyObject = (HistoryObject) object;
                historyObject.setInHistory(false);
                historyObject.setFavorites(false);
            }
        });
    }

    public void clearStarredList() {
        // Очищаем список избранных
        // Переводы удаляются из избранных, но по-прежнему остаются в истории переводов
        final Realm realm = Realm.getDefaultInstance();
        realm.executeTransactionAsync(r -> {
            final RealmQuery query = r.where(HistoryObject.class).equalTo("isFavorites", true);
            RealmResults results = query.findAll();
            for (Object object : results) {
                HistoryObject historyObject = (HistoryObject) object;
                historyObject.setFavorites(false);
            }
        });
    }

    /**
     * Меняет избранность перевод: делает избранным, если оно не было, и наоборот
     *
     * @param word Переводимый текст
     * @param direction Направление перевода
     * @return True, если после вызова функции, перевод стал избранным. Иначе False.
     */
    public boolean reverseWordStarred(String word, String direction) {

        final Realm realm = Realm.getDefaultInstance();
        final HistoryObject w = realm.where(HistoryObject.class)
                .equalTo("word", word).equalTo("direction", direction)
                .findFirst();
        if (w == null)
            return false;

        boolean isStarred = w.isFavorites();
        realm.beginTransaction();
        w.setInHistory(true);
        w.setFavorites(!isStarred);
        realm.commitTransaction();

        return !isStarred;
    }

    /**
     * Осуществляет очистку базы данных от кэшированных переводов
     * Удаляются переводы, не попавшие в историю.
     */
    public void clearCache() {
        // Время жизни кэшированного перевода (в днях)
        final int nDays = 3;
        final Date dateNDaysAgo = getDateNDayAgo(nDays);

        final Realm realm = Realm.getDefaultInstance();

        // Realm не поддерживает каскадное удаление объектов,
        // поэтому приходится удалять объекты вручную

        // Выбираем переводы по заданному условию
        // (не попали в историю, с вышедшим временем жизни)
        final RealmQuery query =
                realm.where(HistoryObject.class).equalTo("inHistory", false)
                        .lessThanOrEqualTo("firstUsingDate", dateNDaysAgo);

        realm.beginTransaction();

        final RealmResults<HistoryObject> translatesToRemove = query.findAll();
        for (HistoryObject historyObject : translatesToRemove) {

            // Код нахождения элемента продублирован из функции getCachedWord(), т.к.
            // она возвращает неуправляемый (unmanaged) объект,
            // а для работы с Realm нам нужен управляемый (managed)
            DictionaryObject dictionaryObject = realm.where(DictionaryObject.class)
                    .equalTo("word", historyObject.getWord())
                    .equalTo("direction", historyObject.getDirection())
                    .findFirst();

            if (dictionaryObject == null)
                continue;

            for (WordObject wordObject : dictionaryObject.getDictionaries()) {
                for (WordMeaningObject m : wordObject.getWordMeaningObjects()) {

                    m.getSynonimes().deleteAllFromRealm();
                    m.getMeanings().deleteAllFromRealm();

                    for (ExampleObject ex : m.getExampleObjects())
                        ex.getTranslates().deleteAllFromRealm();
                    m.getExampleObjects().deleteAllFromRealm();
                }
                wordObject.getWordMeaningObjects().deleteAllFromRealm();
            }
            dictionaryObject.getDictionaries().deleteAllFromRealm();
        }
        query.findAll().deleteAllFromRealm();
        realm.commitTransaction();
    }

    /**
     * Возвращает дату, которая была N дней назад
     */
    private Date getDateNDayAgo(int n) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.DAY_OF_YEAR, -n);
        return cal.getTime();
    }
}
