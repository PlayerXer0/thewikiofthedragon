package com.example.thewikiofthedragon;

import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private EditText nombreEditText, emailEditText, contrasenaEditText;
    private Spinner spinnerBando, spinnerCasa;
    private Button btnGuardarPerfil, btnEliminarPerfil;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // Vinculación de elementos
        nombreEditText = findViewById(R.id.editTextNombre);
        emailEditText = findViewById(R.id.editTextEmail);
        contrasenaEditText = findViewById(R.id.editTextContrasena);
        spinnerBando = findViewById(R.id.spinnerBando);
        spinnerCasa = findViewById(R.id.spinnerCasa);
        btnGuardarPerfil = findViewById(R.id.btnGuardarPerfil);
        btnEliminarPerfil = findViewById(R.id.btnEliminarPerfil);

        // Inicialización de la base de datos
        db = new DatabaseHelper(this);

        // Botón para guardar o modificar perfil
        btnGuardarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = nombreEditText.getText().toString();
                String email = emailEditText.getText().toString();
                String contrasena = contrasenaEditText.getText().toString();
                String bando = spinnerBando.getSelectedItem().toString();
                String casaFav = spinnerCasa.getSelectedItem().toString();

                if (!nombre.isEmpty() && !email.isEmpty() && !contrasena.isEmpty()) {
                    db.agregarUsuario(nombre, email, contrasena, bando, casaFav);
                    Toast.makeText(PerfilUsuarioActivity.this, "Perfil guardado exitosamente", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(PerfilUsuarioActivity.this, "Por favor completa todos los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Botón para eliminar perfil
        btnEliminarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Aquí asumimos que tienes el ID del perfil a eliminar
                int id = 1; // Cambia esto según tu lógica para obtener el ID del usuario

                db.eliminarUsuario(id);
                Toast.makeText(PerfilUsuarioActivity.this, "Perfil eliminado exitosamente", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
