package com.example.thewikiofthedragon;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BlackDragonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DragonAdapter dragonAdapter;
    private DatabaseHelper dbHelper;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_dragon);

        // Inicializar el VideoView
        videoView = findViewById(R.id.backgroundVideoViewBlackDragons);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videologin);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(null);  // Ocultar controles
        videoView.setOnCompletionListener(mp -> videoView.start());  // Repetir en bucle
        videoView.start();

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);

        // Obtener todos los dragones del Bando Negro desde la base de datos
        List<Dragon> dragonList = dbHelper.getAllBlackDragons();  // Método que luego definimos en DatabaseHelper

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.blackDragonRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Configurar el adaptador
        dragonAdapter = new DragonAdapter(dragonList, dragon -> {
            // Acciones al hacer clic en un dragón (mostrar detalle)
        });
        recyclerView.setAdapter(dragonAdapter);
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
