package com.licht.ytranslator.data.endpoint;

import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YandexDictionaryAPI {
    @FormUrlEncoded
    @POST("/api/v1/dicservice.json/lookup")
    Call<JsonObject> getMeaning(@FieldMap Map<String, String> map);
}
