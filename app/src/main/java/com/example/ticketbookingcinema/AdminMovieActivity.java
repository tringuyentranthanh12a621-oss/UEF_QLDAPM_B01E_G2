package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;

public class AdminMovieActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    AdminMovieAdapter adapter; // SỬA: Dùng AdminMovieAdapter thay vì MovieAdapter
    ArrayList<Movie> movieList;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_movie);

        // Nút Back
        findViewById(R.id.btnBackAdminMovie).setOnClickListener(v -> finish());

        // Nút thêm phim mới (+)
        FloatingActionButton fabAdd = findViewById(R.id.fabAddMovie);
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(AdminMovieActivity.this, AddEditMovieActivity.class);
            startActivity(intent);
        });

        // Setup RecyclerView
        rvMovies = findViewById(R.id.rvAdminMovies);
        movieList = new ArrayList<>();

        // SỬA: Khởi tạo AdminMovieAdapter
        adapter = new AdminMovieAdapter(this, movieList);
        rvMovies.setLayoutManager(new LinearLayoutManager(this));
        rvMovies.setAdapter(adapter);

        db = FirebaseFirestore.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadMovies(); // Tải lại danh sách khi quay lại màn hình này
    }

    private void loadMovies() {
        db.collection("movies").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                movieList.clear();
                for (QueryDocumentSnapshot doc : task.getResult()) {
                    // Chuyển đổi dữ liệu JSON sang Object Movie
                    Movie m = doc.toObject(Movie.class);

                    // QUAN TRỌNG: Lấy ID của document và gán vào object Movie
                    // (Để sau này biết cần sửa/xóa document nào)
                    m.setId(doc.getId());

                    movieList.add(m);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}