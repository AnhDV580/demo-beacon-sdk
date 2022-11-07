package com.example.beacon_sdk;

import android.content.Context;
import android.util.Log;

import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;

public class BeaconSDK {
    protected static final String TAG = BeaconSDK.class.getName();
    private BeaconManager beaconManager;

    private Context context;
    private BeaconCallback beaconCallback;

    public BeaconSDK(Context context, BeaconCallback beaconCallback) {
        this.context = context;
        this.beaconCallback = beaconCallback;
        beaconManager = BeaconManager.getInstanceForApplication(context);
    }

    public void beaconOnResume() {
        RangeNotifier rangeNotifier = (beacons, region) -> {
            Log.i(TAG, "Beacon: " + beacons.toString() + ", region: " + region.toString());
            beaconCallback.onListener(beacons, region);
        };
        beaconManager.addRangeNotifier(rangeNotifier);
        beaconManager.startRangingBeacons(BeaconReferenceApplication.wildcardRegion);
    }

    public void beaconOnPause() {
        beaconManager.stopRangingBeacons(BeaconReferenceApplication.wildcardRegion);
        beaconManager.removeAllRangeNotifiers();
    }
}
