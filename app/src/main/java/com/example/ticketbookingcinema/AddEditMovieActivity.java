package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;

public class AddEditMovieActivity extends AppCompatActivity {
    EditText edtTitle, edtCategory, edtDuration, edtRating, edtDescription, edtImageName;
    Button btnSave, btnDelete;
    TextView tvHeader;

    FirebaseFirestore db;
    String movieId = null; // Biến để lưu ID phim nếu đang ở chế độ Sửa

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        tvHeader = findViewById(R.id.tvHeaderMovie);
        edtTitle = findViewById(R.id.edtMovieTitle);
        edtCategory = findViewById(R.id.edtCategory);
        edtDuration = findViewById(R.id.edtDuration);
        edtRating = findViewById(R.id.edtRating);
        edtDescription = findViewById(R.id.edtDescription); // Mới thêm
        edtImageName = findViewById(R.id.edtImageName);     // Mới thêm
        btnSave = findViewById(R.id.btnSaveMovie);
        btnDelete = findViewById(R.id.btnDeleteMovie);

        // --- KIỂM TRA: ĐANG THÊM HAY ĐANG SỬA? ---
        if (getIntent().hasExtra("movieId")) {
            // Chế độ SỬA
            tvHeader.setText("Edit Movie");
            movieId = getIntent().getStringExtra("movieId");

            // Lấy dữ liệu cũ điền vào ô
            Movie movie = (Movie) getIntent().getSerializableExtra("movieData");
            if (movie != null) {
                edtTitle.setText(movie.getTitle());
                edtCategory.setText(movie.getCategory());
                edtDuration.setText(movie.getDuration());
                edtRating.setText(movie.getRating());
                edtDescription.setText(movie.getDescription());
                edtImageName.setText(movie.getPicUrl());
            }
            btnDelete.setVisibility(View.VISIBLE); // Hiện nút xóa
        } else {
            // Chế độ THÊM MỚI
            tvHeader.setText("Add New Movie");
            btnDelete.setVisibility(View.GONE); // Ẩn nút xóa
        }

        // --- SỰ KIỆN LƯU ---
        btnSave.setOnClickListener(v -> saveMovie());

        // --- SỰ KIỆN XÓA ---
        btnDelete.setOnClickListener(v -> deleteMovie());
    }

    private void saveMovie() {
        String title = edtTitle.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String duration = edtDuration.getText().toString().trim();
        String rating = edtRating.getText().toString().trim();
        String description = edtDescription.getText().toString().trim();
        String imageName = edtImageName.getText().toString().trim();

        if (title.isEmpty() || imageName.isEmpty()) {
            Toast.makeText(this, "Title and Image Name are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo Map dữ liệu
        Map<String, Object> movieMap = new HashMap<>();
        movieMap.put("title", title);
        movieMap.put("category", category);
        movieMap.put("duration", duration);
        movieMap.put("rating", rating);
        movieMap.put("description", description);
        movieMap.put("picUrl", imageName); // Lưu tên ảnh (ví dụ: "the_batman")

        if (movieId == null) {
            // === THÊM MỚI ===
            db.collection("movies").add(movieMap)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, "Movie Added!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // === CẬP NHẬT ===
            db.collection("movies").document(movieId).update(movieMap)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Movie Updated!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void deleteMovie() {
        if (movieId != null) {
            db.collection("movies").document(movieId).delete()
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Movie Deleted!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error deleting", Toast.LENGTH_SHORT).show());
        }
    }
}