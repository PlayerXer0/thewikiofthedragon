package com.example.thewikiofthedragon;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface OpenTriviaAPI {
    @GET("api.php")
    Call<TriviaResponse> getQuestions(
            @Query("amount") int amount,         // Número de preguntas
            @Query("category") int category,    // Categoría (opcional)
            @Query("difficulty") String difficulty, // Dificultad ("easy", "medium", "hard")
            @Query("type") String type,         // Tipo ("multiple" o "boolean")
            @Query("encode") String encode      // Codificación (para caracteres especiales)
    );
}
