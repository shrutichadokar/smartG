package com.example.sproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;


public class driverreg extends AppCompatActivity {
    EditText Username1, Email1, MobileNumber1, Password1,Address1,Adharno1,Area1;
    Button submit;
    TextView textView;

    String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." +
            "[a-zA-Z0-9_+&*-]+)*@" +
            "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
            "A-Z]{2,7}$";

    Pattern pat = Pattern.compile(emailRegex);

    ProgressDialog progressDialog;

    FirebaseAuth auth;
    FirebaseUser firebaseUser;
    DatabaseReference referenceProfile;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_driverreg);


        Username1 = findViewById(R.id.Username1);
        Email1 = findViewById(R.id.Email1);
        Password1 = findViewById(R.id.Password1);
        Address1= findViewById(R.id.Address1);
        MobileNumber1= findViewById(R.id.MobileNumber1);
        Area1 = findViewById(R.id.Area1);
        submit = findViewById(R.id.submit);
        Adharno1=findViewById(R.id.Adharno1);
        progressDialog = new ProgressDialog(this);

        auth = FirebaseAuth.getInstance();
        firebaseUser = auth.getCurrentUser();

       /* submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(driverreg.this, driverlogin.class));
            }
        });*/

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });

    }

    private void PerformAuth() {
        String email = Email1.getText().toString();
        String password = Password1.getText().toString();
        String address = Address1.getText().toString();
        String username = Username1.getText().toString();
        String phoneNumber = MobileNumber1.getText().toString();
        String area=Area1.getText().toString();
        String adharno=Adharno1.getText().toString();
        if (email.isEmpty()) {
            Email1.setError("Please Enter Email");
            return;
        } else if (!pat.matcher(email).matches()) {
            Email1.setError("Please Enter a valid Email");
            return;
        } else if (password.isEmpty()) {
            Password1.setError("Please input Password");
            return;
        } else if (password.length() < 6) {
            Password1.setError("Password too short");
            return;

        } else {
            progressDialog.setMessage("Creating your Account....");
            progressDialog.setTitle("Creating");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        progressDialog.dismiss();

                        FirebaseUser firebaseUser = auth.getCurrentUser();
                        String userId = firebaseUser.getUid();

                        referenceProfile= FirebaseDatabase.getInstance().getReference().child(userregister.Driver).child(userId);
//                        HashMap<String, String> hashMap = new HashMap<>();
//                        hashMap.put("id", userId);
//                        hashMap.put("username", username);
//                        hashMap.put("email", email);
//                        hashMap.put("password", password);
//                        hashMap.put("address", address);
//                        hashMap.put("phoneNumber", phoneNumber);



                        Departmentuser model = new Departmentuser();
                        model.setPhoneNumber(phoneNumber);
                        model.setEmail(email);
                        model.setPassword(password);
                        model.setId(userId);
                        model.setUserName(username);
                        model.setAdharno(adharno);
                        model.setArea(area);
                        model.setAddress(address);

                        referenceProfile.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    sendUserToMainActivity();
                                }
                            }
                        });


                        Toast.makeText(driverreg.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(driverreg.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void sendUserToMainActivity() {
        Intent intent = new Intent(driverreg.this, admindash.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }

}




























                    /*referenceProfile.child("drivers").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //check if phone registered before
                            if(snapshot.hasChild(mobilenumber)){
                                Toast.makeText(crtdriver.this, "phone already registered ,use another mobile number", Toast.LENGTH_SHORT).show();
                            }else{
                                FirebaseAuth auth= FirebaseAuth.getInstance();
                                FirebaseUser firebaseUser=auth.getCurrentUser();

                                String userId = firebaseUser.getUid();

                                referenceProfile = FirebaseDatabase.getInstance().getReference().child(userregister.Driver).child(userId);
                                progressBar1.setVisibility(View.VISIBLE);
                                //sending data to firebase realtime database
                                //phone number is used as unique id therefore all details comes under phone number
                                referenceProfile.child("drivers").child(mobilenumber).child("Username1").setValue(username);
                                referenceProfile.child("drivers").child(mobilenumber).child("Email1").setValue(email);
                                referenceProfile.child("drivers").child(mobilenumber).child("MobileNumber1").setValue( mobilenumber );
                                referenceProfile.child("drivers").child(mobilenumber).child("Address1").setValue(address);
                                referenceProfile.child("drivers").child(mobilenumber).child("Password1").setValue(password);
                                referenceProfile.child("drivers").child(mobilenumber).child(" Adharno1").setValue(adharno);
                                referenceProfile.child("drivers").child(mobilenumber).child("Area1").setValue(area);
                                Toast.makeText(crtdriver.this, " Registration successful", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }

        });


    }


    }  */





             /*   }else{
                    try{
                        throw task.getException();
                    }catch(FirebaseAuthWeakPasswordException e){
                        Password1.setError("Your password is weak.Kindly use mix character,numbers,uppercase,and special characters");
                        Password1.requestFocus();
                    }catch(FirebaseAuthInvalidCredentialsException e){
                        Password1.setError("Your email is invalid or already in use.");
                        Password1.requestFocus();
                    }catch(FirebaseAuthUserCollisionException e){
                        Email1.setError("User already registered with this email.Use another email.");
                        Email1.requestFocus();
                    }catch(Exception e){
                        Log.e(TAG,e.getMessage());
                        Toast.makeText(crtdriver.this,e.getMessage(),Toast.LENGTH_LONG).show();

                    }
                    progressBar1.setVisibility(View.GONE);

                }
            }
        });
    }
}*/

