package com.example.thewikiofthedragon;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class BlackDragonActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DragonAdapter dragonAdapter;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_dragon);

        // Inicializar la base de datos
        dbHelper = new DatabaseHelper(this);

        // Obtener todos los dragones del Bando Negro desde la base de datos
        List<Dragon> dragonList = dbHelper.getAllBlackDragons();  // Método que luego definimos

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
    protected void onDestroy() {
        super.onDestroy();
        // No cerrar la base de datos aquí para evitar problemas
    }
}
