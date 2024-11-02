package com.example.thewikiofthedragon;

import android.net.Uri;
import android.os.Bundle;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GreenDragonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DragonAdapter dragonAdapter;
    private DatabaseHelper dbHelper;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_green_dragon);

        // Inicializar el VideoView
        videoView = findViewById(R.id.backgroundVideoViewGreenDragons);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videologin); // Cambia a tu video deseado
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(null);  // Ocultar controles

        // Configurar el video para que se ajuste al tamaño de la pantalla y se repita
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        });

        videoView.start();

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);

        // Obtener todos los dragones del Bando Verde desde la base de datos
        List<Dragon> greenDragonList = dbHelper.getAllGreenDragons();

        // Configurar el RecyclerView
        recyclerView = findViewById(R.id.greenDragonRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Configurar el adaptador
        dragonAdapter = new DragonAdapter(greenDragonList, dragon -> {
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
