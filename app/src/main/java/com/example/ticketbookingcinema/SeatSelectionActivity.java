package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SeatSelectionActivity extends AppCompatActivity {

    GridLayout gridLayoutSeats;
    Button btnBuyTicket;
    TextView tvMovieTitle, tvCinemaName;

    // Firestore
    FirebaseFirestore db;

    // Cấu hình giá
    int priceAdult = 2200;
    int priceChild = 1000;

    // Biến tổng hợp
    int totalPrice = 0;
    int totalTickets = 0;

    // Danh sách ghế đang chọn hiện tại
    List<String> selectedSeats = new ArrayList<>();

    // Danh sách ghế ĐÃ BỊ ĐẶT (lấy từ Firebase)
    List<String> bookedSeats = new ArrayList<>();

    Map<String, Integer> seatPrices = new HashMap<>();

    String movieTitleStr = "";
    String cinemaNameStr = "";
    String dateStr = "";
    String timeStr = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // 1. Nhận dữ liệu từ Intent
        Intent intent = getIntent();
        movieTitleStr = intent.getStringExtra("movieTitle");
        cinemaNameStr = intent.getStringExtra("cinema");
        dateStr = intent.getStringExtra("date"); // Ngày chiếu
        timeStr = intent.getStringExtra("time"); // Giờ chiếu

        // 2. Ánh xạ View
        gridLayoutSeats = findViewById(R.id.gridLayoutSeats);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvCinemaName = findViewById(R.id.tvCinemaName);
        ImageView btnBack = findViewById(R.id.btnBack);

        // 3. Hiển thị thông tin Header
        if (movieTitleStr != null) tvMovieTitle.setText(movieTitleStr);
        if (cinemaNameStr != null) tvCinemaName.setText(cinemaNameStr);

        // 4. Xử lý logic
        btnBack.setOnClickListener(v -> finish());

        // Thay vì gọi initSeats() ngay, ta gọi hàm tải dữ liệu từ Firebase trước
        loadBookedSeatsFromFirestore();

        // 5. Sự kiện nút Mua vé
        btnBuyTicket.setOnClickListener(v -> {
            if (totalTickets > 0) {
                Intent orderIntent = new Intent(SeatSelectionActivity.this, OrderConfirmationActivity.class);
                orderIntent.putExtra("movieTitle", movieTitleStr);
                orderIntent.putExtra("cinemaName", cinemaNameStr);
                orderIntent.putExtra("date", dateStr);
                orderIntent.putExtra("time", timeStr);
                orderIntent.putExtra("totalPrice", totalPrice);
                orderIntent.putStringArrayListExtra("seats", (ArrayList<String>) selectedSeats);
                startActivity(orderIntent);
            } else {
                Toast.makeText(this, "Please select at least one seat", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- HÀM MỚI: Tải danh sách ghế đã đặt ---
    private void loadBookedSeatsFromFirestore() {
        // Query: Tìm tất cả đơn hàng trùng Tên Phim + Ngày + Giờ
        db.collection("bookings")
                .whereEqualTo("movieTitle", movieTitleStr)
                .whereEqualTo("bookingDate", dateStr) // Lưu ý: Tên trường phải khớp với lúc lưu (step sau)
                .whereEqualTo("time", timeStr)       // Lưu ý: Tên trường phải khớp
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookedSeats.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Lấy mảng ghế "seats" từ mỗi đơn hàng
                            List<String> seatsInOrder = (List<String>) document.get("seats");
                            if (seatsInOrder != null) {
                                bookedSeats.addAll(seatsInOrder);
                            }
                        }
                        // Sau khi có danh sách ghế đã đặt -> Mới vẽ ghế
                        initSeats();
                    } else {
                        Toast.makeText(this, "Failed to load seats", Toast.LENGTH_SHORT).show();
                        initSeats(); // Vẫn vẽ ghế dù lỗi (coi như trống hết)
                    }
                });
    }

    private void initSeats() {
        int rows = 8;
        int cols = 11;

        gridLayoutSeats.removeAllViews();
        gridLayoutSeats.setColumnCount(cols);
        gridLayoutSeats.setRowCount(rows);

        int seatSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics());
        int margin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, getResources().getDisplayMetrics());

        for (int r = 1; r <= rows; r++) {
            for (int c = 1; c <= cols; c++) {
                View seatView = new View(this);
                GridLayout.LayoutParams params = new GridLayout.LayoutParams();
                params.width = seatSize;
                params.height = seatSize;
                params.setMargins(margin, margin, margin, margin);
                seatView.setLayoutParams(params);

                // Tạo định danh ghế (Ví dụ: "Row 1 Seat 5") - PHẢI KHỚP FORMAT LÚC LƯU
                String seatName = "Row " + r + " Seat " + c;

                // --- KIỂM TRA TRẠNG THÁI ---
                if (bookedSeats.contains(seatName)) {
                    // GHẾ ĐÃ CÓ NGƯỜI ĐẶT (Occupied)
                    seatView.setBackgroundResource(R.drawable.bg_seat_occupied); // File hình tròn xám sáng có dấu x
                    seatView.setEnabled(false); // Không cho bấm
                } else {
                    // GHẾ TRỐNG (Available)
                    seatView.setBackgroundResource(R.drawable.bg_seat_selector);
                    seatView.setSelected(false);

                    final int currentRow = r;
                    final int currentCol = c;
                    seatView.setOnClickListener(v -> handleSeatClick(seatView, currentRow, currentCol));
                }

                gridLayoutSeats.addView(seatView);
            }
        }
    }

    private void handleSeatClick(View seat, int row, int col) {
        String seatKey = row + "-" + col;
        String seatName = "Row " + row + " Seat " + col;

        if (seat.isSelected()) {
            // Bỏ chọn
            seat.setSelected(false);
            totalTickets--;

            if (seatPrices.containsKey(seatKey)) {
                totalPrice -= seatPrices.get(seatKey);
                seatPrices.remove(seatKey);
            }
            selectedSeats.remove(seatName);
            updateButton();

        } else {
            // Chọn mới -> Hỏi loại vé
            String[] options = {"Adult (" + priceAdult + " ₸)", "Child (" + priceChild + " ₸)"};
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Select Ticket Type");
            builder.setItems(options, (dialog, which) -> {
                seat.setSelected(true);
                totalTickets++;

                int priceToAdd = (which == 0) ? priceAdult : priceChild;
                totalPrice += priceToAdd;

                seatPrices.put(seatKey, priceToAdd);
                selectedSeats.add(seatName); // Lưu tên ghế
                updateButton();
            });
            builder.show();
        }
    }

    private void updateButton() {
        if (totalTickets == 0) {
            btnBuyTicket.setText("Buy tickets • 0 ₸");
        } else {
            btnBuyTicket.setText("Buy " + totalTickets + " tickets • " + totalPrice + " ₸");
        }
    }
}