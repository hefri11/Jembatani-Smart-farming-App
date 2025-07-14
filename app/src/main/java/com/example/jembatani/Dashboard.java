package com.example.jembatani;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

public class Dashboard extends AppCompatActivity {

    // Inisialisasi TextViews
    private TextView textDataHum; // Kelembapan
    private TextView textDataTemp; // Suhu
    private TextView textDataHumSoil; // Soil
    private TextView textDataTempSoil; // ds18b20

    // Reference untuk Firebase
    private Firebase mHum; // Kelembapan
    private Firebase mTemp; // Suhu
    private Firebase mHumSoil; // Soil
    private Firebase mTempSoil; // ds18b20
    private Firebase mWatering; // Reference untuk pengiriman data 0 atau 1

    private boolean isWatering = false; // Flag untuk menentukan apakah tanaman sedang disiram

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER_PORTRAIT);

        ImageView logoutDashScreen = findViewById(R.id.exit_button_dash);
        ImageView imageWatering = findViewById(R.id.imageWatering);

        // Mengatur insets untuk kompatibilitas tepi layar
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.mainDash), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        logoutDashScreen.setOnClickListener(v -> {
            navigateToLogoutDashScreen();
        });

        // Baca komponen nilai dari Firebase
        textDataHum = findViewById(R.id.textDataHum);
        mHum = new Firebase("https://jembatani-c0a0b-default-rtdb.firebaseio.com/sensor/dht11_humidity");

        textDataTemp = findViewById(R.id.textDataTemp);
        mTemp = new Firebase("https://jembatani-c0a0b-default-rtdb.firebaseio.com/sensor/dht11_suhu");

        textDataHumSoil = findViewById(R.id.textDataHumSoil);
        mHumSoil = new Firebase("https://jembatani-c0a0b-default-rtdb.firebaseio.com/sensor/soil");

        textDataTempSoil = findViewById(R.id.textDataTempSoil);
        mTempSoil = new Firebase("https://jembatani-c0a0b-default-rtdb.firebaseio.com/sensor/ds18b20");

        // Inisialisasi Firebase untuk mengirimkan data 0 atau 1
        mWatering = new Firebase("https://jembatani-c0a0b-default-rtdb.firebaseio.com/sensor/relay");

        // Tambahkan listener untuk pengiriman data 0 atau 1 ke Firebase saat ImageView diklik
        imageWatering.setOnClickListener(v -> {
            if (isWatering) {
                mWatering.setValue(0); // Mengirimkan 0 ke Firebase
                Toast.makeText(Dashboard.this, "Watering Stopped", Toast.LENGTH_SHORT).show();
            } else {
                mWatering.setValue(1); // Mengirimkan 1 ke Firebase
                Toast.makeText(Dashboard.this, "Watering Started", Toast.LENGTH_SHORT).show();
            }
            isWatering = !isWatering; // Toggle flag isWatering
        });

        // Mendengarkan perubahan data dari Firebase
        mHum.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dht_humidity = dataSnapshot.getValue(String.class);
                textDataHum.setText(dht_humidity);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // Error handling
            }
        });

        mTemp.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String dht11_suhu = dataSnapshot.getValue(String.class);
                textDataTemp.setText(dht11_suhu);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // Error handling
            }
        });

        mHumSoil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String soil = dataSnapshot.getValue(String.class);
                textDataHumSoil.setText(soil);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // Error handling
            }
        });

        mTempSoil.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String ds18b20 = dataSnapshot.getValue(String.class);
                textDataTempSoil.setText(ds18b20);
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                // Error handling
            }
        });
    }

    private void navigateToLogoutDashScreen() {
        Intent intent = new Intent(Dashboard.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
