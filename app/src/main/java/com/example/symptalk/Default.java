package com.example.symptalk;

import android.util.Log;

import com.cohere.api.Cohere;
import com.cohere.api.resources.v2.requests.V2ChatRequest;
import com.cohere.api.types.*;
import java.util.List;
public class Default {

    public void getAnswer(){

        Cohere cohere = Cohere.builder().token("").clientName("snippet").build();

        ChatResponse response =
                cohere.v2()
                        .chat(
                                V2ChatRequest.builder()
                                        .model("command-a-03-2025")
                                        .messages(
                                                List.of(
                                                        ChatMessageV2.user(
                                                                UserMessage.builder()
                                                                        .content(
                                                                                UserMessageContent
                                                                                        .of("Hello world!"))
                                                                        .build())))
                                        .build());


        System.out.println(response);
        Log.d("response", String.valueOf(response.getMessage()));
        Log.d("also response",response.getMessage().toString());
    }


}
