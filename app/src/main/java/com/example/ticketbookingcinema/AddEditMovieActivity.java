package com.example.ticketbookingcinema;

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

    // Khai báo biến
    EditText edtTitle, edtCategory, edtDuration, edtRating, edtImageName;
    EditText edtDescription, edtDescriptionVi, edtDescriptionRu; // 3 ô mô tả
    Button btnSave, btnDelete;
    TextView tvHeader;

    FirebaseFirestore db;
    String movieId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        tvHeader = findViewById(R.id.tvHeaderMovie);
        edtTitle = findViewById(R.id.edtMovieTitle);
        edtCategory = findViewById(R.id.edtCategory);
        edtDuration = findViewById(R.id.edtDuration);
        edtRating = findViewById(R.id.edtRating);
        edtImageName = findViewById(R.id.edtImageName);

        // Ánh xạ 3 ô nhập mô tả
        edtDescription = findViewById(R.id.edtDescription);     // Anh
        edtDescriptionVi = findViewById(R.id.edtDescriptionVi); // Việt
        edtDescriptionRu = findViewById(R.id.edtDescriptionRu); // Nga

        btnSave = findViewById(R.id.btnSaveMovie);
        btnDelete = findViewById(R.id.btnDeleteMovie);

        // --- LOAD DỮ LIỆU CŨ KHI SỬA ---
        if (getIntent().hasExtra("movieId")) {
            tvHeader.setText("Edit Movie");
            movieId = getIntent().getStringExtra("movieId");
            Movie movie = (Movie) getIntent().getSerializableExtra("movieData");

            if (movie != null) {
                edtTitle.setText(movie.getTitle());
                edtCategory.setText(movie.getCategory());
                edtDuration.setText(movie.getDuration());
                edtRating.setText(movie.getRating());
                edtImageName.setText(movie.getPicUrl());

                // Điền dữ liệu mô tả cũ vào đúng ô
                // Lưu ý: Dùng getter cụ thể (_vi, _ru) để lấy text thô
                if (movie.getDescription() != null) edtDescription.setText(movie.getDescription());
                if (movie.getDescription_vi() != null) edtDescriptionVi.setText(movie.getDescription_vi());
                if (movie.getDescription_ru() != null) edtDescriptionRu.setText(movie.getDescription_ru());
            }
            btnDelete.setVisibility(View.VISIBLE);
        } else {
            tvHeader.setText("Add New Movie");
            btnDelete.setVisibility(View.GONE);
        }

        btnSave.setOnClickListener(v -> saveMovie());
        btnDelete.setOnClickListener(v -> deleteMovie());
    }

    private void saveMovie() {
        String title = edtTitle.getText().toString().trim();
        String category = edtCategory.getText().toString().trim();
        String duration = edtDuration.getText().toString().trim();
        String rating = edtRating.getText().toString().trim();
        String imageName = edtImageName.getText().toString().trim();

        // Lấy dữ liệu 3 ngôn ngữ từ ô nhập
        String descEn = edtDescription.getText().toString().trim();
        String descVi = edtDescriptionVi.getText().toString().trim();
        String descRu = edtDescriptionRu.getText().toString().trim();

        if (title.isEmpty() || imageName.isEmpty()) {
            Toast.makeText(this, "Title and Image Name are required!", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> movieMap = new HashMap<>();
        movieMap.put("title", title);
        movieMap.put("category", category);
        movieMap.put("duration", duration);
        movieMap.put("rating", rating);
        movieMap.put("picUrl", imageName); // GIỮ NGUYÊN PHẦN ẢNH

        // LƯU 3 TRƯỜNG MÔ TẢ RIÊNG BIỆT
        movieMap.put("description", descEn);    // Tiếng Anh
        movieMap.put("description_vi", descVi); // Tiếng Việt
        movieMap.put("description_ru", descRu); // Tiếng Nga

        if (movieId == null) {
            // Thêm mới
            db.collection("movies").add(movieMap)
                    .addOnSuccessListener(doc -> {
                        Toast.makeText(this, "Movie Added!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            // Cập nhật
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