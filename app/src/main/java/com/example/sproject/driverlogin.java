package com.example.sproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;


import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;


public class driverlogin extends AppCompatActivity {
    EditText Email, Password;
    ProgressDialog progressDialog;
    Button buttonsign;
    FirebaseAuth auth;
    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    FirebaseUser firebaseUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverlogin);
        buttonsign = findViewById(R.id.buttonsign);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);

        progressDialog = new ProgressDialog(this);
        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

        buttonsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                performLogin();
            }
        });
    }

        private void performLogin() {
            String email = Email.getText().toString();
            String password = Password.getText().toString();

            if (email.isEmpty()) {
                Email.setError("Please Enter Email");
                return;
            } else if (!pat.matcher(email).matches()) {
                Email.setError("Please Enter a valid Email");
                return;
            } else if (password.isEmpty()) {
                Password.setError("Please input Password");
                return;
            } else if (password.length() < 6) {
                Password.setError("Password too short");
                return;
            } else {
                progressDialog.setMessage("Login in to your Account....");
                progressDialog.setTitle("Loading");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            sendUserToMainActivity();
                            Toast.makeText(driverlogin.this, "Login Successful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressDialog.dismiss();
                            Toast.makeText(driverlogin.this, "Login Failed", Toast.LENGTH_SHORT).show();
                        }
                    }

                    private void sendUserToMainActivity() {
                        Intent intent = new Intent(driverlogin.this, driverdash.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);


                    }
                });
            }
        }
    }


