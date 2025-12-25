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

        // 1. Nhận dữ liệu
        Intent intent = getIntent();
        String movieTitle = intent.getStringExtra("movieTitle");
        String cinemaName = intent.getStringExtra("cinemaName");
        String date = intent.getStringExtra("date");
        String time = intent.getStringExtra("time");
        ArrayList<String> seats = intent.getStringArrayListExtra("seats");

        // 2. Ánh xạ View
        TextView tvTitle = findViewById(R.id.tvTicketMovieTitle);
        TextView tvCinema = findViewById(R.id.tvTicketCinema);
        TextView tvDate = findViewById(R.id.tvTicketDate);
        TextView tvSeats = findViewById(R.id.tvTicketSeats);
        ImageView imgTicketQR = findViewById(R.id.imgTicketQR);
        ImageView btnClose = findViewById(R.id.btnCloseTicket);
        Button btnHome = findViewById(R.id.btnHome);

        // 3. Hiển thị dữ liệu
        if (movieTitle != null) tvTitle.setText(movieTitle);
        if (cinemaName != null) tvCinema.setText(cinemaName);
        if (date != null && time != null) tvDate.setText(date + " • " + time);

        if (seats != null) {
            String seatsStr = seats.toString().replace("[", "").replace("]", "");
            tvSeats.setText(seatsStr);
        }

        // --- LOGIC HIỂN THỊ ẢNH PHIM ---
        if (movieTitle != null) {
            // Chuyển tên phim thành tên file ảnh (ví dụ: "The Batman" -> "the_batman")
            String imgName = movieTitle.toLowerCase().replace(" ", "_");

            // Tìm ID ảnh trong drawable
            int resId = getResources().getIdentifier(imgName, "drawable", getPackageName());

            if (resId > 0) {
                imgTicketQR.setImageResource(resId);
                imgTicketQR.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                // Nếu không tìm thấy ảnh phim, set ảnh mặc định
                imgTicketQR.setImageResource(R.drawable.ic_launcher_background);
            }
        }

        // 4. Xử lý nút bấm
        btnClose.setOnClickListener(v -> returnToMain());

        btnHome.setOnClickListener(v -> {
            // SỬA: Dùng getString() để hiện thông báo đa ngôn ngữ
            Toast.makeText(this, getString(R.string.ticket_saved), Toast.LENGTH_SHORT).show();
            returnToMain();
        });
    }

    private void returnToMain() {
        Intent intent = new Intent(TicketActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}