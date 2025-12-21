package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CardDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        Button btnRemove = findViewById(R.id.btnRemoveCard);

        btnRemove.setOnClickListener(v -> {
            Toast.makeText(this, "Card removed", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}