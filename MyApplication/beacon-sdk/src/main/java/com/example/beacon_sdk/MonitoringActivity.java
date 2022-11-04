package com.example.beacon_sdk;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class MonitoringActivity extends AppCompatActivity {
    protected static final String TAG = "MonitoringActivity";
    private BeaconManager beaconManager;
    private String altBeaconLayout = "m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    private String iBeaconLayout = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        beaconManager = BeaconManager.getInstanceForApplication(this);
        // To detect proprietary beacons, you must add a line like below corresponding to your beacon
        // type.  Do a web search for "setBeaconLayout" to get the proper expression.
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(BeaconParser.ALTBEACON_LAYOUT));
//        beaconManager.addMonitorNotifier(new MonitorNotifier() {
//            @Override
//            public void didEnterRegion(Region region) {
////                String uuid = region.getId1().toString();
////                String major = region.getId2().toString();
////                String minor = region.getId3().toString();
//                Log.i(TAG, "I just saw an beacon for the first time!");
//            }
//
//            @Override
//            public void didExitRegion(Region region) {
//                Log.i(TAG, "I no longer see an beacon");
//            }
//
//            @Override
//            public void didDetermineStateForRegion(int state, Region region) {
//                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
//            }
//        });
//
//        beaconManager.startMonitoring(new Region("myMonitoringUniqueId", null, null, null));

        //
        beaconManager.addRangeNotifier((beacons, region) -> {
            if (beacons != null && !beacons.isEmpty()) {
                for (Beacon beacon: beacons) {
                    String uuid = beacon.getId1().toString();
                    String major = beacon.getId2().toString();
                    String minor = beacon.getId3().toString();
                }
            }
        });
        beaconManager.startRangingBeacons(new Region("myRangingUniqueId", null, null, null));

    }
}