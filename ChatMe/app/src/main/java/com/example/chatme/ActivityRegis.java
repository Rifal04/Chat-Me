package com.example.chatme;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ActivityRegis extends AppCompatActivity {

    EditText email, user, pass, confirm;
    Button register;
    DatabaseReference reference;
    FirebaseAuth auth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);

        email = findViewById(R.id.txtEmailRegis);
        user = findViewById(R.id.txtUserRegis);
        pass = findViewById(R.id.txtPassRegis);
        register = findViewById(R.id.btnRegis);
        confirm = findViewById(R.id.txtConfirmPassRegis);

        auth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newEmail = email.getText().toString();
                String newUser = user.getText().toString();
                String newPass = pass.getText().toString();
                String newConfirm = confirm.getText().toString();

                if (TextUtils.isEmpty(newEmail) || TextUtils.isEmpty(newUser) || TextUtils.isEmpty(newPass) || TextUtils.isEmpty(newConfirm)) {
                    Toast.makeText(ActivityRegis.this, "Harap Isi Semua Permintaan", Toast.LENGTH_SHORT).show();
                } else if (newPass.length() < 6) {
                    Toast.makeText(ActivityRegis.this, "Password Harus 6 Karakter", Toast.LENGTH_SHORT).show();
                }else if (!newPass.equals(newConfirm)) {
                    Toast.makeText(ActivityRegis.this, "Password Tidak Sama", Toast.LENGTH_SHORT).show();
                } else {
                    register(newEmail, newUser, newPass);
                }
            }
        });
    }

    private void register (String email, String user, String pass) {

        auth.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            SharedPreferences.Editor editor = getSharedPreferences("USER_PREFS", MODE_PRIVATE).edit();
                            editor.putString("username", user);
                            editor.putString("email", email);
                            editor.apply();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("username", user);
                            hashMap.put("imageURL", "default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Intent intent = new Intent(ActivityRegis.this, ActivityLogin.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(ActivityRegis.this, "Registrasi Gagal", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

}

