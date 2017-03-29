package com.licht.ytranslator.data.endpoint;


import com.google.gson.JsonObject;
import com.licht.ytranslator.data.model.Result;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexTranslateAPI {
    @FormUrlEncoded
    @POST("/api/v1.5/tr.json/translateText")
    Call<Result> translate(@FieldMap Map<String, String> map);

    @GET("/api/v1.5/tr.json/getLangs")
    Call<JsonObject> getData(@Query("key") String key, @Query("ui") String ui);
}
