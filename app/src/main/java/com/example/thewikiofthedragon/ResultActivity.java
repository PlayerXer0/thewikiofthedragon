package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Obtener el puntaje pasado desde TriviaActivity
        int score = getIntent().getIntExtra("score", 0);
        int totalQuestions = getIntent().getIntExtra("totalQuestions", 0);

        // Mostrar el puntaje en el TextView
        TextView scoreTextView = findViewById(R.id.scoreTextView);
        scoreTextView.setText("Puntuación: " + score + " / " + totalQuestions);

        // Configurar el botón para regresar al menú principal
        Button returnToMenuButton = findViewById(R.id.returnToMenuButton);
        returnToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ResultActivity.this, MainActivity.class);
                startActivity(intent);
                finish(); // Finalizar esta actividad para limpiar el historial
            }
        });
    }
}
