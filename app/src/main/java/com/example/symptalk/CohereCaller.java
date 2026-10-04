package com.example.symptalk;// In your Activity or ViewModel
import android.util.Log;

import com.example.symptalk.CohereService;
import com.example.symptalk.RetrofitClient;

import java.util.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CohereCaller {

    public void callCohereAPI() {
        CohereService service = RetrofitClient.getClient().create(CohereService.class);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "command-r-plus");
        List<Map<String, String>> messages = new ArrayList<>();
        messages.add(Map.of("role", "user", "content", "Hello world!"));
        requestBody.put("messages", messages);

        Call<Map<String, Object>> call = service.chat(requestBody);

        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful()) {
                    Log.d("Cohere", "Response: " + response.body());
                } else {
                    Log.e("Cohere", "API Error: " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("Cohere", "Failure: " + t.getMessage());
            }
        });
    }
}
