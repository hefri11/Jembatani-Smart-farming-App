package com.example.jembatani;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class signup extends AppCompatActivity {

    // create object of DatabaseReference class to access firebase Realtime Database
    private static final String USERS = "users";
    private static final String FULLNAME = "fullname";
    private static final String EMAIL = "email";
    private static final String PASSWORD = "password";

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://jembatani-c0a0b-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

        final EditText fullname = findViewById(R.id.Name_Signup);
        final EditText email = findViewById(R.id.Email_Signup);
        final EditText phone = findViewById(R.id.Phone_Signup);
        final EditText password = findViewById(R.id.Password_Signup);
        final EditText rePassword = findViewById(R.id.RePassword_Signup);

        final Button registerBtn = findViewById(R.id.buttonSignup);
        final TextView loginNavigate = findViewById(R.id.textSignIn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // get Data from EditText
                final String fullnameTxt = fullname.getText().toString().trim();
                final String emailTxt = email.getText().toString().trim();
                final String phoneTxt = phone.getText().toString().trim();
                final String passwordTxt = password.getText().toString().trim();
                final String rePasswordTxt = rePassword.getText().toString().trim();

                // check if user fill all the fields before sending data to firebase
                if (fullnameTxt.isEmpty() || emailTxt.isEmpty() || phoneTxt.isEmpty() || passwordTxt.isEmpty()) {
                    Toast.makeText(signup.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if passwords match
                if (!passwordTxt.equals(rePasswordTxt)) {
                    Toast.makeText(signup.this, "Passwords are not matching", Toast.LENGTH_SHORT).show();
                    return;
                }

                // check if phone is not registered before
                databaseReference.child(USERS).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.hasChild(phoneTxt)) {
                            Toast.makeText(signup.this, "Phone number is already registered", Toast.LENGTH_SHORT).show();
                        } else {
                            // send data to firebase Realtime Database
                            databaseReference.child(USERS).child(phoneTxt).child(FULLNAME).setValue(fullnameTxt);
                            databaseReference.child(USERS).child(phoneTxt).child(EMAIL).setValue(emailTxt);
                            databaseReference.child(USERS).child(phoneTxt).child(PASSWORD).setValue(passwordTxt);

                            // show a success message and finish the activity
                            Toast.makeText(signup.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // Handle potential errors
                        Toast.makeText(signup.this, "Database error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        loginNavigate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open Login Activity
                startActivity(new Intent(signup.this, Login.class));
            }
        });

    }

}
