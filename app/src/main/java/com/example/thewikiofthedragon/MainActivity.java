package com.example.thewikiofthedragon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;
    private FirebaseFirestore db;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar el VideoView
        videoView = findViewById(R.id.backgroundVideoView);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videofondoperso);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(null); // Oculta los controles del video
        videoView.setOnCompletionListener(mp -> videoView.start()); // Reproduce el video en bucle
        videoView.start();

        // Inicializar Firestore y DatabaseHelper
        db = FirebaseFirestore.getInstance();
        databaseHelper = new DatabaseHelper(this);

        // Llamar al método para obtener los personajes de Firebase
        fetchCharactersFromFirebase(new OnFetchDataListener() {
            @Override
            public void onSuccess(List<Character> characterList) {
                // Almacenar los personajes en SQLite para sincronización offline
                for (Character character : characterList) {
                    databaseHelper.addCharacter(character);
                }
                Log.d("FirebaseData", "Personajes sincronizados con SQLite");
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("FirebaseError", "Error al obtener los personajes", e);
            }
        });

        // Botones para elegir el Bando Negro o el Bando Verde
        Button buttonBlackFaction = findViewById(R.id.buttonBlackFaction);
        Button buttonGreenFaction = findViewById(R.id.buttonGreenFaction);

        // Listener para el Bando Negro
        buttonBlackFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BlackFactionActivity.class);
                startActivity(intent);
            }
        });

        // Listener para el Bando Verde
        buttonGreenFaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GreenFactionActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.start(); // Reanuda la reproducción del video cuando la actividad se reanuda
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause(); // Pausa la reproducción del video cuando la actividad está en pausa
        }
    }

    // Método para obtener personajes desde Firebase
    private void fetchCharactersFromFirebase(final OnFetchDataListener listener) {
        db.collection("characters").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Character> characterList = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    String name = document.getString("name");
                    long imageResId = document.getLong("imageResId");
                    String biography = document.getString("biography");

                    Character character = new Character(name, (int) imageResId, biography);
                    characterList.add(character);
                }
                listener.onSuccess(characterList);
            } else {
                listener.onFailure(task.getException());
            }
        });
    }

    // Interfaz para manejar la respuesta de Firebase
    public interface OnFetchDataListener {
        void onSuccess(List<Character> characterList);
        void onFailure(Exception e);
    }
}
