package com.licht.ytranslator.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.licht.ytranslator.data.model.WordObject;
import com.licht.ytranslator.data.model.ExampleObject;
import com.licht.ytranslator.data.model.StringWrapper;
import com.licht.ytranslator.data.model.WordMeaningObject;
import com.licht.ytranslator.data.sources.UtilsPreferences;

import io.realm.RealmList;

/**
 * Парсер JSON ответа от Яндекс словаря.
 *
 */
public class DictionaryAnswerParser {

    private static final UtilsPreferences utilsPreferences = new UtilsPreferences();

    public static RealmList<WordObject> parse(JsonObject obj) {
        if (obj == null)
            return  new RealmList<>();

        JsonArray def = obj.getAsJsonArray("def");

        final RealmList<WordObject> dictionaries = new RealmList<>();
        for (int i = 0; i < def.size(); ++i) {
            JsonObject el = def.get(i).getAsJsonObject();

            final String text = el.get("text").getAsString();
            final String transcription = el.get("ts") == null ? "" : el.get("ts").getAsString(); //todo null
            final String pos = el.get("pos").getAsString();
            //extract translates
            JsonArray tr = el.getAsJsonArray("tr");
            RealmList<WordMeaningObject> wordMeaningObjects = extractTranslating(tr);

            WordObject d = new WordObject(utilsPreferences.generateDictionaryNumber(), text, transcription, pos, wordMeaningObjects);
            dictionaries.add(d);
        }

        return dictionaries;
    }

    private static RealmList<WordMeaningObject> extractTranslating(JsonArray tr) {
        final RealmList<WordMeaningObject> wordMeaningObjects = new RealmList<>();
        for (int j = 0; j < tr.size(); ++j) {
            JsonObject el1 = tr.get(j).getAsJsonObject();

            RealmList<StringWrapper> synList = new RealmList<>();

            JsonArray syn = el1.getAsJsonArray("syn");
            if (syn != null)
                for (JsonElement jsonElement : syn) {
                    String s = jsonElement.getAsJsonObject().get("text").getAsString();
                    synList.add(new StringWrapper(s));
                }

            RealmList<StringWrapper> meanings = new RealmList<>();

            final String text = el1.get("text").getAsString();
            final String pos = el1.get("pos").getAsString();

            JsonArray ex = el1.getAsJsonArray("ex");

            final RealmList<ExampleObject> exampleObjects = new RealmList<>();

            if (ex != null) {
                for (int k = 0; k < ex.size(); ++k) {
                    final JsonObject obj = ex.get(k).getAsJsonObject();
                    final String t = obj.get("text").getAsString();
                    final JsonArray tr1 = obj.getAsJsonArray("tr");

                    final RealmList<StringWrapper> translates1 = new RealmList<>();
                    for (int k1 = 0; k1 < tr1.size(); ++k1)
                        translates1.add(new StringWrapper(tr1.get(k1).getAsJsonObject().get("text").getAsString()));
                    exampleObjects.add(new ExampleObject(new StringWrapper(t), translates1));

                }

            }

            JsonArray arr = el1.getAsJsonArray("mean");
            if (arr != null)
                for (int k = 0; k < arr.size(); ++k) {
                    String s = arr.get(k).getAsJsonObject().get("text").getAsString();
                    meanings.add(new StringWrapper(s));
                }


            wordMeaningObjects.add(new WordMeaningObject(synList, meanings, exampleObjects, text, pos));
        }
        return wordMeaningObjects;
    }
}