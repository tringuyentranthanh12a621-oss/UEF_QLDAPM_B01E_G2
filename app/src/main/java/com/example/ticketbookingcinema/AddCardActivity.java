package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddCardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        Button btnAdd = findViewById(R.id.btnConfirmAddCard);

        // Xử lý nút Add -> Chỉ hiển thị thông báo và quay lại
        btnAdd.setOnClickListener(v -> {
            Toast.makeText(this, "Card added successfully!", Toast.LENGTH_SHORT).show();
            finish(); // Đóng màn hình này, quay về Profile
        });
    }
}