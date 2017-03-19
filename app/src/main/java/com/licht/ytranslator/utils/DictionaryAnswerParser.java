package com.licht.ytranslator.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.licht.ytranslator.data.model.Dictionary;
import com.licht.ytranslator.data.model.Translate;

import java.util.ArrayList;
import java.util.List;

public class DictionaryAnswerParser {
    public static List<Dictionary> parse(JsonObject obj) {
        JsonArray def = obj.getAsJsonArray("def");

        final List<Dictionary> dictionaries = new ArrayList<>();
        for (int i = 0; i < def.size(); ++i) {
            JsonObject el = def.get(i).getAsJsonObject();

            final String text = el.get("text").getAsString();
            final String transcription = el.get("ts").getAsString(); //todo null

            //extract translates
            JsonArray tr = el.getAsJsonArray("tr");
            List<Translate> translates = extractTranslating(tr);

            dictionaries.add(new Dictionary(text, transcription, translates));
        }

        return dictionaries;
    }

    private static List<Translate> extractTranslating(JsonArray tr) {
        final List<Translate> translates = new ArrayList<>();
        for (int j = 0; j < tr.size(); ++j) {
            JsonObject el1 = tr.get(j).getAsJsonObject();

            List<String> synList = new ArrayList<>();

            JsonArray syn = el1.getAsJsonArray("syn");
            if (syn != null)
                for (JsonElement jsonElement : syn) {
                    String s = jsonElement.getAsJsonObject().get("text").getAsString();
                    synList.add(s);
                }

            List<String> meanings = new ArrayList<>();

            JsonArray arr = el1.getAsJsonArray("mean");
            if (arr != null)
                for (int k = 0; k < arr.size(); ++k) {
                    String s = arr.get(k).getAsJsonObject().get("text").getAsString();
                    meanings.add(s);
                }

            translates.add(new Translate(synList, meanings));
        }
        return translates;
    }
}