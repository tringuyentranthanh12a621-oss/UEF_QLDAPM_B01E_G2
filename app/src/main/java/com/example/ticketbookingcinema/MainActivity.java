package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    MovieAdapter adapter;
    ArrayList<Movie> movies;

    // Khai báo các biến giao diện mới
    TextView tvLocation, tvLanguage;
    LinearLayout layoutLocation, layoutLanguage;
    ImageView btnSearch;
    EditText edtSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- ÁNH XẠ VIEW ---
        rvMovies = findViewById(R.id.rvMovies);
        tvLocation = findViewById(R.id.tvLocation);
        tvLanguage = findViewById(R.id.tvLanguage);
        layoutLocation = findViewById(R.id.layoutLocation);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearchMovie);
        Button btnProfile = findViewById(R.id.btnProfile);

        // --- KHỞI TẠO DỮ LIỆU & ADAPTER ---
        initData();
        adapter = new MovieAdapter(this, movies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setAdapter(adapter);

        // --- 1. XỬ LÝ NÚT PROFILE ---
        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

        // --- 2. TÌM KIẾM PHIM ---
        // Bấm kính lúp -> Ẩn/Hiện ô nhập
        btnSearch.setOnClickListener(v -> {
            if (edtSearch.getVisibility() == View.VISIBLE) {
                edtSearch.setVisibility(View.GONE);
            } else {
                edtSearch.setVisibility(View.VISIBLE);
            }
        });

        // Lắng nghe khi người dùng gõ chữ
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString()); // Gọi hàm lọc
            }
        });

        // --- 3. ĐỔI ĐỊA ĐIỂM ---
        layoutLocation.setOnClickListener(v -> showChangeLocationDialog());

        // --- 4. ĐỔI NGÔN NGỮ ---
        layoutLanguage.setOnClickListener(v -> showChangeLanguageDialog());
    }

    // Hàm lọc danh sách phim
    private void filter(String text) {
        ArrayList<Movie> filteredList = new ArrayList<>();
        for (Movie item : movies) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        // Gọi hàm cập nhật bên Adapter
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

    private void initData() {
        movies = new ArrayList<>();
        // LƯU Ý: Đảm bảo bạn đã copy ảnh (the_batman.jpg, etc.) vào res/drawable
        // Nếu chưa có ảnh, hãy đổi tạm thành R.drawable.ic_launcher_background để chạy thử
        movies.add(new Movie("The Batman", "Action", "2h 55m", "8.3", R.drawable.the_batman));
        movies.add(new Movie("Uncharted", "Adventure", "1h 56m", "7.9", R.drawable.uncharted));
        movies.add(new Movie("Spider-Man", "Action", "2h 28m", "8.1", R.drawable.spider_man));
        movies.add(new Movie("Turning Red", "Comedy", "1h 40m", "7.1", R.drawable.turning_red));
    }
}