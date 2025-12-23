package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        // Ánh xạ View
        Button btnAdd = findViewById(R.id.btnConfirmAddCard);
        ImageView btnBack = findViewById(R.id.btnBackAddCard);

        // (Nếu muốn lấy dữ liệu sau này)
        EditText edtCardNumber = findViewById(R.id.edtCardNumber);
        EditText edtCardHolder = findViewById(R.id.edtCardHolder);
        EditText edtExpiry = findViewById(R.id.edtExpiry);
        EditText edtCvv = findViewById(R.id.edtCvv);

        // 1. Xử lý nút Back (Quay lại Profile)
        btnBack.setOnClickListener(v -> finish());

        // 2. Xử lý nút Add
        btnAdd.setOnClickListener(v -> {
            // Kiểm tra sơ bộ (Validation)
            if (edtCardNumber.getText().toString().isEmpty() || edtCardHolder.getText().toString().isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            } else {
                // Giả lập thêm thành công
                Toast.makeText(this, "Card added successfully!", Toast.LENGTH_SHORT).show();
                finish(); // Đóng màn hình này, quay về Profile
            }
        });
    }
}