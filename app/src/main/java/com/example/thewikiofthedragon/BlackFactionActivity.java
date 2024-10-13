package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BlackFactionActivity extends AppCompatActivity {

    private Button btnCharacters, btnDragons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_faction);

        // Vincular los botones
        btnCharacters = findViewById(R.id.btnCharacters);
        btnDragons = findViewById(R.id.btnDragons);

        // Configurar la acción del botón "Personajes"
        btnCharacters.setOnClickListener(v -> {
            Intent intent = new Intent(BlackFactionActivity.this, BlackFactionCharactersActivity.class);
            startActivity(intent);
        });

        // Configurar la acción del botón "Dragones"
        btnDragons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navegar a la actividad de los dragones
                Intent intent = new Intent(BlackFactionActivity.this, BlackDragonActivity.class);
                startActivity(intent);
            }
        });
    }
}
