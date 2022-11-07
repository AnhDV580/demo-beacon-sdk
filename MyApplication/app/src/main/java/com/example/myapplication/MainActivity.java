package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Button buttonInputUserInfo, buttonBeaconDetection, buttonGPSTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        clickEvents();
    }

    private void initView() {
        buttonInputUserInfo = (Button) findViewById(R.id.button_user_info);
        buttonBeaconDetection = (Button) findViewById(R.id.button_beacon_detection);
        buttonGPSTracking = (Button) findViewById(R.id.button_gps_tracking);
    }

    private void clickEvents() {
        buttonInputUserInfo.setOnClickListener(view -> {
            Log.d(TAG, "Input user data");
            Intent intentInputUserInfo = new Intent(this, UserInfoActivity.class);
            startActivity(intentInputUserInfo);
        });

        buttonBeaconDetection.setOnClickListener(view -> {
            Log.d(TAG, "Scan Beacon device");
            Intent intentBeaconDetection = new Intent(this, BeaconDetectionActivity.class);
            startActivity(intentBeaconDetection);
        });

        buttonGPSTracking.setOnClickListener(view -> {
            Log.d(TAG, "GPS tracking");
            Intent intentGPSTracking = new Intent(this, GPSTrackingActivity.class);
            startActivity(intentGPSTracking);
        });
    }
}