package com.example.jembatani;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private static final String USERS = "users";
    private static final String PASSWORD = "password";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://jembatani-c0a0b-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        final EditText loginPhone = findViewById(R.id.Phone_Signin);
        final EditText loginPassword = findViewById(R.id.Password_Signin);
        final TextView signupNavigate = findViewById(R.id.textSignUp);
        final Button loginButton = findViewById(R.id.buttonSignin);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String phoneTxt = loginPhone.getText().toString().trim();
                final String passwordTxt = loginPassword.getText().toString().trim();

                if (phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(Login.this, "Please enter your phone number and password", Toast.LENGTH_SHORT).show();
                } else {
                    databaseReference.child(USERS).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // Check if the phone number exists in Firebase Database
                            if (snapshot.hasChild(phoneTxt)) {
                                // Mobile exists in Firebase Database
                                // Now get the password of the user from Firebase Database and match it with the entered password
                                final String getPassword = snapshot.child(phoneTxt).child(PASSWORD).getValue(String.class);

                                if (getPassword != null && getPassword.equals(passwordTxt)) {
                                    Toast.makeText(Login.this, "Successfully Logged in", Toast.LENGTH_SHORT).show();

                                    // Open MainActivity on Success
                                    startActivity(new Intent(Login.this, MainActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(Login.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(Login.this, "Phone Number not registered", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle potential errors
                            Toast.makeText(Login.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        signupNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open Signup Activity
                startActivity(new Intent(Login.this, signup.class));
            }
        });
    }
}
