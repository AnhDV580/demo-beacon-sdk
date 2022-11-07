package com.example.beacon_sdk;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public interface BeaconCallback {
    void onListener(Collection<Beacon> beacons, Region region);
}
