package com.example.ticketbookingcinema;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatSelectionActivity extends AppCompatActivity {

    GridLayout gridLayoutSeats;
    Button btnBuyTicket;
    TextView tvMovieTitle;

    // Cấu hình giá
    int priceAdult = 2200;
    int priceChild = 1000;

    int totalPrice = 0;
    int totalTickets = 0;

    // Danh sách ghế đã chọn
    List<String> selectedSeats = new ArrayList<>();
    // Lưu giá tiền của từng ghế để khi bỏ chọn thì trừ đúng số tiền
    Map<String, Integer> seatPrices = new HashMap<>();

    String movieTitleStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Nhận tên phim từ Intent
        movieTitleStr = getIntent().getStringExtra("movieTitle");

        // Ánh xạ View
        gridLayoutSeats = findViewById(R.id.gridLayoutSeats);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);

        // Hiển thị tên phim
        if (movieTitleStr != null) {
            tvMovieTitle.setText(movieTitleStr);
        }

        // Nút Back
        findViewById(R.id.btnBack).setOnClickListener(v -> finish());

        // Khởi tạo ghế
        initSeats();

        // Sự kiện nút Mua vé
        btnBuyTicket.setOnClickListener(v -> {
            if (totalTickets > 0) {
                Intent intent = new Intent(SeatSelectionActivity.this, OrderConfirmationActivity.class);
                intent.putExtra("movieTitle", movieTitleStr);
                intent.putExtra("totalPrice", totalPrice);
                intent.putStringArrayListExtra("seats", (ArrayList<String>) selectedSeats);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initSeats() {
        // --- KHAI BÁO SỐ HÀNG VÀ CỘT ---
        int rows = 8;
        int cols = 8;
        // -------------------------------

        // Kích thước mỗi ghế (nhỏ hơn một chút để vừa giao diện)
        int seatSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                TextView seatView = new TextView(this);

                // Cấu hình LayoutParams cho ô lưới
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = seatSize;
                params.height = seatSize;
                params.setMargins(margin, margin, margin, margin);
                seatView.setLayoutParams(params);

                seatView.setGravity(Gravity.CENTER);
                seatView.setTextSize(10);

                // Set trạng thái ngẫu nhiên (Giả lập Occupied)
                boolean isOccupied = Math.random() < 0.2; // 20% ghế bị chiếm

                if (isOccupied) {
                    // Ghế đã có người ngồi
                    seatView.setBackgroundResource(R.drawable.bg_seat_occupied); // File hình tròn xám bạn đã tạo
                    seatView.setText("x"); // Dấu x nhỏ
                    seatView.setTextColor(Color.LTGRAY);
                    seatView.setEnabled(false); // Không cho click
                    seatView.setTag("OCCUPIED");
                } else {
                    // Ghế trống (Available)
                    seatView.setBackgroundResource(R.drawable.bg_seat_state); // File selector hình tròn đổi màu
                    seatView.setText(""); // Để trống cho đẹp (giống dấu chấm)
                    seatView.setSelected(false);
                    seatView.setTag("AVAILABLE");

                    final int row = r;
                    final int col = c;
                    // Bắt sự kiện click
                    seatView.setOnClickListener(v -> handleSeatClick(seatView, row, col));
                }

                gridLayoutSeats.addView(seatView);
            }
        }
    }

    private void handleSeatClick(TextView seat, int row, int col) {
        String seatKey = row + "-" + col; // Key để lưu giá tiền ghế này

        if (seat.isSelected()) {
            // --- TRƯỜNG HỢP: ĐANG CHỌN -> BỎ CHỌN ---
            seat.setSelected(false);
            totalTickets--;

            // Trừ tiền đúng theo giá vé lúc chọn ghế này
            if (seatPrices.containsKey(seatKey)) {
                totalPrice -= seatPrices.get(seatKey);
                seatPrices.remove(seatKey);
            }

            selectedSeats.remove("Row " + row + " Seat " + col);
            updateButton();
        } else {
            // --- TRƯỜNG HỢP: CHƯA CHỌN -> CHỌN MỚI ---
            // Hiện Dialog hỏi loại vé (Adult / Child)
            String[] options = {"Adult (" + priceAdult + " T)", "Child (" + priceChild + " T)"};

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Ticket Type");
            builder.setItems(options, (dialog, which) -> {
                seat.setSelected(true); // Đổi màu thành Cam
                totalTickets++;

                int priceToAdd = 0;
                if (which == 0) {
                    priceToAdd = priceAdult;
                } else {
                    priceToAdd = priceChild;
                }

                totalPrice += priceToAdd;
                seatPrices.put(seatKey, priceToAdd); // Lưu giá vé của ghế này

                selectedSeats.add("Row " + row + " Seat " + col);
                updateButton();
            });
            builder.show();
        }
    }

    private void updateButton() {
        if (totalTickets == 0) {
            btnBuyTicket.setText("Buy tickets • 0 T");
        } else {
            btnBuyTicket.setText("Buy " + totalTickets + " tickets • " + totalPrice + " T");
        }
    }
}