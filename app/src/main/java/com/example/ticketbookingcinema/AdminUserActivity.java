package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

public class AdminUserActivity extends AppCompatActivity {
    RecyclerView rvUsers;
    UserAdapter adapter;
    ArrayList<User> users;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        // Nút back
        findViewById(R.id.btnBackUser).setOnClickListener(v -> finish());

        rvUsers = findViewById(R.id.rvUsers);
        users = new ArrayList<>();

        // Cấu hình Adapter
        adapter = new UserAdapter(users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);

        // Khởi tạo Firestore và lấy dữ liệu
        db = FirebaseFirestore.getInstance();
        loadUsersFromFirestore();
    }

    private void loadUsersFromFirestore() {
        db.collection("users")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        users.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Tự động map JSON sang object User
                            try {
                                User user = document.toObject(User.class);
                                users.add(user);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged(); // Cập nhật giao diện
                    } else {
                        Toast.makeText(this, "Error getting users", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}