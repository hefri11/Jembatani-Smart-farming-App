package com.example.jembatani;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;



public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        ImageView LogoutScreen = findViewById(R.id.exit_button);
        ImageView NavigateDashboard = findViewById(R.id.plant_image);

        LogoutScreen.setOnClickListener(v -> {
            // Navigate to the "Create account" layout
            navigateToLogoutScreen();
        });

        NavigateDashboard.setOnClickListener(v -> {
            // Navigate to Dashboard
            navigateToDashboardScreen();
        });
    }


    private void navigateToDashboardScreen() {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        startActivity(intent);
        finish();
    }

    private void navigateToLogoutScreen() {
        Intent intent = new Intent(MainActivity.this, Login.class);
        startActivity(intent);
        finish();
    }
}