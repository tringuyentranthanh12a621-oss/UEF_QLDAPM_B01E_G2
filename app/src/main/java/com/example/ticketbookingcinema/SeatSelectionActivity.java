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

    FirebaseFirestore db;

    int priceAdult = 2200;
    int priceChild = 1000;

    int totalPrice = 0;
    int totalTickets = 0;

    List<String> selectedSeats = new ArrayList<>();
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

        db = FirebaseFirestore.getInstance();

        Intent intent = getIntent();
        movieTitleStr = intent.getStringExtra("movieTitle");
        cinemaNameStr = intent.getStringExtra("cinema");
        dateStr = intent.getStringExtra("date");
        timeStr = intent.getStringExtra("time");

        gridLayoutSeats = findViewById(R.id.gridLayoutSeats);
        btnBuyTicket = findViewById(R.id.btnBuyTicket);
        tvMovieTitle = findViewById(R.id.tvMovieTitle);
        tvCinemaName = findViewById(R.id.tvCinemaName);
        ImageView btnBack = findViewById(R.id.btnBack);

        if (movieTitleStr != null) tvMovieTitle.setText(movieTitleStr);
        if (cinemaNameStr != null) tvCinemaName.setText(cinemaNameStr);

        // Cập nhật text ban đầu cho nút mua vé
        updateButton();

        btnBack.setOnClickListener(v -> finish());

        loadBookedSeatsFromFirestore();

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
                Toast.makeText(this, getString(R.string.please_select_seat), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @SuppressWarnings("unchecked")
    private void loadBookedSeatsFromFirestore() {
        db.collection("bookings")
                .whereEqualTo("movieTitle", movieTitleStr)
                .whereEqualTo("bookingDate", dateStr)
                .whereEqualTo("time", timeStr)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        bookedSeats.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            List<String> seatsInOrder = (List<String>) document.get("seats");
                            if (seatsInOrder != null) {
                                bookedSeats.addAll(seatsInOrder);
                            }
                        }
                        initSeats();
                    } else {
                        Toast.makeText(this, "Failed to load seats", Toast.LENGTH_SHORT).show();
                        initSeats();
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

                String seatName = "Row " + r + " Seat " + c;

                if (bookedSeats.contains(seatName)) {
                    seatView.setBackgroundResource(R.drawable.bg_seat_occupied);
                    seatView.setEnabled(false);
                } else {
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
            seat.setSelected(false);
            totalTickets--;

            if (seatPrices.containsKey(seatKey)) {
                totalPrice -= seatPrices.get(seatKey);
                seatPrices.remove(seatKey);
            }
            selectedSeats.remove(seatName);
            updateButton();

        } else {
            // Đã đổi Dialog sang Đa ngôn ngữ
            String[] options = {
                    getString(R.string.adult) + " (" + priceAdult + " ₸)",
                    getString(R.string.child) + " (" + priceChild + " ₸)"
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(getString(R.string.select_ticket_type)); // Lấy tiêu đề từ strings
            builder.setItems(options, (dialog, which) -> {
                seat.setSelected(true);
                totalTickets++;

                int priceToAdd = (which == 0) ? priceAdult : priceChild;
                totalPrice += priceToAdd;

                seatPrices.put(seatKey, priceToAdd);
                selectedSeats.add(seatName);
                updateButton();
            });
            builder.show();
        }
    }

    private void updateButton() {
        if (totalTickets == 0) {
            // "Mua vé • 0 ₸"
            btnBuyTicket.setText(getString(R.string.buy_tickets) + " • 0 ₸");
        } else {
            // "Mua 2 vé • 5000 ₸" (Ghép chuỗi đa ngôn ngữ)
            String text = getString(R.string.buy) + " " + totalTickets + " " + getString(R.string.tickets) + " • " + totalPrice + " ₸";
            btnBuyTicket.setText(text);
        }
    }
}