package com.example.chatme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class ActivityLogin extends AppCompatActivity {
    EditText email, pass;
    TextView toRegister;
    Button login;

    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.txtEmail);
        pass = findViewById(R.id.txtPass);
        login = findViewById(R.id.btnLogin);
        toRegister = findViewById(R.id.toRegister);

        auth = FirebaseAuth.getInstance();


        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ActivityLogin.this, ActivityRegis.class);
                startActivity(intent);
                finish();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = email.getText().toString();
                String newPass = pass.getText().toString();

                if (TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newPass)) {
                    Toast.makeText(ActivityLogin.this, "Harap Isi Semua Permintaan", Toast.LENGTH_SHORT).show();
                }
                else {
                    auth.signInWithEmailAndPassword(newEmail, newPass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        SharedPreferences.Editor editor = getSharedPreferences("LOGIN", MODE_PRIVATE).edit();
                                        editor.putBoolean("isLoggedIn", true);
                                        editor.apply();

                                        Intent intent = new Intent(ActivityLogin.this, ActivityMenu.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(ActivityLogin.this, "Login Gagal", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                 }
            }
        });
    }
}
