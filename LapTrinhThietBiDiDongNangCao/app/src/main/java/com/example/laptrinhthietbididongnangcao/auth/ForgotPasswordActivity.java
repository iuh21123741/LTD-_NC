package com.example.laptrinhthietbididongnangcao.auth;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.laptrinhthietbididongnangcao.R;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText etContact;
    private Button btnSendResetLink;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Khởi tạo Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ các view
        etContact = findViewById(R.id.et_contact);
        btnSendResetLink = findViewById(R.id.btn_send_reset_link);

        // Xử lý sự kiện khi người dùng nhấn nút gửi liên kết reset mật khẩu
        btnSendResetLink.setOnClickListener(v -> sendResetLink());
    }

    private void sendResetLink() {
        String email = etContact.getText().toString().trim();

        // Kiểm tra tính hợp lệ của email
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!email.contains("@") || !email.contains(".")) {
            Toast.makeText(this, "Email không hợp lệ", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gửi yêu cầu gửi liên kết reset mật khẩu đến email
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        // Thông báo đã gửi email
                        Toast.makeText(ForgotPasswordActivity.this, "Liên kết reset mật khẩu đã được gửi vào email của bạn", Toast.LENGTH_SHORT).show();
                        finish();  // Quay lại màn hình đăng nhập sau khi gửi email
                    } else {
                        // Xử lý lỗi
                        String errorMessage = task.getException() != null ? task.getException().getMessage() : "Lỗi không xác định";
                        Toast.makeText(ForgotPasswordActivity.this, "Lỗi: " + errorMessage, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Hàm xử lý sự kiện khi nhấn vào nút quay lại
    public void goBack(View view) {
        // Sử dụng phương thức của AppCompatActivity
        getOnBackPressedDispatcher().onBackPressed();
    }
}
