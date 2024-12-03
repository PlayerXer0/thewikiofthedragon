package com.example.thewikiofthedragon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class GreenFactionActivity extends AppCompatActivity {

    private Button btnGreenCharacters, btnGreenDragons;
    private ImageButton btnToggleMusic;
    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_faction);

        // Vincular los botones
        btnGreenCharacters = findViewById(R.id.btnGreenCharacters);
        btnGreenDragons = findViewById(R.id.btnGreenDragons);
        btnToggleMusic = findViewById(R.id.btnToggleMusicGreen);

        // Configurar la acción del botón "Personajes"
        btnGreenCharacters.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreenFactionActivity.this, GreenFactionCharactersActivity.class);
                startActivity(intent);
            }
        });

        // Configurar la acción del botón "Dragones"
        btnGreenDragons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GreenFactionActivity.this, GreenDragonActivity.class);
                startActivity(intent);
            }
        });

        // Inicializar el reproductor de música
        mediaPlayer = MediaPlayer.create(this, R.raw.bando_verde); // Archivo de música en res/raw/bando_verde.mp3
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
