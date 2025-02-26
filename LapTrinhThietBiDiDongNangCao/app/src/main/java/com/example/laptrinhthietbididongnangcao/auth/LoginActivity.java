package com.example.laptrinhthietbididongnangcao.auth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.laptrinhthietbididongnangcao.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText etEmail, etPassword, etPhone, etOtp;
    private Button btnLogin, btnSendOtp, btnVerifyOtp;
    private TextView tvForgotPassword, tvRegister;
    private LinearLayout layoutEmail, layoutPhone;
    private TabLayout tabLayout;

    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Khởi tạo FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        // Ánh xạ view
        tabLayout = findViewById(R.id.tabLayout);
        layoutEmail = findViewById(R.id.layout_email);
        layoutPhone = findViewById(R.id.layout_phone);

        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegister = findViewById(R.id.tv_register);

        etPhone = findViewById(R.id.et_phone);
        btnSendOtp = findViewById(R.id.btn_send_otp);
        etOtp = findViewById(R.id.et_otp);
        btnVerifyOtp = findViewById(R.id.btn_verify_otp);

        // Xử lý sự kiện chuyển tab
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) { // Tab Email
                    layoutEmail.setVisibility(View.VISIBLE);
                    layoutPhone.setVisibility(View.GONE);
                } else { // Tab Số điện thoại
                    layoutEmail.setVisibility(View.GONE);
                    layoutPhone.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Xử lý đăng nhập bằng Email
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            } else {
                loginUser(email, password);
            }
        });

        // Chuyển tới màn hình Quên mật khẩu
        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });

        // Chuyển tới màn hình Đăng ký
        tvRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        // Xử lý gửi mã OTP
        btnSendOtp.setOnClickListener(v -> {
            String phone = etPhone.getText().toString().trim();
            if (phone.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập số điện thoại", Toast.LENGTH_SHORT).show();
            } else {
                sendOtp(phone);
            }
        });

        // Xác nhận OTP
        btnVerifyOtp.setOnClickListener(v -> {
            String otp = etOtp.getText().toString().trim();
            if (otp.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Vui lòng nhập mã OTP", Toast.LENGTH_SHORT).show();
            } else {
                verifyOtp(otp);
            }
        });
    }

    // Hàm xử lý đăng nhập bằng Email
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(LoginActivity.this, "Đăng nhập thành công: " + user.getEmail(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LoginActivity.this, com.example.laptrinhthietbididongnangcao.dashboard.HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Đăng nhập thất bại. Kiểm tra lại email hoặc mật khẩu.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Placeholder: Hàm gửi OTP (Bạn cần tích hợp Firebase Authentication)
    private void sendOtp(String phone) {
        // Đảm bảo số điện thoại có mã quốc gia (VD: +84 cho Việt Nam)
        if (!phone.startsWith("+")) {
            phone = "+84" + phone.substring(1);
        }

        PhoneAuthOptions options = PhoneAuthOptions.newBuilder(mAuth)
                .setPhoneNumber(phone) // Số điện thoại phải đúng định dạng quốc tế
                .setTimeout(60L, TimeUnit.SECONDS)
                .setActivity(this)
                .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential credential) {
                        signInWithPhoneAuthCredential(credential);
                    }

                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        Log.e("OTP", "Gửi OTP thất bại: " + e.getMessage());
                        Toast.makeText(LoginActivity.this, "Gửi OTP thất bại: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCodeSent(@NonNull String verificationId,
                                           @NonNull PhoneAuthProvider.ForceResendingToken token) {
                        LoginActivity.this.verificationId = verificationId;
                        LoginActivity.this.resendToken = token;
                        Log.d("OTP", "Mã OTP đã được gửi: " + verificationId);
                        Toast.makeText(LoginActivity.this, "Mã OTP đã được gửi!", Toast.LENGTH_SHORT).show();
                    }
                })
                .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    // Placeholder: Hàm xác nhận OTP (Bạn cần tích hợp Firebase Authentication)
    private void verifyOtp(String otp) {
        if (verificationId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy mã xác thực. Gửi lại OTP.", Toast.LENGTH_SHORT).show();
            return;
        }

        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Log.d("OTP", "Đăng nhập bằng OTP thành công!");
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(LoginActivity.this, com.example.laptrinhthietbididongnangcao.dashboard.HomeActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Log.e("OTP", "Xác thực OTP thất bại", task.getException());
                        Toast.makeText(LoginActivity.this, "Xác thực OTP thất bại", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
