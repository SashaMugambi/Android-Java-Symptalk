package com.example.symptalk;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class main extends AppCompatActivity {

//    Default aDefault = new Default();
    CohereCaller cCaller = new CohereCaller();

    private static final String TAG = "SympTalk";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        cCaller.callCohereAPI();

//        System.out.println(aDefault.getAnswer());
//        aDefault.getAnswer();
        // Load JSON data (optional use)
        String jsonData = loadJSONFromAsset("diseases_symptoms.json");
        if (jsonData != null) {
            Log.d(TAG, "Loaded JSON: " + jsonData);
        } else {
            Log.e(TAG, "Failed to load JSON");
        }

        EditText symptomInputEditText = findViewById(R.id.symptomInputEditText);
        Button submitButton = findViewById(R.id.submitButton);

        submitButton.setOnClickListener(v -> {
            String userSymptomInput = symptomInputEditText.getText().toString().trim();

            if (userSymptomInput.isEmpty()) {
                Toast.makeText(main.this, "Please enter a symptom.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Build prompt
            String prompt = "You are a helpful medical assistant. Based on the symptom: \"" + userSymptomInput + "\", generate follow-up questions to gather more medical details.";

            // Prepare request body for Cohere
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", "command-r"); // or "command-r-plus"
            requestBody.put("prompt", prompt);
            requestBody.put("max_tokens", 100);
            requestBody.put("temperature", 0.7);

            // Create service
            CohereService cohereService = createCohereService();

            // Call Cohere API
//            Call<CohereGenerateResponse> call = cohereService.generateText(requestBody);
//            Call<CohereGenerateResponse> call = cohereService.generateText(requestBody);

//            call.enqueue(new Callback<CohereGenerateResponse>() {
//                @Override
//                public void onResponse(Call<CohereGenerateResponse> call, Response<CohereGenerateResponse> response) {
//                    if (response.isSuccessful() && response.body() != null && response.body().generations != null && !response.body().generations.isEmpty()) {
//                        String reply = response.body().generations.get(0).text;
//                        runOnUiThread(() -> Toast.makeText(main.this, reply, Toast.LENGTH_LONG).show());
//                    } else {
//                        Log.e(TAG, "Cohere response error: " + response.code());
//                        runOnUiThread(() -> Toast.makeText(main.this, "Error: " + response.message(), Toast.LENGTH_LONG).show());
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<CohereGenerateResponse> call, Throwable t) {
//                    Log.e(TAG, "Cohere request failed", t);
//                    Log.e("NET ERRRRRRR",t.getMessage());
//                    runOnUiThread(() -> Toast.makeText(main.this, "Network Error: " + t.getMessage(), Toast.LENGTH_LONG).show());
//                }
//            });
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private String loadJSONFromAsset(String fileName) {
        try (InputStream is = getAssets().open(fileName)) {
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            return new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private CohereService createCohereService() {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request original = chain.request();
                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer YOUR_COHERE_API_KEY_HERE") // 🔑 Replace with your actual API key
                            .method(original.method(), original.body())
                            .build();
                    return chain.proceed(request);
                }).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.cohere.ai/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return retrofit.create(CohereService.class);
    }
}

