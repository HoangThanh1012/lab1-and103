package com.example.lab1_and103;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LoginEmailActivity extends AppCompatActivity {

    private EditText edtUser, edtPass;
    private Button btnLogin;
    private TextView txtForgot, txtSignUp;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_email);

        edtUser = findViewById(R.id.edtUser);
        edtPass = findViewById(R.id.edtPass);
        btnLogin = findViewById(R.id.btnLogin);
        txtForgot = findViewById(R.id.txtForgot);
        txtSignUp = findViewById(R.id.txtSignUp);
        mAuth = FirebaseAuth.getInstance();

        btnLogin.setOnClickListener(view -> {
            String email = edtUser.getText().toString();
            String password = edtPass.getText().toString();

            if (!email.isEmpty() && !password.isEmpty()) {
                loginUser(email, password);
            } else {
                Toast.makeText(LoginEmailActivity.this, "Vui long nhap day du thong tin", Toast.LENGTH_SHORT).show();
            }
        });

        txtForgot.setOnClickListener(view -> {
            String email = edtUser.getText().toString();
            if (!TextUtils.isEmpty(email)) {
                sendPasswordResetEmail(email);
            } else {
                Toast.makeText(LoginEmailActivity.this, "Vui long nhap email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginEmailActivity.this, "Login thanh cong", Toast.LENGTH_SHORT).show();
                        // Navigate to another activity after login success
                    } else {
                        Toast.makeText(LoginEmailActivity.this, "Loi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void sendPasswordResetEmail(String email) {
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(LoginEmailActivity.this, "Pass da duoc gui ve email", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginEmailActivity.this, "Loi: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
