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
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText edtName, edtEmail, edtPassword, edtRePassword;
    Button btnRegister;
    TextView tvGoToLogin;

    FirebaseAuth mAuth;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmailReg);
        edtPassword = findViewById(R.id.edtPasswordReg);
        edtRePassword = findViewById(R.id.edtRePasswordReg);
        btnRegister = findViewById(R.id.btnRegister);
        tvGoToLogin = findViewById(R.id.tvGoToLogin);

        tvGoToLogin.setOnClickListener(v -> finish());

        btnRegister.setOnClickListener(v -> {
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();
            String pass = edtPassword.getText().toString().trim();
            String rePass = edtRePassword.getText().toString().trim();

            if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                // --- CẬP NHẬT ---
                Toast.makeText(this, getString(R.string.fill_all_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!pass.equals(rePass)) {
                // --- CẬP NHẬT ---
                Toast.makeText(this, getString(R.string.password_mismatch), Toast.LENGTH_SHORT).show();
                return;
            }

            if (pass.length() < 6) {
                // --- CẬP NHẬT ---
                Toast.makeText(this, getString(R.string.password_length_error), Toast.LENGTH_SHORT).show();
                return;
            }

            registerUser(email, pass, name);
        });
    }

    private void registerUser(String email, String password, String fullName) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        String userId = mAuth.getCurrentUser().getUid();

                        Map<String, Object> user = new HashMap<>();
                        user.put("fullName", fullName);
                        user.put("email", email);
                        user.put("role", "client"); // Giữ nguyên "client" vì đây là dữ liệu hệ thống

                        db.collection("users").document(userId)
                                .set(user)
                                .addOnSuccessListener(aVoid -> {
                                    // --- CẬP NHẬT ---
                                    Toast.makeText(RegisterActivity.this, getString(R.string.account_created), Toast.LENGTH_SHORT).show();

                                    Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                })
                                .addOnFailureListener(e -> {
                                    // --- CẬP NHẬT ---
                                    Toast.makeText(RegisterActivity.this, getString(R.string.error_save_user) + e.getMessage(), Toast.LENGTH_SHORT).show();
                                });

                    } else {
                        // --- CẬP NHẬT ---
                        Toast.makeText(RegisterActivity.this, getString(R.string.reg_failed) + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}