package com.example.sproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class adminviewuser extends AppCompatActivity {
    TextView textViewName, textViewEmail,textViewAddress,textViewMobile;
    String name,email,address,mobile;
    FirebaseAuth authProfile;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminviewuser);
         textViewName=findViewById(R.id.textView_show_name0);
        textViewEmail = findViewById(R.id.textView_show_email0);
        textViewAddress = findViewById(R.id.textView_show_address0);
        textViewMobile = findViewById(R.id.textView_show_mobileno0);
        authProfile = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
        if (firebaseUser == null) {
            Toast.makeText(adminviewuser.this, "something went wrong",
                    Toast.LENGTH_LONG).show();
        } else {
            showUserprofile(firebaseUser);

        }

    }
    private void showUserprofile(FirebaseUser firebaseUser ){

        String userID=firebaseUser.getUid();
        //Extracting user reference from database for Registered Users
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Admin");
        referenceProfile.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteUserDetails readUserDetails=snapshot.getValue(ReadWriteUserDetails.class);
                if (readUserDetails!=null){
                    name=readUserDetails.name;
                    email=firebaseUser.getEmail();
                    mobile=readUserDetails.phone;
                    address=readUserDetails.uaddress;

                    textViewName.setText(name);
                    textViewEmail.setText(email);
                    textViewAddress.setText(address);
                    textViewMobile.setText(mobile);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(adminviewuser.this, "Something went wrong!!",
                        Toast.LENGTH_LONG).show();
            }
        });

    }
}