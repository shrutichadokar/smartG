package com.example.sproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class regcomplain extends AppCompatActivity {

    TextView textViewName, textViewEmail,textViewAddress,textViewMobile;
    String name,email,address,mobile;
    EditText complain;
    FirebaseAuth authProfile;
    DatabaseReference complainref;

    Button button11;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regcomplain);

textViewName=findViewById(R.id.textView_show_name0);
        complain=findViewById(R.id.complain);
        textViewEmail = findViewById(R.id.textView_show_email);
        textViewAddress = findViewById(R.id.textView_show_address);
        textViewMobile = findViewById(R.id.textView_show_mobileno0);
        complain=findViewById(R.id.complain);
        authProfile = FirebaseAuth.getInstance();
        button11=findViewById(R.id.button11);
        complainref=FirebaseDatabase.getInstance().getReference().child("complaints");
        FirebaseUser firebaseUser = authProfile.getCurrentUser();
            showUserprofile(firebaseUser);


    }
    private void showUserprofile(FirebaseUser firebaseUser ){

        String userID=firebaseUser.getUid();
        //Extracting user reference from database for Registered Users
        DatabaseReference referenceProfile= FirebaseDatabase.getInstance().getReference("Registered users");
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
                Toast.makeText(regcomplain.this, "Something went wrong!!",
                        Toast.LENGTH_LONG).show();
            }
        });

        button11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertcomplainData();
            }
        });


    }

    private void insertcomplainData() {

        String comp=complain.getText().toString();

        complainmodel complain=new complainmodel(comp);
        complainref.push().setValue(complain);
        Toast.makeText(this, "Complain Registered  successfully", Toast.LENGTH_SHORT).show();
    }
}