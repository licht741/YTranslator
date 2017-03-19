package com.licht.ytranslator.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.StringWrapper;
import com.licht.ytranslator.data.model.Translate;

import java.util.ArrayList;
import java.util.List;

import io.realm.RealmList;

public class DictionaryAnswerParser {
    public static RealmList<Dictionary> parse(JsonObject obj) {
        JsonArray def = obj.getAsJsonArray("def");

        final RealmList<Dictionary> dictionaries = new RealmList<>();
        for (int i = 0; i < def.size(); ++i) {
            JsonObject el = def.get(i).getAsJsonObject();

            final String text = el.get("text").getAsString();
            final String transcription = el.get("ts") == null? "" : el.get("ts").getAsString(); //todo null

            //extract translates
            JsonArray tr = el.getAsJsonArray("tr");
            RealmList<Translate> translates = extractTranslating(tr);

            dictionaries.add(new Dictionary(text, transcription, translates));
        }

        return dictionaries;
    }

    private static RealmList<Translate> extractTranslating(JsonArray tr) {
        final RealmList<Translate> translates = new RealmList<>();
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

            JsonArray arr = el1.getAsJsonArray("mean");
            if (arr != null)
                for (int k = 0; k < arr.size(); ++k) {
                    String s = arr.get(k).getAsJsonObject().get("text").getAsString();
                    meanings.add(new StringWrapper(s));
                }

            translates.add(new Translate(synList, meanings));
        }
        return translates;
    }
}