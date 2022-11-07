package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.beacon_sdk.BeaconCallback;
import com.example.beacon_sdk.BeaconSDK;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class BeaconDetectionActivity extends AppCompatActivity implements BeaconCallback {
    private BeaconSDK beaconSDK;
    private static final String TAG = BeaconDetectionActivity.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beacon_detection);
        beaconSDK = new BeaconSDK(this, this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        beaconSDK.beaconOnResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconSDK.beaconOnPause();
    }

    @Override
    public void onListener(Collection<Beacon> beacons, Region region) {
        if (beacons.size() > 0) {
            Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  " + beacons.size());
            Beacon firstBeacon = beacons.iterator().next();
            for (Beacon beacon: beacons) {
                String uuid = beacon.getId1().toString();
                String major = beacon.getId2().toString();
                String minor = beacon.getId3().toString();
                Log.d(TAG, "uuid:  " + uuid + ", major:  " + major + ", minor:  " + minor);
            }
        }
    }
}