package com.example.lab1_and103;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken;

import java.util.concurrent.TimeUnit;

public class PhoneLoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText edtPhone, edtOTP;
    private Button btnSendOtp, btnLogin;
    private String verificationId;
    private PhoneAuthProvider.ForceResendingToken resendToken;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_login);

        mAuth = FirebaseAuth.getInstance();
        edtPhone = findViewById(R.id.edtPhone);
        edtOTP = findViewById(R.id.edtOTP);
        btnSendOtp = findViewById(R.id.btnSendOtp);
        btnLogin = findViewById(R.id.btnLogin);

        btnSendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = edtPhone.getText().toString().trim();
                if (!phone.isEmpty()) {
                    sendOtp(phone);
                } else {
                    Toast.makeText(PhoneLoginActivity.this, "Vui lòng nhập số điện thoại!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = edtOTP.getText().toString().trim();
                if (!otp.isEmpty()) {
                    verifyOtp(otp);
                } else {
                    Toast.makeText(PhoneLoginActivity.this, "Vui lòng nhập mã OTP!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendOtp(String phone) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone)       // Số điện thoại
                        .setTimeout(60L, TimeUnit.SECONDS)  // Thời gian chờ
                        .setActivity(this)  // Activity hiện tại
                        .setCallbacks(new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                                // Khi OTP được xác thực tự động
                                signInWithPhoneAuthCredential(phoneAuthCredential);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {

                            }

                            public void onVerificationFailed(Exception e) {
                                Toast.makeText(PhoneLoginActivity.this, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCodeSent(String verificationId, ForceResendingToken token) {
                                // Mã OTP đã được gửi
                                PhoneLoginActivity.this.verificationId = verificationId;
                                PhoneLoginActivity.this.resendToken = token;
                                Toast.makeText(PhoneLoginActivity.this, "Mã OTP đã được gửi", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyOtp(String otp) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, otp);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = task.getResult().getUser();
                        Toast.makeText(PhoneLoginActivity.this, "Đăng nhập thành công", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(PhoneLoginActivity.this, "Đăng nhập thất bại: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
