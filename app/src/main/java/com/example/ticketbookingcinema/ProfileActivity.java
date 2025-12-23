package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager; // Import mới
import androidx.recyclerview.widget.RecyclerView; // Import mới

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot; // Import mới

import java.util.ArrayList; // Import mới
import java.util.List; // Import mới

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseFirestore db;
    TextView tvName, tvEmail;

    // --- THÊM BIẾN CHO RECYCLERVIEW ---
    RecyclerView rvBookingHistory;
    BookingAdapter bookingAdapter;
    List<Booking> bookingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);

        // --- ÁNH XẠ RECYCLERVIEW ---
        rvBookingHistory = findViewById(R.id.rvBookingHistory);

        // Setup RecyclerView
        bookingList = new ArrayList<>();
        bookingAdapter = new BookingAdapter(bookingList);
        rvBookingHistory.setLayoutManager(new LinearLayoutManager(this));
        rvBookingHistory.setAdapter(bookingAdapter);

        ImageView btnBack = findViewById(R.id.btnBackProfile);
        ImageView btnLogout = findViewById(R.id.btnLogout);
        Button btnAddCard = findViewById(R.id.btnAddCard);
        View cardVisa = findViewById(R.id.cardVisa);

        loadUserProfile();
        loadBookingHistory(); // --- GỌI HÀM TẢI LỊCH SỬ ---

        btnBack.setOnClickListener(v -> finish());

        btnLogout.setOnClickListener(v -> {
            mAuth.signOut();
            Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });

        btnAddCard.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, AddCardActivity.class);
            startActivity(intent);
        });

        if (cardVisa != null) {
            cardVisa.setOnClickListener(v -> {
                Intent intent = new Intent(ProfileActivity.this, CardDetailActivity.class);
                startActivity(intent);
            });
        }
    }

    private void loadUserProfile() {
        // ... (Giữ nguyên code cũ của bạn) ...
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("users").document(userId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String name = documentSnapshot.getString("fullName");
                            String email = documentSnapshot.getString("email");
                            if (tvName != null) tvName.setText(name);
                            if (tvEmail != null) tvEmail.setText(email);
                        }
                    });
        }
    }

    // --- HÀM MỚI: TẢI LỊCH SỬ ĐẶT VÉ ---
    private void loadBookingHistory() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();

            // Tìm trong collection "bookings" những vé có userId trùng với người đang đăng nhập
            db.collection("bookings")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            bookingList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Chuyển đổi dữ liệu về Object Booking
                                Booking booking = document.toObject(Booking.class);
                                bookingList.add(booking);
                            }
                            // Cập nhật giao diện
                            bookingAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(this, "Failed to load history", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}