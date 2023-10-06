package com.example.sproject;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;


public class userlogin extends AppCompatActivity {
     EditText Email,Password;
     ProgressBar progressBar;
     FirebaseAuth authProfile;
    TextView textview7;

    static final String TAG="userlogin";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userlogin);

        textview7=findViewById(R.id.textView7);
         Email=findViewById(R.id.Email);
         Password=findViewById(R.id.Password);
         progressBar = findViewById(R.id.progressBar1);
         authProfile=FirebaseAuth.getInstance();
        Button button5=findViewById(R.id.buttonsign);
         Button forgot_password=findViewById(R.id.forgot_password);

        forgot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(userlogin.this, "You can reset your password now!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(userlogin.this,ForgotPasswordActivity.class));
            }
        });


        textview7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userlogin.this,userregister.class));
            }
        });
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = Email.getText().toString();
                String password = Password.getText().toString();

                if (TextUtils.isEmpty(email)){
                    Toast.makeText(userlogin.this, "field is required", Toast.LENGTH_SHORT).show();
                    Email.setError("Email is required");
                    Email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(userlogin.this, " Re-enter your email", Toast.LENGTH_SHORT).show();
                    Email.setError(" Valid email is required");
                    Email.requestFocus();
                }else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(userlogin.this, "field is required", Toast.LENGTH_SHORT).show();
                    Password.setError(" Password  is required");
                    Password.requestFocus();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    loginUser(email,password);
                }
            }
        });

    }

    private void loginUser(String email, String password) {

        authProfile.signInWithEmailAndPassword(email ,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(userlogin.this, "Login successful", Toast.LENGTH_SHORT).show();

                    //Get instance of current user
                    FirebaseUser firebaseUser=authProfile.getCurrentUser();
                    if (firebaseUser.isEmailVerified()){
                        Toast.makeText(userlogin.this, "you are logged in now", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(userlogin.this,  userdashboard.class));
                        finish();
                    }else{
                        firebaseUser.sendEmailVerification();
                        authProfile.signOut();
                        showAlertDialog();
                    }

                }else{
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        Password.setError("Invalid credentials.Enter again");
                        Password.requestFocus();
                    }catch (Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(userlogin.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }


                }
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void showAlertDialog() {
        //set up the alert builder
        AlertDialog.Builder builder=new AlertDialog.Builder(userlogin.this);
        builder.setTitle("Email not verified");
        builder.setMessage("Please verify your email now.You cannot login without email verification");

        builder.setPositiveButton("continue", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent=new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
