package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.firestore.FirebaseFirestore;

public class ManageShowtimeActivity extends AppCompatActivity {

    EditText edtDate, edtTime, edtCinema, edtPrice;
    Button btnAdd;
    TextView tvHeader;
    FirebaseFirestore db;
    String movieId, movieTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_showtime);

        db = FirebaseFirestore.getInstance();
        movieId = getIntent().getStringExtra("movieId");
        movieTitle = getIntent().getStringExtra("movieTitle");

        tvHeader = findViewById(R.id.tvHeader);
        tvHeader.setText("Add Schedule: " + movieTitle);

        edtDate = findViewById(R.id.edtShowDate);
        edtTime = findViewById(R.id.edtShowTime);
        edtCinema = findViewById(R.id.edtCinemaName);
        edtPrice = findViewById(R.id.edtPrice);
        btnAdd = findViewById(R.id.btnAddShowtime);

        btnAdd.setOnClickListener(v -> addShowtime());
    }

    private void addShowtime() {
        String date = edtDate.getText().toString().trim();
        String time = edtTime.getText().toString().trim();
        String cinema = edtCinema.getText().toString().trim();
        String price = edtPrice.getText().toString().trim();

        if (date.isEmpty() || time.isEmpty() || cinema.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        Showtime showtime = new Showtime(movieId, cinema, date, time, price);

        db.collection("showtimes").add(showtime)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(this, "Showtime Added!", Toast.LENGTH_SHORT).show();
                    finish();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }
}