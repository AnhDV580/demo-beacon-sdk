package com.example.beacon_sdk;

import android.content.Context;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class BeaconDetection {
    private static BeaconManager beaconManager;
    private static final String TAG = "BeaconDetection";
    private final String ALTBEACON_LAYOUT = "m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    public BeaconDetection() {

    }

    public static void getBeaconData(Context context) {
        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
//        beaconManager.addRangeNotifier((beacons, region) -> {
//            if (beacons != null && !beacons.isEmpty()) {
//                for (Beacon beacon: beacons) {
//                    String uuid = beacon.getId1().toString();
//                    String major = beacon.getId2().toString();
//                    String minor = beacon.getId3().toString();
//                    Log.d(TAG, "UUID: " + uuid);
//                    Log.d(TAG, "Major: " + major);
//                    Log.d(TAG, "Minor: " + minor);
//                }
//            }
//        });
//        beaconManager.startRangingBeacons(new Region("myRangingUniqueId", null, null, null));

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
                String uuid = region.getId1().toString();
                String major = region.getId2().toString();
                String minor = region.getId3().toString();
                Log.i(TAG, "I just saw an beacon for the first time!");
            }

            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });

        beaconManager.startMonitoring(new Region("myMonitoringUniqueId", null, null, null));

        Log.d(TAG, "Beacon data: 12345");
    }
}
