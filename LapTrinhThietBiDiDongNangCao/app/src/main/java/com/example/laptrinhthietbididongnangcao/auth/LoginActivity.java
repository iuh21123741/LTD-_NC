package com.example.laptrinhthietbididongnangcao.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laptrinhthietbididongnangcao.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login); // link tới layout của bạn

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Lấy các view từ layout
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);

        // Đăng nhập khi người dùng click vào nút Đăng nhập
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Chuyển tới màn hình quên mật khẩu
        tvForgotPassword.setOnClickListener(v -> {
            // Bạn có thể thay thế màn hình quên mật khẩu ở đây
            Intent intent = new Intent(LoginActivity.this,  com.example.laptrinhthietbididongnangcao.auth.ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Chuyển tới màn hình đăng ký
        tvRegister.setOnClickListener(v -> {
            // Điều hướng tới Activity đăng ký
            // Intent chuyển tới RegisterActivity nếu bạn đã tạo nó
            // startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            Intent intent = new Intent(LoginActivity.this,  com.example.laptrinhthietbididongnangcao.auth.RegisterActivity.class);
            startActivity(intent);
        });
    }

    // Hàm thực hiện đăng nhập
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Đăng nhập thành công
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            // Điều hướng tới trang chính của ứng dụng hoặc màn hình tiếp theo
                            Intent intent = new Intent(LoginActivity.this, com.example.laptrinhthietbididongnangcao.dashboard.HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // Đăng nhập thất bại
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại email hoặc mật khẩu.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
