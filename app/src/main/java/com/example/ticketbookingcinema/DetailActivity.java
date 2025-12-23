package com.example.ticketbookingcinema;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat; // Thêm thư viện lấy màu an toàn

import java.util.Calendar;

public class DetailActivity extends AppCompatActivity {

    // Khai báo biến UI
    LinearLayout layoutAbout, layoutSessions;
    LinearLayout tabAbout, tabSessions;
    TextView tvTabAbout, tvTabSessions;
    View viewTabAbout, viewTabSessions;

    // --- BIẾN CHO PHẦN SESSION ---
    LinearLayout btnDateFilter;
    TextView tvDateFilter;
    LinearLayout itemSession1, itemSession2;

    // Biến lưu thông tin vé
    String selectedCinema = "";
    String selectedTime = "";
    String selectedDate = ""; // Sẽ lấy ngày hiện tại mặc định

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        initViews();
        setDefaultDate(); // Set ngày mặc định là hôm nay

        // Nhận object Movie từ Intent
        Movie movie = (Movie) getIntent().getSerializableExtra("object");
        fillMovieData(movie);
        setupTabs();

        // --- XỬ LÝ CHỌN NGÀY ---
        btnDateFilter.setOnClickListener(v -> showDatePicker());

        // --- XỬ LÝ CHỌN SUẤT CHIẾU (Hardcode giả lập) ---
        itemSession1.setOnClickListener(v -> {
            itemSession1.setSelected(true);
            itemSession2.setSelected(false); // Bỏ chọn cái kia

            // Đổi background để người dùng biết đang chọn (Cần file drawable selector)
            itemSession1.setBackgroundResource(R.drawable.bg_session_item_selector);
            itemSession2.setBackgroundResource(R.drawable.bg_input_field); // Reset cái kia về mặc định

            selectedTime = "14:40";
            selectedCinema = "Eurasia Cinema7";
        });

        itemSession2.setOnClickListener(v -> {
            itemSession2.setSelected(true);
            itemSession1.setSelected(false);

            itemSession2.setBackgroundResource(R.drawable.bg_session_item_selector);
            itemSession1.setBackgroundResource(R.drawable.bg_input_field);

            selectedTime = "15:10";
            selectedCinema = "Kinopark 8 IMAX";
        });

        // Nút Back
        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

        // Nút Select Session (Mua vé)
        findViewById(R.id.btnSelectSession).setOnClickListener(v -> {
            if (selectedTime.isEmpty()) {
                Toast.makeText(this, "Please select a session time!", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(DetailActivity.this, SeatSelectionActivity.class);
            intent.putExtra("movieTitle", movie != null ? movie.getTitle() : "");
            intent.putExtra("cinema", selectedCinema);
            intent.putExtra("time", selectedTime);
            intent.putExtra("date", selectedDate);
            startActivity(intent);
        });
    }

    private void initViews() {
        layoutAbout = findViewById(R.id.layoutAbout);
        layoutSessions = findViewById(R.id.layoutSessions);
        tabAbout = findViewById(R.id.tabAbout);
        tabSessions = findViewById(R.id.tabSessions);
        tvTabAbout = findViewById(R.id.tvTabAbout);
        tvTabSessions = findViewById(R.id.tvTabSessions);
        viewTabAbout = findViewById(R.id.viewTabAbout);
        viewTabSessions = findViewById(R.id.viewTabSessions);

        btnDateFilter = findViewById(R.id.btnDateFilter);
        tvDateFilter = findViewById(R.id.tvDateFilter);
        itemSession1 = findViewById(R.id.itemSession1);
        itemSession2 = findViewById(R.id.itemSession2);
    }

    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        selectedDate = day + "/" + month;
        tvDateFilter.setText(selectedDate);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    selectedDate = dayOfMonth + "/" + (month1 + 1) + "/" + year1;
                    tvDateFilter.setText(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void setupTabs() {
        selectAboutTab(); // Mặc định chọn tab About
        tabAbout.setOnClickListener(v -> selectAboutTab());
        tabSessions.setOnClickListener(v -> selectSessionsTab());
    }

    private void selectAboutTab() {
        layoutAbout.setVisibility(View.VISIBLE);
        layoutSessions.setVisibility(View.GONE);

        int orange = ContextCompat.getColor(this, R.color.orange_main);
        int grey = ContextCompat.getColor(this, R.color.text_grey);
        int darkBg = ContextCompat.getColor(this, R.color.dark_bg); // Hoặc màu nền button cũ

        tvTabAbout.setTextColor(orange);
        viewTabAbout.setBackgroundColor(orange);

        tvTabSessions.setTextColor(grey);
        viewTabSessions.setBackgroundColor(darkBg);
    }

    private void selectSessionsTab() {
        layoutAbout.setVisibility(View.GONE);
        layoutSessions.setVisibility(View.VISIBLE);

        int orange = ContextCompat.getColor(this, R.color.orange_main);
        int grey = ContextCompat.getColor(this, R.color.text_grey);
        int darkBg = ContextCompat.getColor(this, R.color.dark_bg);

        tvTabSessions.setTextColor(orange);
        viewTabSessions.setBackgroundColor(orange);

        tvTabAbout.setTextColor(grey);
        viewTabAbout.setBackgroundColor(darkBg);
    }

    private void fillMovieData(Movie movie) {
        if (movie == null) return;

        TextView tvTitle = findViewById(R.id.tvDetailTitle);
        TextView tvRating = findViewById(R.id.tvDetailRating);
        TextView tvDesc = findViewById(R.id.tvDetailDescription);
        TextView tvGenre = findViewById(R.id.tvDetailGenre);
        ImageView imgPoster = findViewById(R.id.imgDetailPoster);

        tvTitle.setText(movie.getTitle());
        tvRating.setText(movie.getRating());
        tvDesc.setText(movie.getDescription());
        tvGenre.setText("Genre: " + movie.getCategory());

        // --- SỬA PHẦN LOAD ẢNH ---
        // Lấy tên ảnh từ object (String) -> Tìm ID drawable -> Set ảnh
        String picUrl = movie.getPicUrl();
        int drawableResourceId = getResources().getIdentifier(picUrl, "drawable", getPackageName());

        if (drawableResourceId > 0) {
            imgPoster.setImageResource(drawableResourceId);
        } else {
            imgPoster.setImageResource(R.drawable.ic_launcher_background); // Ảnh lỗi mặc định
        }
    }
}