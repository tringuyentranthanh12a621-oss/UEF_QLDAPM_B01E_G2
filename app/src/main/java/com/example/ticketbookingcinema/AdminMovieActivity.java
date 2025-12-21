package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager; // Hoặc GridLayoutManager
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class AdminMovieActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    MovieAdapter adapter; // Tái sử dụng Adapter cũ cho nhanh
    ArrayList<Movie> movies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_movie);

        // Nút Back
        findViewById(R.id.btnBackAdminMovie).setOnClickListener(v -> finish());

        // Nút Thêm phim mới (+)
        FloatingActionButton fab = findViewById(R.id.fabAddMovie);
        fab.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMovieActivity.this, AddEditMovieActivity.class);
            startActivity(intent);
        });

        // Setup danh sách
        rvMovies = findViewById(R.id.rvAdminMovies);
        initData(); // Dữ liệu giả

        // Lưu ý: MovieAdapter cũ khi click sẽ mở DetailActivity của Client.
        // Để đơn giản, ta tạm dùng nó. Nếu muốn click vào để Sửa (Edit), ta cần sửa Adapter sau.
        adapter = new MovieAdapter(this, movies);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);
    }

    private void initData() {
        movies = new ArrayList<>();
        movies.add(new Movie("The Batman", "Action", "2h 55m", "8.3", R.drawable.ic_launcher_background));
        movies.add(new Movie("Uncharted", "Adventure", "1h 56m", "7.9", R.drawable.ic_launcher_background));
        // Thêm các phim khác...
    }
}