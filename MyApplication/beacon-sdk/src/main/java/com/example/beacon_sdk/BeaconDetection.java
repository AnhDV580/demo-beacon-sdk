package com.example.beacon_sdk;

import android.content.Context;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;

public class BeaconDetection {
    private static BeaconManager beaconManager;
    private static String TAG = "BeaconDetection";

    public BeaconDetection() {

    }

    public static void getBeaconData(Context context) {
        beaconManager = BeaconManager.getInstanceForApplication(context);

        Log.d(TAG, "Beacon data: 12345");
    }
}
