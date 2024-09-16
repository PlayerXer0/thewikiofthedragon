package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class CharacterListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter characterAdapter;
    private List<Character> characterList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_list);

        // Inicializar la lista de personajes
        characterList = new ArrayList<>();
        characterList.add(new Character("Rhaenyra Targaryen", R.drawable.rhae, "Biografía de Rhaenyra"));
        characterList.add(new Character("Daemon Targaryen", R.drawable.daemon_targaryen, "Biografía de Daemon"));
        // Añadir más personajes según sea necesario

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recyclerViewCharacters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
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
