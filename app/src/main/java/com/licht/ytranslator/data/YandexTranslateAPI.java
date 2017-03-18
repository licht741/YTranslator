package com.licht.ytranslator.data;


import com.google.gson.JsonObject;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface YandexTranslateAPI {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translate")
    Call<Result> translate(@FieldMap Map<String, String> map);

    @GET("/api/v1.5/tr.json/getLangs")
    Call<JsonObject> getData(@Query("key") String key, @Query("ui") String ui);
}
