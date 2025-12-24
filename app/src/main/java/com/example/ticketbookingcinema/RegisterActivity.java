package com.example.ticketbookingcinema;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore; // Thêm thư viện Firestore

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtRePassword; // Thêm edtName
    Button btnRegister;
    TextView tvGoToLogin;

    FirebaseAuth mAuth;
    FirebaseFirestore db; // Khai báo Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // Khởi tạo Firebase
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance(); // Khởi tạo Database

        // Ánh xạ View
        edtName = findViewById(R.id.edtName); // Ánh xạ ô nhập tên
        edtEmail = findViewById(R.id.edtEmailReg);
        edtPassword = findViewById(R.id.edtPasswordReg);
        edtRePassword = findViewById(R.id.edtRePasswordReg);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        // Chuyển về màn hình đăng nhập
        tvGoToLogin.setOnClickListener(v -> finish());

        // Xử lý sự kiện nút Đăng ký
        btnRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();
            String rePass = edtRePassword.getText().toString().trim();

            // Kiểm tra dữ liệu đầu vào
            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(rePass)) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                Toast.makeText(this, "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                return;
            }

            // Gọi hàm đăng ký có truyền thêm Name
            registerUser(email, pass, name);
            System.out.println(email+ " "+ pass + "" + name);
        });
    }

    private void registerUser(String email, String password, String fullName) {
        // 1. Tạo tài khoản Authentication trước
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Lấy UID (User ID) vừa được tạo ra
                        String userId = mAuth.getCurrentUser().getUid();
                        System.out.println(userId);

                        // 2. Chuẩn bị dữ liệu để lưu vào Firestore
                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", fullName);
                        user.put("email", email);
                        user.put("role", "client"); // Mặc định là khách hàng

                        // 3. Lưu vào Collection "users" với Document ID là UID
                        db.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    Toast.makeText(RegisterActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();

                                    // Chuyển sang màn hình chính
                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(RegisterActivity.this, "Error saving user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        // Lỗi khi tạo Authentication (ví dụ: email đã tồn tại, mật khẩu yếu...)
                        Toast.makeText(RegisterActivity.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}