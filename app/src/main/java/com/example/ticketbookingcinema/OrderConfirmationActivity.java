package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class OrderConfirmationActivity extends AppCompatActivity {

    FirebaseFirestore db;
    FirebaseAuth mAuth;

    // Biến dữ liệu
    String movieTitle, cinemaName, date, time;
    int totalPrice;
    ArrayList<String> seats;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Khởi tạo Firebase
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        // 1. Nhận dữ liệu từ SeatSelectionActivity
        Intent intent = getIntent();
        movieTitle = intent.getStringExtra("movieTitle");
        cinemaName = intent.getStringExtra("cinemaName");
        date = intent.getStringExtra("date");
        time = intent.getStringExtra("time");
        totalPrice = intent.getIntExtra("totalPrice", 0);
        seats = intent.getStringArrayListExtra("seats");

        // 2. Ánh xạ View
        TextView tvTitle = findViewById(R.id.tvOrderMovieTitle);
        TextView tvCinema = findViewById(R.id.tvOrderCinema); // Cần thêm ID này trong XML nếu muốn hiện
        TextView tvDate = findViewById(R.id.tvOrderDate);     // Cần thêm ID này trong XML nếu muốn hiện
        TextView tvSeats = findViewById(R.id.tvOrderSeats);
        TextView tvTotal = findViewById(R.id.tvOrderTotal);
        Button btnPay = findViewById(R.id.btnFinalPay);

        // 3. Hiển thị dữ liệu
        if(movieTitle != null) tvTitle.setText(movieTitle);
        if(cinemaName != null && tvCinema != null) tvCinema.setText(cinemaName);
        if(date != null && time != null && tvDate != null) tvDate.setText(date + " • " + time);

        tvTotal.setText("Total: " + totalPrice + " ₸");
        btnPay.setText("Pay • " + totalPrice + " ₸");

        if (seats != null) {
            tvSeats.setText("Seats: " + seats.toString().replace("[", "").replace("]", ""));
        }

        // Nút Back
        findViewById(R.id.btnBackConfirm).setOnClickListener(v -> finish());

        // 4. Xử lý nút Pay -> Lưu vào Firestore
        btnPay.setOnClickListener(v -> saveBookingToFirestore());
    }

    private void saveBookingToFirestore() {
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User not logged in!", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = mAuth.getCurrentUser().getUid();

        // Tạo Map dữ liệu để lưu
        Map<String, Object> booking = new HashMap<>();
        booking.put("userId", userId);
        booking.put("movieTitle", movieTitle);
        booking.put("cinemaName", cinemaName);
        booking.put("bookingDate", date); // Quan trọng: Khớp tên trường với logic check ghế
        booking.put("time", time);        // Quan trọng: Khớp tên trường với logic check ghế
        booking.put("seats", seats);      // Lưu mảng ghế
        booking.put("totalPrice", totalPrice);
        booking.put("timestamp", new Date()); // Thời gian đặt vé thực tế

        // Lưu vào Collection "bookings"
        db.collection("bookings")
                .add(booking)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(OrderConfirmationActivity.this, "Booking Successful!", Toast.LENGTH_SHORT).show();

                    // Chuyển sang màn hình Vé (TicketActivity)
                    Intent intent = new Intent(OrderConfirmationActivity.this, TicketActivity.class);
                    // Truyền dữ liệu sang để hiển thị vé
                    intent.putExtra("movieTitle", movieTitle);
                    intent.putExtra("cinemaName", cinemaName);
                    intent.putExtra("date", date);
                    intent.putExtra("time", time);
                    intent.putExtra("seats", seats);

                    // Xóa back stack để user không back lại màn hình thanh toán được
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(OrderConfirmationActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}