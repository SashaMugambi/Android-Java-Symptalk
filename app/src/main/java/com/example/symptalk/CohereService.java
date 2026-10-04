package com.example.symptalk;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface CohereService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: "
    })
    @POST("v1/chat")
//    Call<CohereGenerateResponse> generateText(@Body Map<String, Object> body);

    Call<Map<String, Object>> chat(Map<String, Object> requestBody);
}

