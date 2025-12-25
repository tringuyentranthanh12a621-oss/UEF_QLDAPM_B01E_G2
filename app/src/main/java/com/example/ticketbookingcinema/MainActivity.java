package com.example.ticketbookingcinema;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvMovies;
    MovieAdapter adapter;
    ArrayList<Movie> movies;
    FirebaseFirestore db;

    TextView tvLocation, tvLanguage;
    LinearLayout layoutLocation, layoutLanguage;
    ImageView btnSearch;
    EditText edtSearch;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Load ngôn ngữ trước khi hiện giao diện
        loadLocale();

        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        // Ánh xạ View
        rvMovies = findViewById(R.id.rvMovies);
        tvLocation = findViewById(R.id.tvLocation);
        tvLanguage = findViewById(R.id.tvLanguage);
        layoutLocation = findViewById(R.id.layoutLocation);
        layoutLanguage = findViewById(R.id.layoutLanguage);
        btnSearch = findViewById(R.id.btnSearch);
        edtSearch = findViewById(R.id.edtSearchMovie);
        Button btnProfile = findViewById(R.id.btnProfile);
        progressBar = findViewById(R.id.progressBar);

        // 2. Cập nhật chữ Eng/Vie/Rus
        updateLanguageDisplay();

        movies = new ArrayList<>();
        adapter = new MovieAdapter(this, movies);
        rvMovies.setLayoutManager(new GridLayoutManager(this, 2));
        rvMovies.setAdapter(adapter);

        // 3. Lấy dữ liệu phim
        getMoviesFromFirestore();

        if (btnProfile != null) {
            btnProfile.setOnClickListener(v -> {
                Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(intent);
            });
        }

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

        layoutLocation.setOnClickListener(v -> showChangeLocationDialog());
        layoutLanguage.setOnClickListener(v -> showChangeLanguageDialog());
    }

    private void getMoviesFromFirestore() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);

        db.collection("movies")
                .get()
                .addOnCompleteListener(task -> {
                    if (progressBar != null) progressBar.setVisibility(View.GONE);

                    if (task.isSuccessful()) {
                        movies.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Movie movie = document.toObject(Movie.class);

                                // --- [QUAN TRỌNG NHẤT] ---
                                // Dòng này sửa lỗi ID bị null.
                                // Nó lấy ID document (ví dụ: dV9d...) gán vào object Movie
                                movie.setId(document.getId());
                                // -------------------------

                                movies.add(movie);
                            } catch (Exception e) {
                                Log.e("FirestoreError", "Lỗi convert: " + e.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (movies.isEmpty()) {
                            Toast.makeText(MainActivity.this, "Không tìm thấy phim nào!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(MainActivity.this, "Lỗi kết nối: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void filter(String text) {
        ArrayList<Movie> filteredList = new ArrayList<>();
        for (Movie item : movies) {
            if (item.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }

    private void showChangeLocationDialog() {
        String[] cities = {"Nur-Sultan", "Almaty", "Ho Chi Minh", "Ha Noi", "Da Nang"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose City");
        builder.setItems(cities, (dialog, which) -> {
            tvLocation.setText(" " + cities[which]);
        });
        builder.show();
    }

    private void showChangeLanguageDialog() {
        final String[] listItems = {"English", "Tiếng Việt", "Russian"};
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("Choose Language");
        builder.setSingleChoiceItems(listItems, -1, (dialog, which) -> {
            if (which == 0) {
                setLocale("en");
                restartActivity();
            } else if (which == 1) {
                setLocale("vi");
                restartActivity();
            } else if (which == 2) {
                setLocale("ru");
                restartActivity();
            }
            dialog.dismiss();
        });
        AlertDialog mDialog = builder.create();
        mDialog.show();
    }

    private void restartActivity() {
        Intent intent = new Intent(MainActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    // --- CÁC HÀM XỬ LÝ NGÔN NGỮ ---

    private void setLocale(String langCode) {
        Locale locale = new Locale(langCode);
        Locale.setDefault(locale);
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences.Editor editor = getSharedPreferences("Settings", MODE_PRIVATE).edit();
        editor.putString("My_Lang", langCode);
        editor.apply();
    }

    public void loadLocale() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String language = prefs.getString("My_Lang", "");
        if (!language.equals("")) {
            setLocale(language);
        }
    }

    private void updateLanguageDisplay() {
        SharedPreferences prefs = getSharedPreferences("Settings", MODE_PRIVATE);
        String langCode = prefs.getString("My_Lang", "en");

        if (langCode.equals("vi")) {
            tvLanguage.setText(" Vie");
        } else if (langCode.equals("ru")) {
            tvLanguage.setText(" Rus");
        } else {
            tvLanguage.setText(" Eng");
        }
    }
}