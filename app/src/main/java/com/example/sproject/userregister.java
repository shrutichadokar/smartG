package com.example.sproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class userregister extends AppCompatActivity {
    TextView textview7;
    EditText Username, Password, Email, MobileNumber, Address;
    ProgressBar progressBar;
    Button button5;
    String emailPattern = " [a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    static final String TAG="users";
    public static final String Driver = "drivers";
    public static final String USERS = "users";
    public static final String COMPLAINT = "complaints";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregister);
        textview7=findViewById(R.id.textView7);
        Username=findViewById(R.id.Username);
        Password=findViewById(R.id.Password);
        Email=findViewById(R.id.Email);
        MobileNumber=findViewById(R.id.MobileNumber);
        Address=findViewById(R.id.Address);
        button5=findViewById(R.id.buttonsign);
        progressBar=findViewById(R.id.progressBar);
        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //obtain the entered data
                String username = Username.getText().toString();
                String email = Email.getText().toString();
                String mobilenumber = MobileNumber.getText().toString();
                String address = Address.getText().toString();
                String password = Password.getText().toString();

                //validate mobile number using matcher and pattern (regular expression)
                String mobileRegex= "[6-9][0-9]{9}";
                Matcher mobileMatcher ;
                Pattern mobilePattern=Pattern.compile(mobileRegex);
                mobileMatcher=mobilePattern.matcher(mobilenumber);


                if(TextUtils.isEmpty(username)){
                    Toast.makeText(userregister.this, "field is required", Toast.LENGTH_SHORT).show();
                    Username.setError("Username is required");
                    Username.requestFocus();
                }else if (TextUtils.isEmpty(email)){
                    Toast.makeText(userregister.this, "field is required", Toast.LENGTH_SHORT).show();
                    Email.setError("email is required");
                    Email.requestFocus();
                }else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(userregister.this, " Re-enter your email", Toast.LENGTH_SHORT).show();
                    Email.setError(" valid email is required");
                    Email.requestFocus();
                }else if (TextUtils.isEmpty(mobilenumber)) {
                    Toast.makeText(userregister.this, "field is required", Toast.LENGTH_SHORT).show();
                    MobileNumber.setError(" Mobile Number is required");
                    MobileNumber.requestFocus();
                } else if (mobilenumber.length()!=10) {
                    Toast.makeText(userregister.this, "Re-enter mobile number", Toast.LENGTH_SHORT).show();
                    MobileNumber.setError(" 10 digit required");
                    MobileNumber.requestFocus();
                } else if (!mobileMatcher.find()){
                    Toast.makeText(userregister.this, "Re-enter mobile number", Toast.LENGTH_SHORT).show();
                    MobileNumber.setError("mobile number is not valid");
                    MobileNumber.requestFocus();

                } else if (TextUtils.isEmpty(address)) {
                    Toast.makeText(userregister.this, "field is required", Toast.LENGTH_SHORT).show();
                    Address.setError(" address is required");
                    Address.requestFocus();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(userregister.this, "field is required", Toast.LENGTH_SHORT).show();
                    Password.setError(" Password  is required");
                    Password.requestFocus();
                }else if (password.length() <8) {
                    Toast.makeText(userregister.this, "should be 6 digits", Toast.LENGTH_SHORT).show();
                    Password.setError(" Password too weak");
                    Password.requestFocus();
                }else{

                    progressBar.setVisibility(View.VISIBLE);
                    registerUser(username,email,mobilenumber,address,password);
                }
            }

        });
    }

    private void registerUser(String username, String email, String mobilenumber, String address, String password) {

        FirebaseAuth auth= FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(userregister.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    FirebaseUser firebaseUser=auth.getCurrentUser();

                    //Enter user Data into the firebase database
                    ReadWriteUserDetails writeUserDetails=new ReadWriteUserDetails(username,mobilenumber,address);

                    String userId = firebaseUser.getUid();


                    //Extracting user reference from datadbase for "Registered Users"
                    DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference().child(USERS).child(userId);
                    referenceProfile.child(firebaseUser.getUid()).setValue(writeUserDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful()){
                                //send verification email
                               firebaseUser.sendEmailVerification();
                                Toast.makeText(userregister.this, "User registered successfully .Please verify your email", Toast.LENGTH_SHORT).show();

                                  /* //after successful registration open login page
                                   Intent intent =new Intent(userregister.this,userlogin.class);

                                   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |Intent.FLAG_ACTIVITY_CLEAR_TASK|
                                   Intent.FLAG_ACTIVITY_NEW_TASK);
                                   startActivity(intent);
                                   finish();    //to close register activity */
                            }else{
                                Toast.makeText(userregister.this, "User registered failed.Please try again", Toast.LENGTH_SHORT).show();

                            }
                            progressBar.setVisibility(View.GONE);
                        }
                    });

                }else{
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        Password.setError("Your password is weak.Kindly use mix character,numbers,uppercase,and special characters");
                        Password.requestFocus();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        Password.setError("Your email is invalid or already in use.");
                        Password.requestFocus();
                    }catch(FirebaseAuthUserCollisionException e){
                        Email.setError("User already registered with this email.Use another email.");
                        Email.requestFocus();
                    }catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(userregister.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                    progressBar.setVisibility(View.GONE);

                }
            }
        });



    }
}



