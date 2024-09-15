package com.example.thewikiofthedragon;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class PerfilUsuarioActivity extends AppCompatActivity {

    private EditText editTextNombre, editTextContrasena; // Actualizar los nombres de las variables
    private RadioGroup radioGroupBando;
    private Button buttonCrearPerfil, buttonEliminarPerfil;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);

        // Vinculación de las vistas, asegurándose que coincidan con los IDs del XML
        editTextNombre = findViewById(R.id.editTextNombre); // Este coincide con el XML
        editTextContrasena = findViewById(R.id.editTextContrasena); // Este coincide con el XML
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

                // Obtener el bando seleccionado
                RadioButton selectedBando = findViewById(selectedBandoId);
                String bando = selectedBando.getText().toString();

                // Intentar agregar el usuario
                long id = databaseHelper.agregarUsuario(nombre, "", contrasena, bando, "");
                if (id > 0) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Perfil creado exitosamente", Toast.LENGTH_SHORT).show();
                    finish(); // Regresar al login después de crear el perfil
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

                // Eliminar el perfil de la base de datos
                boolean eliminado = databaseHelper.eliminarUsuarioPorNickname(nombre);

                if (eliminado) {
                    Toast.makeText(PerfilUsuarioActivity.this, "Perfil eliminado exitosamente", Toast.LENGTH_SHORT).show();
                    finish(); // Regresar al login después de eliminar el perfil
                } else {
                    Toast.makeText(PerfilUsuarioActivity.this, "Error al eliminar el perfil. Revisa el nombre.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
