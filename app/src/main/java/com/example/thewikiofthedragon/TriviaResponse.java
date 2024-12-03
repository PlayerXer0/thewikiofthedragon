package com.example.thewikiofthedragon;

import java.util.List;

public class TriviaResponse {
    private int response_code;
    private List<TriviaQuestionAPI> results;

    public int getResponseCode() {
        return response_code;
    }

    public List<TriviaQuestionAPI> getResults() {
        return results;
    }
}
