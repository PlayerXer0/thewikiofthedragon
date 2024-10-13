package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CharacterListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter characterAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);

        // Obtener todos los personajes desde la base de datos
        List<Character> characterList = dbHelper.getAllCharacters();
        Log.d("DatabaseDebug", "Número de personajes recuperados de la base de datos: " + characterList.size());

        // Comprobar si la lista de personajes está vacía
        if (characterList.isEmpty()) {
            Log.d("CharacterListActivity", "No se encontraron personajes en la base de datos.");
            return; // Si no hay personajes, detener la ejecución
        } else {
            Log.d("CharacterListActivity", "Número de personajes encontrados: " + characterList.size());
        }

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCharacters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Configurar el adaptador con los personajes desde la base de datos
        characterAdapter = new CharacterAdapter(characterList, character -> {
            // Acción al hacer clic en un personaje
            Intent intent = new Intent(CharacterListActivity.this, CharacterDetailActivity.class);
            intent.putExtra("characterName", character.getName());
            intent.putExtra("characterImage", character.getImageResId());
            intent.putExtra("characterBiography", character.getBiography());
            startActivity(intent);
        });
        recyclerView.setAdapter(characterAdapter);
    }
}
