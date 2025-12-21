package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView; // <--- Thêm dòng này để dùng TextView
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // --- 1. PHẦN CLIENT (KHÁCH HÀNG) ---
        EditText edtPhone = findViewById(R.id.edtPhone);
        Button btnContinue = findViewById(R.id.btnContinue);

        btnContinue.setOnClickListener(v -> {
            String phone = edtPhone.getText().toString();
            if (!phone.isEmpty()) {
                Intent intent = new Intent(LoginActivity.this, LoginPhoneActivity.class);
                intent.putExtra("phone", phone);
                startActivity(intent);
            }
        });

        // --- 2. PHẦN ADMIN (MỚI THÊM) ---
        // Tìm TextView "Login as Admin" mà chúng ta vừa thêm vào XML
        TextView tvLoginAdmin = findViewById(R.id.tvLoginAdmin);

        // Khi ấn vào chữ này thì chuyển sang trang quản trị
        if (tvLoginAdmin != null) {
            tvLoginAdmin.setOnClickListener(v -> {
                Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                startActivity(intent);
            });
        }
    }
}