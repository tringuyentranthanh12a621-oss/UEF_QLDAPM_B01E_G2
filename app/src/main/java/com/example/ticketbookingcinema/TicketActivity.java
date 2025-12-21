package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class TicketActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket);

        // 1. Nhận dữ liệu từ Intent
        // (Lưu ý: Nếu bên OrderConfirmation chưa truyền "totalPrice", thì ở đây sẽ hiện 0 T)
        String movieTitle = getIntent().getStringExtra("movieTitle");
        ArrayList<String> seats = getIntent().getStringArrayListExtra("seats");

        // Bạn có thể update OrderConfirmationActivity để truyền thêm totalPrice sang nếu muốn
        // int cost = getIntent().getIntExtra("totalPrice", 0);

        // 2. Ánh xạ View
        TextView tvTitle = findViewById(R.id.tvTicketMovieTitle);
        TextView tvSeats = findViewById(R.id.tvTicketSeats);
        // TextView tvCost = findViewById(R.id.tvTicketCost); // Nếu muốn set giá

        ImageView btnClose = findViewById(R.id.btnCloseTicket);
        Button btnHome = findViewById(R.id.btnHome); // Nút màu cam (Send)
        Button btnRefund = findViewById(R.id.btnRefund); // Nút màu tối

        // 3. Hiển thị dữ liệu
        if (movieTitle != null) {
            tvTitle.setText(movieTitle);
        }

        if (seats != null) {
            // Xử lý chuỗi hiển thị ghế cho đẹp
            String displaySeats = seats.toString().replace("[", "").replace("]", "");
            tvSeats.setText(displaySeats);
        }

        // tvCost.setText(cost + " T (paid)"); // Mở comment nếu bạn đã truyền giá sang

        // 4. Xử lý sự kiện click

        // Nút Đóng (X) -> Về màn hình chính
        btnClose.setOnClickListener(v -> returnToMain());

        // Nút Send (Home) -> Thông báo giả lập gửi vé rồi về màn hình chính
        btnHome.setOnClickListener(v -> {
            Toast.makeText(this, "Ticket sent to your email!", Toast.LENGTH_SHORT).show();
            returnToMain();
        });

        // Nút Refund -> Thông báo giả lập
        btnRefund.setOnClickListener(v -> {
            Toast.makeText(this, "Refund request sent!", Toast.LENGTH_SHORT).show();
            returnToMain();
        });
    }

    // Hàm quay về màn hình chính an toàn
    private void returnToMain() {
        Intent intent = new Intent(TicketActivity.this, MainActivity.class);
        // Xóa sạch lịch sử các trang đặt vé để không back lại được
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}