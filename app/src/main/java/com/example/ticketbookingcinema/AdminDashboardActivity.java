package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        Button btnMovies = findViewById(R.id.btnManageMovies);
        Button btnUsers = findViewById(R.id.btnManageUsers);
        Button btnLogout = findViewById(R.id.btnAdminLogout);

        // Chuyển sang quản lý phim (Sẽ làm ở bước sau)
        btnMovies.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminMovieActivity.class);
            startActivity(intent);
        });

        // Chuyển sang quản lý người dùng
        btnUsers.setOnClickListener(v -> {
            Intent intent = new Intent(AdminDashboardActivity.this, AdminUserActivity.class);
            startActivity(intent);
        });

        btnLogout.setOnClickListener(v -> finish());
    }
}