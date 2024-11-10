package com.example.thewikiofthedragon;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BlackFactionCharactersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CharacterAdapter characterAdapter;
    private DatabaseHelper dbHelper;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_faction_characters);

        // Inicializar el VideoView
        videoView = findViewById(R.id.backgroundVideoViewBlackFaction);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videologin);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(null);  // Ocultar controles
        videoView.setOnCompletionListener(mp -> videoView.start());  // Repetir el video en bucle
        videoView.start();

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);

        // Obtener todos los personajes desde la base de datos
        List<Character> characterList = dbHelper.getAllBlackCharacters();

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.blackFactionRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Configurar el adaptador
        characterAdapter = new CharacterAdapter(characterList, character -> {
            // Acciones al hacer clic en un personaje
        });
        recyclerView.setAdapter(characterAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.start();  // Reanudar el video al volver a la actividad
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause();  // Pausar el video al salir de la actividad
        }
    }
}
