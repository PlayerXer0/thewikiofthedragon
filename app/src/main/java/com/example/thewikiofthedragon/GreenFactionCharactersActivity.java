package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GreenFactionCharactersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter characterAdapter;
    private List<Character> characterList;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_faction_characters);

        recyclerView = findViewById(R.id.recyclerViewGreenFactionCharacters);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        characterList = dbHelper.getAllGreenCharacters(); // Obtener personajes del bando verde

        characterAdapter = new CharacterAdapter(characterList, character -> {
            Intent intent = new Intent(GreenFactionCharactersActivity.this, CharacterDetailActivity.class);
            intent.putExtra("characterName", character.getName());
            intent.putExtra("characterImage", character.getImageResId());
            intent.putExtra("characterBiography", character.getBiography());
            startActivity(intent);
        });

        recyclerView.setAdapter(characterAdapter);
    }
}
