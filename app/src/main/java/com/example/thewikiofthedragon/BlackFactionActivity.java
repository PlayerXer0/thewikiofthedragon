package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BlackFactionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_faction);

        Button btnCharacters = findViewById(R.id.btnCharacters);
        Button btnDragon = findViewById(R.id.btnDragon);

        btnCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BlackFactionActivity.this, BlackFactionCharactersActivity.class);
                startActivity(intent);
            }
        });

        btnDragon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Agregar la lógica para abrir la actividad de Lugares si existe
            }
        });
    }
}
