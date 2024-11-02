package com.example.thewikiofthedragon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;

public class BlackFactionActivity extends AppCompatActivity {

    private Button btnCharacters, btnDragons;
    private ImageButton btnToggleMusic;
    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_faction);

        // Vincular los botones
        btnCharacters = findViewById(R.id.btnCharacters);
        btnDragons = findViewById(R.id.btnDragons);
        btnToggleMusic = findViewById(R.id.btnToggleMusic);

        // Configurar la acción del botón "Personajes"
        btnCharacters.setOnClickListener(v -> {
            Intent intent = new Intent(BlackFactionActivity.this, BlackFactionCharactersActivity.class);
            startActivity(intent);
        });

        // Configurar la acción del botón "Dragones"
        btnDragons.setOnClickListener(v -> {
            Intent intent = new Intent(BlackFactionActivity.this, BlackDragonActivity.class);
            startActivity(intent);
        });

        // Inicializar el reproductor de música
        mediaPlayer = MediaPlayer.create(this, R.raw.bando_negro); // Archivo de música en res/raw/bando_negro.mp3
        mediaPlayer.setLooping(true); // Repetir en bucle
        mediaPlayer.start();

        // Configurar el botón de silenciar/activar música
        btnToggleMusic.setOnClickListener(v -> toggleMusic());
    }

    // Método para silenciar o activar la música
    private void toggleMusic() {
        if (isMusicPlaying) {
            mediaPlayer.pause();
            btnToggleMusic.setImageResource(R.drawable.ic_off_n); // Cambia el icono a apagado
        } else {
            mediaPlayer.start();
            btnToggleMusic.setImageResource(R.drawable.ic_on_n); // Cambia el icono a encendido
        }
        isMusicPlaying = !isMusicPlaying;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer != null && isMusicPlaying) {
            mediaPlayer.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mediaPlayer != null && isMusicPlaying) {
            mediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}
