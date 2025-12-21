package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    MovieAdapter adapter;
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- 1. XỬ LÝ NÚT PROFILE ---
        // Tìm nút Profile theo ID (Đảm bảo trong activity_main.xml bạn đã đặt id là @+id/btnProfile)
        Button btnProfile = findViewById(R.id.btnProfile);

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                // Chuyển sang màn hình ProfileActivity
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // --- 2. HIỂN THỊ DANH SÁCH PHIM ---
        rvMovies = findViewById(R.id.rvMovies);
        initData();

        adapter = new MovieAdapter(this, movies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2)); // Grid 2 cột
        rvMovies.setAdapter(adapter);
    }

    private void initData() {
        movies = new ArrayList<>();
        // Dữ liệu giả (Mock Data)
        movies.add(new Movie("The Batman", "Action", "2h 55m", "8.3", R.drawable.ic_launcher_background));
        movies.add(new Movie("Uncharted", "Adventure", "1h 56m", "7.9", R.drawable.ic_launcher_background));
        movies.add(new Movie("Spider-Man", "Action", "2h 28m", "8.1", R.drawable.ic_launcher_background));
        movies.add(new Movie("Turning Red", "Comedy", "1h 40m", "7.1", R.drawable.ic_launcher_background));
        // Bạn có thể thêm nhiều phim hơn để test khả năng cuộn trang
    }
}