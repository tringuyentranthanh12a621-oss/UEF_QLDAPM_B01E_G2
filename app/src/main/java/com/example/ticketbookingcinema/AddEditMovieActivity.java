package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddEditMovieActivity extends AppCompatActivity {
    EditText edtTitle, edtCategory, edtDuration, edtRating;
    Button btnSave, btnDelete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit_movie);

        edtTitle = findViewById(R.id.edtMovieTitle);
        edtCategory = findViewById(R.id.edtCategory);
        edtDuration = findViewById(R.id.edtDuration);
        edtRating = findViewById(R.id.edtRating);
        btnSave = findViewById(R.id.btnSaveMovie);
        btnDelete = findViewById(R.id.btnDeleteMovie);

        // Xử lý nút Lưu
        btnSave.setOnClickListener(v -> {
            String title = edtTitle.getText().toString();
            if(title.isEmpty()) {
                Toast.makeText(this, "Please enter title", Toast.LENGTH_SHORT).show();
            } else {
                // Ở đây bạn sẽ code logic lưu vào Database (SQLite/Firebase).
                // Vì hiện tại chúng ta dùng list giả, nên chỉ thông báo thành công.
                Toast.makeText(this, "Movie Saved: " + title, Toast.LENGTH_SHORT).show();
                finish(); // Quay lại danh sách
            }
        });

        // Xử lý nút Xóa
        btnDelete.setOnClickListener(v -> {
            Toast.makeText(this, "Movie Deleted", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}