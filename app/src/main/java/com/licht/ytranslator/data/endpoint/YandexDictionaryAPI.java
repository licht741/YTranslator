package com.licht.ytranslator.data.endpoint;

import com.google.gson.JsonObject;
import com.licht.ytranslator.data.model.Result;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface YandexDictionaryAPI {
    @FormUrlEncoded
    @POST("/api/v1/dicservice.json/lookup")
    Call<JsonObject> getMeaning(@FieldMap Map<String, String> map);


}
