package com.example.beacon_sdk;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.BeaconParser;
import org.altbeacon.beacon.MonitorNotifier;
import org.altbeacon.beacon.Region;

public class BeaconDetection extends Activity  {
    private static BeaconManager beaconManager;
    private static final String TAG = "BeaconDetection";
    private final String ALTBEACON_LAYOUT = "m:2-3=beac,i:4-19,i:20-21,i:22-23,p:24-24,d:25-25";
    private static final String IBEACON_LAYOUT = "m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24";

    private static final int PERMISSION_REQUEST_FINE_LOCATION = 1;
    private static final int PERMISSION_REQUEST_BACKGROUND_LOCATION = 2;

    public static final Region wildcardRegion = new Region("wildcardRegion", null, null, null);
    public static boolean insideRegion = false;

    public BeaconDetection() {}

    public static void getBeaconData(Activity context) {
        beaconManager = BeaconManager.getInstanceForApplication(context);
        beaconManager.getBeaconParsers().clear();
        beaconManager.getBeaconParsers().add(new BeaconParser().
                setBeaconLayout(IBEACON_LAYOUT));
        beaconManager.setDebug(true);



        for (Region region: beaconManager.getMonitoredRegions()) {
            beaconManager.stopMonitoring(region);
        }
        beaconManager.startMonitoring(wildcardRegion);


        verifyBluetooth(context);
        requestPermissions(context);
        // No need to start monitoring here because we already did it in
        // BeaconReferenceApplication.onCreate
        // check if we are currently inside or outside of that region to update the display
        if (BeaconReferenceApplication.insideRegion) {
//            logToDisplay("Beacons are visible.");
        }
        else {
//            logToDisplay("No beacons are visible.");
        }


        //        beaconManager.startRangingBeacons(new Region("myRangingUniqueId", null, null, null));

        beaconManager.addMonitorNotifier(new MonitorNotifier() {
            @Override
            public void didEnterRegion(Region region) {
//                String uuid = region.getId1().toString();
//                String major = region.getId2().toString();
//                String minor = region.getId3().toString();
//                Log.i(TAG, "I just saw an beacon for the first time!");
                Log.d(TAG, "did enter region.");
                insideRegion = true;
                // Send a notification to the user whenever a Beacon
                // matching a Region (defined above) are first seen.
                Log.d(TAG, "Sending notification.");
//                sendNotification(context);
            }
//
            @Override
            public void didExitRegion(Region region) {
                Log.i(TAG, "I no longer see an beacon");
                insideRegion = false;
            }

            @Override
            public void didDetermineStateForRegion(int state, Region region) {
                Log.i(TAG, "I have just switched from seeing/not seeing beacons: "+state);
            }
        });



//        beaconManager.startMonitoring(new Region("myMonitoringUniqueId", null, null, null));


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
//
//        beaconManager.startMonitoring(new Region("myMonitoringUniqueId", null, null, null));

        Log.d(TAG, "Beacon data: 12345");
    }

    private static void requestPermissions(Activity context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (context.checkSelfPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                            != PackageManager.PERMISSION_GRANTED) {
                        if (!context.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("This app needs background location access");
                            builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @TargetApi(23)
                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                    context.requestPermissions(new String[]{Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                                            PERMISSION_REQUEST_BACKGROUND_LOCATION);
                                }

                            });
                            builder.show();
                        }
                        else {
                            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                            builder.setTitle("Functionality limited");
                            builder.setMessage("Since background location access has not been granted, this app will not be able to discover beacons in the background.  Please go to Settings -> Applications -> Permissions and grant background location access to this app.");
                            builder.setPositiveButton(android.R.string.ok, null);
                            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                                @Override
                                public void onDismiss(DialogInterface dialog) {
                                }

                            });
                            builder.show();
                        }
                    }
                }
            } else {
                if (!context.shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
                    context.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                            PERMISSION_REQUEST_FINE_LOCATION);
                }
                else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Functionality limited");
                    builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons.  Please go to Settings -> Applications -> Permissions and grant location access to this app.");
                    builder.setPositiveButton(android.R.string.ok, null);
                    builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                        @Override
                        public void onDismiss(DialogInterface dialog) {
                        }

                    });
                    builder.show();
                }

            }
        }
    }

    public void onRangingClicked(View view) {
        Intent myIntent = new Intent(this, RangingActivity.class);
        this.startActivity(myIntent);
    }
    public void onEnableClicked(View view) {
        // This is a toggle.  Each time we tap it, we start or stop
        Button button = (Button) findViewById(R.id.enableButton);
        if (BeaconManager.getInstanceForApplication(this).getMonitoredRegions().size() > 0) {
            BeaconManager.getInstanceForApplication(this).stopMonitoring(BeaconReferenceApplication.wildcardRegion);
            button.setText("Enable Monitoring");
        }
        else {
            BeaconManager.getInstanceForApplication(this).startMonitoring(BeaconReferenceApplication.wildcardRegion);
            button.setText("Disable Monitoring");
        }
    }

    private static void verifyBluetooth(Activity context) {
        try {
            if (!BeaconManager.getInstanceForApplication(context).checkAvailability()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Bluetooth not enabled");
                builder.setMessage("Please enable bluetooth in settings and restart this application.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
//                        finishAffinity();
                    }
                });
                builder.show();
            }
        }
        catch (RuntimeException e) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Bluetooth LE not available");
            builder.setMessage("Sorry, this device does not support Bluetooth LE.");
            builder.setPositiveButton(android.R.string.ok, null);
            builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                @Override
                public void onDismiss(DialogInterface dialog) {
//                    finishAffinity();
                }

            });
            builder.show();

        }

    }

    private String cumulativeLog = "";
    private void logToDisplay(String line) {
        cumulativeLog += line+"\n";
        runOnUiThread(new Runnable() {
            public void run() {
//                EditText editText = (EditText)MonitoringActivity.this
//                        .findViewById(R.id.monitoringText);
//                editText.setText(cumulativeLog);
            }
        });
    }


//    static private void sendNotification(Context context) {
//        NotificationManager notificationManager =
//                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//        Notification.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannel channel = new NotificationChannel("Beacon Reference Notifications",
//                    "Beacon Reference Notifications", NotificationManager.IMPORTANCE_HIGH);
//            channel.enableLights(true);
//            channel.enableVibration(true);
//            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
//            notificationManager.createNotificationChannel(channel);
//            builder = new Notification.Builder(this, channel.getId());
//        }
//        else {
//            builder = new Notification.Builder(this);
//            builder.setPriority(Notification.PRIORITY_HIGH);
//        }
//
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//        stackBuilder.addNextIntent(new Intent(context, MonitoringActivity.class));
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        builder.setSmallIcon(R.drawable.ic_launcher);
//        builder.setContentTitle("I detect a beacon");
//        builder.setContentText("Tap here to see details in the reference app");
//        builder.setContentIntent(resultPendingIntent);
//        notificationManager.notify(1, builder.build());
//    }
}
