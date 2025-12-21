package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class OrderConfirmationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Nhận dữ liệu
        String movieTitle = getIntent().getStringExtra("movieTitle");
        int totalPrice = getIntent().getIntExtra("totalPrice", 0);
        ArrayList<String> seats = getIntent().getStringArrayListExtra("seats");

        // Ánh xạ (Đảm bảo ID khớp với XML ở trên)
        TextView tvTitle = findViewById(R.id.tvOrderMovieTitle);
        TextView tvSeats = findViewById(R.id.tvOrderSeats);
        TextView tvTotal = findViewById(R.id.tvOrderTotal);
        Button btnPay = findViewById(R.id.btnFinalPay);

        // Hiển thị dữ liệu
        if(movieTitle != null) tvTitle.setText(movieTitle);
        tvTotal.setText("Total: " + totalPrice + " T");
        btnPay.setText("Pay • " + totalPrice + " T");

        if (seats != null) {
            tvSeats.setText("Seats: " + seats.toString().replace("[", "").replace("]", ""));
        }

        // Nút Back
        findViewById(R.id.btnBackConfirm).setOnClickListener(v -> finish());

        // Xử lý nút Pay -> Chuyển sang màn hình vé
        btnPay.setOnClickListener(v -> {
            Intent intent = new Intent(OrderConfirmationActivity.this, TicketActivity.class);
            intent.putExtra("movieTitle", movieTitle);
            intent.putExtra("seats", seats);
            startActivity(intent);
        });
    }
}