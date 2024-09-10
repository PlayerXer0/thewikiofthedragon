package com.example.thewikiofthedragon;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BlackFactionCharactersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter characterAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_faction_characters);

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);

        // Verifica si ya hay personajes en la base de datos
        if (dbHelper.getAllCharacters().isEmpty()) {
            // Solo inserta los personajes si la tabla está vacía, con sus biografías
            dbHelper.addCharacter("Rhaenyra Targaryen", R.drawable.rhae, "Rhaenyra es la primogénita de Viserys I...");
            dbHelper.addCharacter("Daemon Targaryen", R.drawable.daemon_targaryen, "Daemon Targaryen es el hermano menor de Viserys I...");
            dbHelper.addCharacter("Jacaerys Velaryon", R.drawable.jaca, "Jacaerys es el hijo mayor de Rhaenyra Targaryen...");
            dbHelper.addCharacter("Lucerys Velaryon", R.drawable.luce, "Lucerys es el segundo hijo de Rhaenyra...");
            dbHelper.addCharacter("Joffrey Velaryon", R.drawable.joff, "Joffrey es el hijo menor de Rhaenyra...");
            dbHelper.addCharacter("Baela Targaryen", R.drawable.baela, "Baela es la hija de Daemon Targaryen...");
            dbHelper.addCharacter("Rhaena Targaryen", R.drawable.rhaena, "Rhaena es la hermana gemela de Baela...");
            dbHelper.addCharacter("Rhaenys Targaryen", R.drawable.rhaenys, "Rhaenys es conocida como la Reina que nunca fue...");
        }

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.blackFactionRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Obtener todos los personajes del Bando Negro desde la base de datos
        List<Character> characterList = dbHelper.getAllCharacters();

        // Configurar el adaptador del RecyclerView
        characterAdapter = new CharacterAdapter(characterList, character -> {
            // Acciones al hacer clic en un personaje (muestra detalle, por ejemplo)
        });
        recyclerView.setAdapter(characterAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dbHelper.close();  // Cierra la base de datos cuando ya no se necesite
    }
}
