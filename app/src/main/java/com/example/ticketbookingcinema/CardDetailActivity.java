package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class CardDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_detail);

        // 1. Ánh xạ View
        Button btnRemove = findViewById(R.id.btnRemoveCard);
        ImageView btnBack = findViewById(R.id.btnBackCardDetail);

        // 2. Xử lý sự kiện nút Back
        btnBack.setOnClickListener(v -> finish());

        // 3. Xử lý sự kiện xóa thẻ
        btnRemove.setOnClickListener(v -> {
            // ... code xóa Firebase ...

            // SỬA: Dùng getString để hỗ trợ Đa ngôn ngữ
            Toast.makeText(this, getString(R.string.msg_card_removed), Toast.LENGTH_SHORT).show();

            finish();
        });
    }
}