package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class DetailActivity extends AppCompatActivity {
    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        movie = (Movie) getIntent().getSerializableExtra("object");

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        ImageView imgDetail = findViewById(R.id.imgDetail);
        Button btnSession = findViewById(R.id.btnSelectSession);

        if (movie != null) {
            tvTitle.setText(movie.getTitle());
            imgDetail.setImageResource(movie.getImageResId());
        }

        btnSession.setOnClickListener(v -> {
            Intent intent = new Intent(DetailActivity.this, SeatSelectionActivity.class);
            intent.putExtra("movieTitle", movie.getTitle());
            startActivity(intent);
        });
    }
}