package com.example.ticketbookingcinema;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DetailActivity extends AppCompatActivity {

    // UI Components
    LinearLayout layoutAbout, layoutSessions;
    LinearLayout tabAbout, tabSessions;
    TextView tvTabAbout, tvTabSessions;
    View viewTabAbout, viewTabSessions;
    LinearLayout btnDateFilter;
    TextView tvDateFilter;

    // RecyclerView cho suất chiếu
    RecyclerView rvShowtimes;
    ShowtimeAdapter showtimeAdapter;
    List<Showtime> showtimeList;
    Showtime selectedShowtime = null;

    // Data
    Movie currentMovie;
    String selectedDate = "";
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Load ngôn ngữ
        loadLocale();
        setContentView(R.layout.activity_detail);

        db = FirebaseFirestore.getInstance();

        initViews();
        setDefaultDate(); // Mặc định lấy ngày hôm nay

        // 2. Nhận dữ liệu phim từ màn hình trước
        currentMovie = (Movie) getIntent().getSerializableExtra("object");
        fillMovieData(currentMovie);

        // Setup Tab (Mặc định chọn tab Sessions để khách thấy lịch ngay)
        setupTabs();

        // 3. Cấu hình RecyclerView
        rvShowtimes = findViewById(R.id.rvShowtimes);
        rvShowtimes.setLayoutManager(new LinearLayoutManager(this));
        showtimeList = new ArrayList<>();
        showtimeAdapter = new ShowtimeAdapter(showtimeList, showtime -> {
            selectedShowtime = showtime; // Lưu suất chiếu khách chọn
        });
        rvShowtimes.setAdapter(showtimeAdapter);

        // 4. Tải dữ liệu thật từ Firebase
        loadShowtimes(selectedDate);

        // 5. Sự kiện chọn ngày
        btnDateFilter.setOnClickListener(v -> showDatePicker());

        // 6. Nút Back
        findViewById(R.id.btnBackDetail).setOnClickListener(v -> finish());

        // 7. Nút Mua vé
        findViewById(R.id.btnSelectSession).setOnClickListener(v -> {
            if (selectedShowtime == null) {
                Toast.makeText(this, "Please select a session time!", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(DetailActivity.this, SeatSelectionActivity.class);
            // Truyền dữ liệu sang màn hình chọn ghế
            intent.putExtra("movieTitle", currentMovie != null ? currentMovie.getTitle() : "");
            intent.putExtra("cinema", selectedShowtime.getCinemaName());
            intent.putExtra("time", selectedShowtime.getTime());
            intent.putExtra("date", selectedShowtime.getDate());
            intent.putExtra("price", selectedShowtime.getPrice());
            startActivity(intent);
        });
    }

    // --- LOGIC TẢI SUẤT CHIẾU TỪ FIREBASE ---
    private void loadShowtimes(String date) {
        if (currentMovie == null) return;

        // DEBUG: In ra ID để kiểm tra nếu danh sách bị rỗng
        android.util.Log.e("CHECK_ID", "Đang tìm lịch cho phim ID: " + currentMovie.getId());
        android.util.Log.e("CHECK_ID", "Ngày đang chọn: " + date);

        db.collection("showtimes")
                .whereEqualTo("movieId", currentMovie.getId()) // Lọc theo ID phim
                .whereEqualTo("date", date)                   // Lọc theo Ngày (vd: 26/12)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        showtimeList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            Showtime st = doc.toObject(Showtime.class);
                            st.setId(doc.getId());
                            showtimeList.add(st);
                        }
                        showtimeAdapter.notifyDataSetChanged();

                        // Nếu không tìm thấy, thông báo nhẹ
                        if (showtimeList.isEmpty()) {
                            // Toast.makeText(this, "Chưa có lịch chiếu ngày " + date, Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "Lỗi tải dữ liệu!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, month1, dayOfMonth) -> {
                    // Định dạng ngày phải chuẩn: d/M (ví dụ: 26/12)
                    selectedDate = dayOfMonth + "/" + (month1 + 1);
                    tvDateFilter.setText(selectedDate);

                    // Gọi hàm tải lại dữ liệu khi chọn ngày mới
                    loadShowtimes(selectedDate);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void setDefaultDate() {
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int month = calendar.get(Calendar.MONTH) + 1;
        selectedDate = day + "/" + month;
        tvDateFilter.setText(selectedDate);
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
    }

    private void setupTabs() {
        // Mặc định chọn tab Sessions
        selectSessionsTab();

        tabAbout.setOnClickListener(v -> selectAboutTab());
        tabSessions.setOnClickListener(v -> selectSessionsTab());
    }

    private void selectAboutTab() {
        layoutAbout.setVisibility(View.VISIBLE);
        layoutSessions.setVisibility(View.GONE);
        int orange = ContextCompat.getColor(this, R.color.orange_main);
        int grey = ContextCompat.getColor(this, R.color.text_grey);
        int darkBg = ContextCompat.getColor(this, R.color.dark_bg);
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
        TextView tvDuration = findViewById(R.id.tvDetailDuration);

        tvTitle.setText(movie.getTitle());
        tvRating.setText(movie.getRating());
        tvDesc.setText(movie.getDescription());
        tvGenre.setText(movie.getCategory());
        tvDuration.setText(movie.getDuration());

        String picUrl = movie.getPicUrl();
        int resId = 0;
        if (picUrl != null && !picUrl.isEmpty()) {
            try {
                resId = getResources().getIdentifier(picUrl, "drawable", getPackageName());
            } catch (Exception e) { e.printStackTrace(); }
        }
        if (resId > 0) imgPoster.setImageResource(resId);
        else imgPoster.setImageResource(R.drawable.ic_launcher_background);
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        if (!language.equals("")) {
            setLocale(language);
        }
    }

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }
}