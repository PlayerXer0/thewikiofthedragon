package com.example.thewikiofthedragon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private VideoView videoView;

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

        // Botón de creación de perfil
        ImageButton btnCrearPerfil = findViewById(R.id.btnCrearPerfil);

        // Listener para abrir la actividad de creación de perfil
        btnCrearPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PerfilUsuarioActivity.class);
                startActivity(intent);
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
}
