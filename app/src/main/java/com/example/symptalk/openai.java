package com.example.symptalk;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import com.openai.models.ChatModel;

OpenAIClient client = OpenAIOkHttpClient.fromEnv();
public class openai {

    ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
            .addUserMessage("Say this is a test")
            .model(ChatModel.O3_MINI)
            .build();
    ChatCompletion chatCompletion = client.chat().completions().create(params);

}
