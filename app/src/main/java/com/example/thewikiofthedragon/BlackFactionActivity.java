package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class BlackFactionActivity extends AppCompatActivity {

    private Button btnCharacters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_faction);

        // Configurar el botón "Personajes"
        btnCharacters = findViewById(R.id.btnCharacters);
        btnCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Al hacer clic en el botón "Personajes", ir a la nueva actividad de lista de personajes
                Intent intent = new Intent(BlackFactionActivity.this, CharacterListActivity.class);
                startActivity(intent);
            }
        });
    }
}
