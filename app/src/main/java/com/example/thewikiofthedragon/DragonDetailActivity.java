package com.example.thewikiofthedragon;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DragonDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dragon_detail);

        String dragonName = getIntent().getStringExtra("dragonName");
        int dragonImage = getIntent().getIntExtra("dragonImage", 0);
        String dragonBiography = getIntent().getStringExtra("dragonBiography");

        TextView nameTextView = findViewById(R.id.dragonDetailName);
        ImageView imageView = findViewById(R.id.dragonDetailImage);
        TextView biographyTextView = findViewById(R.id.dragonDetailBiography);

        nameTextView.setText(dragonName);
        imageView.setImageResource(dragonImage);
        biographyTextView.setText(dragonBiography);
    }
}
