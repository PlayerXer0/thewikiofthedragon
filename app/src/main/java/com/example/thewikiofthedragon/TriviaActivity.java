package com.example.thewikiofthedragon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class TriviaActivity extends AppCompatActivity {

    private TextView questionTextView;
    private RadioGroup optionsRadioGroup;
    private Button nextButton;
    private VideoView videoView;

    private MediaPlayer mediaPlayer;
    private boolean isMusicPlaying = true; // Estado de la música
    private ImageButton btnToggleMusic; // Botón para controlar la música

    private FirebaseFirestore db;
    private List<Map<String, Object>> questionsList = new ArrayList<>();
    private int currentQuestionIndex = 0;
    private int score = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trivia);

        // Inicializar el VideoView
        videoView = findViewById(R.id.backgroundVideoViewTrivia);
        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.videologin);
        videoView.setVideoURI(videoUri);
        videoView.setMediaController(null); // Ocultar controles

        // Configurar el video para que se ajuste al tamaño de la pantalla
        videoView.setOnPreparedListener(mp -> {
            mp.setLooping(true);
            mp.setVideoScalingMode(android.media.MediaPlayer.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING);
        });
        videoView.start();

        // Inicializar música de fondo
        mediaPlayer = MediaPlayer.create(this, R.raw.troc); // Archivo de música en res/raw/trivia_music.mp3
        mediaPlayer.setLooping(true); // Reproducir en bucle
        mediaPlayer.start();

        // Inicializar componentes
        questionTextView = findViewById(R.id.questionTextView);
        optionsRadioGroup = findViewById(R.id.optionsRadioGroup);
        nextButton = findViewById(R.id.nextButton);
        btnToggleMusic = findViewById(R.id.btnToggleMusicTrivia);

        // Configurar el botón de silenciar/activar música
        btnToggleMusic.setOnClickListener(v -> toggleMusic());

        // Inicializar Firebase
        db = FirebaseFirestore.getInstance();

        // Cargar preguntas desde Firebase
        loadQuestionsFromFirebase();

        // Configurar el botón "Siguiente"
        nextButton.setOnClickListener(v -> checkAnswerAndLoadNextQuestion());
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (videoView != null) {
            videoView.start(); // Reanudar el video al volver a la actividad
        }
        if (mediaPlayer != null && isMusicPlaying) {
            mediaPlayer.start(); // Reanudar la música
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (videoView != null) {
            videoView.pause(); // Pausar el video
        }
        if (mediaPlayer != null && isMusicPlaying) {
            mediaPlayer.pause(); // Pausar la música
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null; // Liberar recursos de la música
        }
    }

    private void toggleMusic() {
        if (isMusicPlaying) {
            mediaPlayer.pause();
            btnToggleMusic.setImageResource(R.drawable.ic_off_n); // Cambiar el ícono a apagado
        } else {
            mediaPlayer.start();
            btnToggleMusic.setImageResource(R.drawable.ic_on_n); // Cambiar el ícono a encendido
        }
        isMusicPlaying = !isMusicPlaying;
    }

    private void finishTrivia() {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("score", score); // Pasar el puntaje final
        intent.putExtra("totalQuestions", questionsList.size()); // Pasar el número total de preguntas
        startActivity(intent);
        finish(); // Finalizar TriviaActivity para que no pueda volver atrás
    }

    private void loadQuestionsFromFirebase() {
        db.collection("trivia").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Map<String, Object>> uniqueQuestions = new ArrayList<>();
                for (QueryDocumentSnapshot document : task.getResult()) {
                    Map<String, Object> questionData = document.getData();
                    if (!uniqueQuestions.contains(questionData)) {
                        uniqueQuestions.add(questionData);
                    }
                }
                questionsList.clear();
                questionsList.addAll(uniqueQuestions);

                if (!questionsList.isEmpty()) {
                    showQuestion();
                } else {
                    Toast.makeText(TriviaActivity.this, "No hay preguntas disponibles", Toast.LENGTH_SHORT).show();
                }
            } else {
                Log.e("TriviaActivity", "Error al cargar preguntas", task.getException());
                Toast.makeText(TriviaActivity.this, "Error al cargar preguntas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showQuestion() {
        if (currentQuestionIndex >= questionsList.size()) {
            finishTrivia();
            return;
        }

        optionsRadioGroup.clearCheck();
        optionsRadioGroup.removeAllViews();

        // Obtener la pregunta actual
        Map<String, Object> currentQuestion = questionsList.get(currentQuestionIndex);
        String question = (String) currentQuestion.get("question");
        String correctAnswer = (String) currentQuestion.get("correct_answer");
        List<String> incorrectAnswers = (List<String>) currentQuestion.get("incorrect_answers");

        // Validar los datos
        if (question == null || correctAnswer == null || incorrectAnswers == null) {
            Toast.makeText(this, "Error: Pregunta inválida o incompleta", Toast.LENGTH_SHORT).show();
            currentQuestionIndex++;
            if (currentQuestionIndex < questionsList.size()) {
                showQuestion();
            } else {
                finishTrivia();
            }
            return;
        }

        // Mezclar opciones
        List<String> options = new ArrayList<>(incorrectAnswers);
        options.add(correctAnswer);
        Collections.shuffle(options);

        // Mostrar la pregunta
        questionTextView.setText(question);

        // Agregar opciones al RadioGroup
        for (String option : options) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(option);
            radioButton.setTextSize(18);
            radioButton.setTextColor(getResources().getColor(R.color.gold_text));
            optionsRadioGroup.addView(radioButton);
        }
    }

    private void checkAnswerAndLoadNextQuestion() {
        // Verificar si se seleccionó una respuesta
        int selectedOptionId = optionsRadioGroup.getCheckedRadioButtonId();
        if (selectedOptionId == -1) {
            Toast.makeText(this, "Por favor selecciona una respuesta", Toast.LENGTH_SHORT).show();
            return;
        }

        // Verificar si la respuesta es correcta
        RadioButton selectedRadioButton = findViewById(selectedOptionId);
        String selectedAnswer = selectedRadioButton.getText().toString();
        String correctAnswer = (String) questionsList.get(currentQuestionIndex).get("correct_answer");

        if (selectedAnswer.equals(correctAnswer)) {
            score++;
            Toast.makeText(this, "¡Correcto!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Incorrecto. La respuesta era: " + correctAnswer, Toast.LENGTH_SHORT).show();
        }

        // Cargar la siguiente pregunta o finalizar la trivia
        currentQuestionIndex++;
        if (currentQuestionIndex < questionsList.size()) {
            showQuestion();
        } else {
            finishTrivia();
        }
    }
}
