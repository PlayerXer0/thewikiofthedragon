package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class GreenFactionActivity extends AppCompatActivity {

    private Button btnGreenCharacters, btnGreenDragons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_faction);

        // Vincular los botones
        btnGreenCharacters = findViewById(R.id.btnGreenCharacters);
        btnGreenDragons = findViewById(R.id.btnGreenDragons);

        // Configurar la acción del botón "Personajes"
        btnGreenCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreenFactionActivity.this, GreenFactionCharactersActivity.class);
                startActivity(intent);
            }
        });

        // Configurar la acción del botón "Dragones"
        btnGreenDragons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreenFactionActivity.this, GreenDragonActivity.class);
                startActivity(intent);
            }
        });
    }
}
