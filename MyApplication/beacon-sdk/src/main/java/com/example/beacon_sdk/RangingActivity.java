package com.example.beacon_sdk;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends Activity {
    protected static final String TAG = RangingActivity.class.getSimpleName();
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
    }

    @Override
    protected void onResume() {
        super.onResume();
        RangeNotifier rangeNotifier = (beacons, region) -> {
            if (beacons.size() > 0) {
                Log.d(TAG, "didRangeBeaconsInRegion called with beacon count:  "+beacons.size());
                Beacon firstBeacon = beacons.iterator().next();

                logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
            }
        };
        beaconManager.addRangeNotifier(rangeNotifier);
        beaconManager.startRangingBeacons(BeaconReferenceApplication.wildcardRegion);
    }

    @Override
    protected void onPause() {
        super.onPause();
        beaconManager.stopRangingBeacons(BeaconReferenceApplication.wildcardRegion);
        beaconManager.removeAllRangeNotifiers();
    }

    private void logToDisplay(final String line) {
        runOnUiThread(() -> {
            EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
            editText.append(line+"\n");
        });
    }
}
