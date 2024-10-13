package com.example.thewikiofthedragon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.VideoView;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextContrasena;
    private RadioGroup radioGroupBando;
    private Button buttonCrearPerfil, buttonEliminarPerfil;
    private DatabaseHelper databaseHelper;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // Configurar el VideoView
        videoView = findViewById(R.id.backgroundVideoViewProfile);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videologin);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(null); // Ocultar controles
        videoView.setOnCompletionListener(mp -> videoView.start()); // Repetir en bucle
        videoView.start();

        // Vinculación de las vistas
        editTextNombre = findViewById(R.id.editTextNombre);
        editTextContrasena = findViewById(R.id.editTextContrasena);
        radioGroupBando = findViewById(R.id.radioGroupBando);
        buttonCrearPerfil = findViewById(R.id.btnGuardarPerfil);
        buttonEliminarPerfil = findViewById(R.id.btnEliminarPerfil);
        databaseHelper = new DatabaseHelper(this);

        // Crear perfil
        buttonCrearPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();
                String contrasena = editTextContrasena.getText().toString().trim();
                int selectedBandoId = radioGroupBando.getCheckedRadioButtonId();

                if (nombre.isEmpty()) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Por favor, ingresa un nombre", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (contrasena.isEmpty()) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Por favor, ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedBandoId == -1) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Selecciona un bando", Toast.LENGTH_SHORT).show();
                    return;
                }

                RadioButton selectedBando = findViewById(selectedBandoId);
                String bando = selectedBando.getText().toString();

                long id = databaseHelper.agregarUsuario(nombre, "", contrasena, bando, "");
                if (id > 0) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Perfil creado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();

                    // Animación de regreso
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(PerfilUsuarioActivity.this, "Error al crear el perfil. Revisa los datos e inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Eliminar perfil
        buttonEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = editTextNombre.getText().toString().trim();

                if (nombre.isEmpty()) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Por favor, ingresa el nombre del perfil a eliminar", Toast.LENGTH_SHORT).show();
                    return;
                }

                boolean eliminado = databaseHelper.eliminarUsuarioPorNickname(nombre);
                if (eliminado) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Perfil eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    finish();

                    // Animación de regreso
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                } else {
                    Toast.makeText(PerfilUsuarioActivity.this, "Error al eliminar el perfil. Revisa el nombre.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.start(); // Reanuda el video cuando la actividad está en foco
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause(); // Pausa el video cuando la actividad pierde foco
        }
    }
}
