package com.example.thewikiofthedragon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        // Botón para acceder a la trivia
        ImageButton btnTrivia = findViewById(R.id.btnTrivia);
        btnTrivia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TriviaActivity.class);
                startActivity(intent);
            }
        });

        // Subir preguntas de trivia (llama esto solo una vez)
        subirPreguntasTriviaAFirebase();
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

    // Método para subir preguntas de trivia a Firebase
    private void subirPreguntasTriviaAFirebase() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        List<Map<String, Object>> preguntas = new ArrayList<>();

        // Agregar las preguntas a la lista
        preguntas.add(createQuestion("¿Quién es la heredera original al Trono de Hierro al comienzo de 'La Casa del Dragón'?", "Rhaenyra Targaryen",
                new String[]{"Alicent Hightower", "Daemon Targaryen", "Viserys I Targaryen"}));

        preguntas.add(createQuestion("¿Cuál es el lema de la Casa Targaryen?", "Fuego y Sangre",
                new String[]{"El Invierno se Acerca", "Somos los que Sembramos", "Nunca Doblados, Nunca Rotos"}));

        preguntas.add(createQuestion("¿Quién es el esposo de Rhaenyra Targaryen al comienzo de la serie?", "Laenor Velaryon",
                new String[]{"Criston Cole", "Daemon Targaryen", "Harwin Strong"}));

        preguntas.add(createQuestion("¿Qué dragón monta Daemon Targaryen?", "Caraxes",
                new String[]{"Syrax", "Vhagar", "Sunfyre"}));

        preguntas.add(createQuestion("¿Quién forjó el Trono de Hierro?", "Aegon I Targaryen",
                new String[]{"Rhaenyra Targaryen", "Daemon Targaryen", "Viserys I Targaryen"}));

        preguntas.add(createQuestion("¿Qué casa controla Driftmark?", "Casa Velaryon",
                new String[]{"Casa Stark", "Casa Hightower", "Casa Baratheon"}));

        preguntas.add(createQuestion("¿Cuál es el nombre del dragón de Rhaenyra Targaryen?", "Syrax",
                new String[]{"Caraxes", "Vhagar", "Meleys"}));

        preguntas.add(createQuestion("¿Quién es la Mano del Rey al comienzo de 'La Casa del Dragón'?", "Otto Hightower",
                new String[]{"Daemon Targaryen", "Corlys Velaryon", "Harwin Strong"}));

        preguntas.add(createQuestion("¿Cómo se llama la espada ancestral de la Casa Targaryen?", "Fuegoscuro",
                new String[]{"Hermana Oscura", "Garra", "Veneno del Corazón"}));

        preguntas.add(createQuestion("¿Qué evento marca el comienzo del conflicto en 'La Casa del Dragón'?", "El nombramiento de Rhaenyra como heredera",
                new String[]{"La muerte de Viserys I", "El nacimiento de Aegon II", "La boda de Rhaenyra"}));

        // Subir cada pregunta a Firestore
        for (Map<String, Object> pregunta : preguntas) {
            db.collection("trivia")
                    .add(pregunta)
                    .addOnSuccessListener(documentReference -> Log.d("Firestore", "Pregunta añadida con ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.e("Firestore", "Error al añadir la pregunta", e));
        }
    }

    private Map<String, Object> createQuestion(String question, String correctAnswer, String[] incorrectAnswers) {
        Map<String, Object> pregunta = new HashMap<>();
        pregunta.put("question", question);
        pregunta.put("correct_answer", correctAnswer);
        pregunta.put("incorrect_answers", Arrays.asList(incorrectAnswers));
        return pregunta;
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
