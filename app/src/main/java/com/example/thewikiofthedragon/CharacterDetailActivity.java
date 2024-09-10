package com.example.thewikiofthedragon;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

public class CharacterDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);

        String characterName = getIntent().getStringExtra("characterName");
        int characterImage = getIntent().getIntExtra("characterImage", 0);

        TextView nameTextView = findViewById(R.id.characterDetailName);
        ImageView imageView = findViewById(R.id.characterDetailImage);

        nameTextView.setText(characterName);
        imageView.setImageResource(characterImage);
    }
}
