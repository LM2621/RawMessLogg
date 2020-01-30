package com.example.rawmesslogg;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import androidx.annotation.Nullable;

import static android.content.ContentValues.TAG;

public class LoggerService extends Service {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private static final int MIN_UPDATE_LOC_TIME_MS = 2000;
    private static final int MIN_UPDATE_LOC_DIST_M = 0;
    private static final String LOG_TAG = LoggerService.class.getSimpleName();
    FileOutputStream fileOutputStream = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        startMessLogg();
        Log.d(TAG, "onCreate: här");

        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        Log.d(TAG, "LoggerService: "+extStorageDirectory);
//        File file = new File(extStorageDirectory, "test69.txt");

        File sdcard = Environment.getExternalStorageDirectory();
        File dir = new File(sdcard.getAbsolutePath() + "/RawMessLogg/");
        dir.mkdir();
        File file = new File(dir, "sample69.txt");
        Log.d(TAG, "onCreate: Filen här" + file.getAbsolutePath());
        try {
            fileOutputStream = new FileOutputStream(file);
//        fileOutputStream = openFileOutput("test69.txt", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();

        }
    }

    @Override
    public void onDestroy() {

        super.onDestroy();

        if(fileOutputStream!=null){
        try {
            fileOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } } else {
        Log.d(TAG, "onDestroy: fileOutputStream är stängd");
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void startMessLogg() {

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {

            }

            @Override
            public void onStatusChanged(String s, int i, Bundle bundle) {

            }

            @Override
            public void onProviderEnabled(String s) {

            }

            @Override
            public void onProviderDisabled(String s) {

            }
        };

        if (locationManager != null) {

            try {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,
                        MIN_UPDATE_LOC_TIME_MS,
                        MIN_UPDATE_LOC_DIST_M,
                        locationListener);

                locationManager.registerGnssMeasurementsCallback(new GnssMeasurementsEvent.Callback() {
                    @Override
                    public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                        super.onGnssMeasurementsReceived(eventArgs);
                        for (GnssMeasurement event : eventArgs.getMeasurements()){
                            int svid = event.getSvid();
                            Log.d(LOG_TAG, "onGnssMeasurementsReceived: + SatID= " + svid);
                            if(fileOutputStream!=null){
                            try {
                                fileOutputStream.write(Integer.toString(svid).getBytes());
                                fileOutputStream.write("\n".getBytes());
                                Log.d(TAG, "onGnssMeasurementsReceived: vi bör ha skrivit till filen");
                            } catch (IOException e) {
                                Log.d(TAG, "onGnssMeasurementsReceived: vi kunde inte skriva till filen");
                                e.printStackTrace();
                            }
                        }}
                    }
                });
        } finally {
        }
    }
    };

}
