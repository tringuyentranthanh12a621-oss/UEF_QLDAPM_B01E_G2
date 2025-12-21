package com.example.ticketbookingcinema;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

public class AdminUserActivity extends AppCompatActivity {
    RecyclerView rvUsers;
    UserAdapter adapter;
    ArrayList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);

        // Nút back
        findViewById(R.id.btnBackUser).setOnClickListener(v -> finish());

        rvUsers = findViewById(R.id.rvUsers);
        users = new ArrayList<>();
        // Mock data
        users.add(new User("Nguyen Van A", "0901234567"));
        users.add(new User("Tran Thi B", "0912345678"));
        users.add(new User("Le Van C", "0987654321"));

        adapter = new UserAdapter(users);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);
    }
}