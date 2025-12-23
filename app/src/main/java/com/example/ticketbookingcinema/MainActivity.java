package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    MovieAdapter adapter;
    ArrayList<Movie> movies;

    // Khai báo Firestore
    FirebaseFirestore db;

    // Biến giao diện
    TextView tvLocation, tvLanguage;
    LinearLayout layoutLocation, layoutLanguage;
    ImageView btnSearch;
    EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // --- ÁNH XẠ VIEW ---
        rvMovies = findViewById(R.id.rvMovies);
        tvLocation = findViewById(R.id.tvLocation);
        tvLanguage = findViewById(R.id.tvLanguage);
        layoutLocation = findViewById(R.id.layoutLocation);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearchMovie);
        Button btnProfile = findViewById(R.id.btnProfile);

        // --- CẤU HÌNH RECYCLERVIEW ---
        movies = new ArrayList<>();
        adapter = new MovieAdapter(this, movies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setAdapter(adapter);

        // --- LẤY DỮ LIỆU TỪ FIREBASE ---
        getMoviesFromFirestore();

        // --- 1. XỬ LÝ NÚT PROFILE ---
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // --- 2. TÌM KIẾM PHIM ---
        btnSearch.setOnClickListener(v -> {
            if (edtSearch.getVisibility() == View.VISIBLE) {
                edtSearch.setVisibility(View.GONE);
            } else {
                edtSearch.setVisibility(View.VISIBLE);
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });

        // --- 3. ĐỔI ĐỊA ĐIỂM & NGÔN NGỮ ---
        layoutLocation.setOnClickListener(v -> showChangeLocationDialog());
        layoutLanguage.setOnClickListener(v -> showChangeLanguageDialog());
    }

    // --- HÀM LẤY DỮ LIỆU TỪ FIREBASE ---
    private void getMoviesFromFirestore() {
        db.collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        movies.clear(); // Xóa dữ liệu cũ (nếu có)
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Firestore tự động map JSON sang Object Movie
                            // Yêu cầu: Class Movie phải có Constructor rỗng và trùng tên trường
                            try {
                                Movie movie = document.toObject(Movie.class);
                                movies.add(movie);
                            } catch (Exception e) {
                                Log.e("FirestoreError", "Error converting document", e);
                            }
                        }
                        // Cập nhật giao diện sau khi tải xong
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(MainActivity.this, "Error getting movies: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Hàm lọc danh sách phim
    private void filter(String text) {
        ArrayList<Movie> filteredList = new ArrayList<>();
        for (Movie item : movies) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    // Dialog chọn địa điểm
    private void showChangeLocationDialog() {
        String[] cities = {"Nur-Sultan", "Almaty", "Ho Chi Minh", "Ha Noi", "Da Nang"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose City");
        builder.setItems(cities, (dialog, which) -> {
            tvLocation.setText(" " + cities[which]);
            Toast.makeText(this, "Location changed to " + cities[which], Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }

    // Dialog chọn ngôn ngữ
    private void showChangeLanguageDialog() {
        String[] languages = {"English", "Tiếng Việt", "Russian"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Language");
        builder.setItems(languages, (dialog, which) -> {
            String selected = languages[which];
            if(selected.equals("English")) tvLanguage.setText(" Eng");
            else if(selected.equals("Tiếng Việt")) tvLanguage.setText(" VN");
            else tvLanguage.setText(" Rus");
            Toast.makeText(this, "Language changed to " + selected, Toast.LENGTH_SHORT).show();
        });
        builder.show();
    }
}