package com.example.sproject;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
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


public class adminlogin extends AppCompatActivity {
    EditText Email, Password;
    ProgressBar progressBar;
    FirebaseAuth authProfile;
    Button buttonsign;

    static final String TAG = "adminlogin";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminlogin);
        buttonsign=findViewById(R.id.buttonsign);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        authProfile = FirebaseAuth.getInstance();


        buttonsign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(adminlogin.this, "field is required", Toast.LENGTH_SHORT).show();
                    Email.setError("Email is required");
                    Email.requestFocus();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(adminlogin.this, " Re-enter your email", Toast.LENGTH_SHORT).show();
                    Email.setError(" Valid email is required");
                    Email.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(adminlogin.this, "field is required", Toast.LENGTH_SHORT).show();
                    Password.setError(" Password  is required");
                    Password.requestFocus();
                } else {
                    loginUser(email, password);
                }
            }
        });

    }

    private void loginUser(String email, String password) {

        authProfile.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(adminlogin.this, "Login successful", Toast.LENGTH_SHORT).show();

                    //Get instance of current user
                    FirebaseUser firebaseUser = authProfile.getCurrentUser();
                    startActivity(new Intent(adminlogin.this, admindash.class));
                    finish();
                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        Password.setError("Invalid credentials.Enter again");
                        Password.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(adminlogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
    }
}




