package com.example.symptalk;

import java.util.List;

public class CohereResponse {
    public String id;
    public String response_id;
    public String generation_id;
    public String prompt;
    public List<ChatMessage> messages;
    public String text; // main generated output
    public Meta meta;

    public static class ChatMessage {
        public String role; // "USER", "CHATBOT", etc.
        public String message;
    }

    public static class Meta {
        public ApiVersion api_version;
        public String model;
        public String finish_reason;

        public static class ApiVersion {
            public String version;
        }
    }
}

