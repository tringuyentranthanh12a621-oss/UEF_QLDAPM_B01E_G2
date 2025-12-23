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

        // 2. Xử lý sự kiện nút Back (Quay lại Profile)
        btnBack.setOnClickListener(v -> finish());

        // 3. Xử lý sự kiện xóa thẻ
        btnRemove.setOnClickListener(v -> {
            // Ở đây bạn có thể thêm code xóa dữ liệu khỏi Firebase nếu cần
            // db.collection("users").document(uid).collection("cards").document(cardId).delete()...

            Toast.makeText(this, "Card removed successfully", Toast.LENGTH_SHORT).show();
            finish(); // Đóng màn hình này lại
        });
    }
}