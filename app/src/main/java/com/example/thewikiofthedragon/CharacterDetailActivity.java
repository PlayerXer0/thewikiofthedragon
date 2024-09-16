package com.example.thewikiofthedragon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CharacterDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_character_detail);

        String characterName = getIntent().getStringExtra("characterName");
        int characterImage = getIntent().getIntExtra("characterImage", 0);
        String characterBiography = getIntent().getStringExtra("characterBiography");

        TextView nameTextView = findViewById(R.id.characterDetailName);
        ImageView imageView = findViewById(R.id.characterDetailImage);
        TextView biographyTextView = findViewById(R.id.characterDetailBiography);

        nameTextView.setText(characterName);
        imageView.setImageResource(characterImage);
        biographyTextView.setText(characterBiography);
    }
}
