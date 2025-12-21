package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1. Nút Back (Quay lại trang chủ)
        ImageView btnBack = findViewById(R.id.btnBackProfile);
        btnBack.setOnClickListener(v -> finish());

        // 2. Nút Logout (Đăng xuất) -> Về màn hình Login và xóa hết lịch sử
        ImageView btnLogout = findViewById(R.id.btnLogout);
        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            // Xóa cờ (Flag) để người dùng không bấm Back quay lại được Profile sau khi đã Logout
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // 3. Nút "Add new card" -> Mở màn hình AddCardActivity
        Button btnAddCard = findViewById(R.id.btnAddCard);
        btnAddCard.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddCardActivity.class);
            startActivity(intent);
        });

        // 4. Bấm vào thẻ Visa có sẵn -> Mở màn hình CardDetailActivity (để xóa thẻ)
        View cardVisa = findViewById(R.id.cardVisa);
        if (cardVisa != null) {
            cardVisa.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, CardDetailActivity.class);
                startActivity(intent);
            });
        }
    }
}