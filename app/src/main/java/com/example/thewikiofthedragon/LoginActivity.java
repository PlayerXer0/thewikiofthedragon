package com.example.thewikiofthedragon;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText editTextNickname, editTextPassword;
    private RadioGroup radioGroupBando;
    private Button buttonLogin, buttonCreateProfile;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextNickname = findViewById(R.id.editTextNickname);
        editTextPassword = findViewById(R.id.editTextPassword);
        radioGroupBando = findViewById(R.id.radioGroupBando);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonCreateProfile = findViewById(R.id.buttonCreateProfile);
        databaseHelper = new DatabaseHelper(this);


        // Botón de login
        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = editTextNickname.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();
                int selectedBandoId = radioGroupBando.getCheckedRadioButtonId();

                // Validar que los campos no estén vacíos
                if (nickname.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, ingresa un nickname", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Por favor, ingresa una contraseña", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedBandoId == -1) {
                    Toast.makeText(LoginActivity.this, "Por favor, selecciona un bando", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Obtener el bando seleccionado
                RadioButton selectedBando = findViewById(selectedBandoId);
                String bando = selectedBando.getText().toString();

                // Agregar logs para depurar
                Log.d("LoginDebug", "Nickname: " + nickname + ", Password: " + password + ", Bando: " + bando);

                try {
                    // Verificar las credenciales
                    if (databaseHelper.validarLogin(nickname, password, bando)) {
                        // Login correcto, ir a MainActivity
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish(); // Para evitar que vuelva al login
                    } else {
                        // Mostrar advertencia si las credenciales son incorrectas
                        Toast.makeText(LoginActivity.this, "Credenciales incorrectas o usuario no registrado", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    // Mostrar una advertencia si ocurre algún error inesperado
                    Toast.makeText(LoginActivity.this, "Ocurrió un error. Verifica los datos e inténtalo de nuevo.", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();  // Para depuración
                }
            }
        });

        // Botón para ir a la actividad de crear perfil
        buttonCreateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PerfilUsuarioActivity.class);
                startActivity(intent);
            }
        });
    }
}
