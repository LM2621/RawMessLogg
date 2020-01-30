package com.example.gnssmeasurementbackgroundservice051219_2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GnssMeasurement;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;

//Kanske bättre med IntentService?
public class BackgroundLoggerService extends Service {

    private static final String LOG_TAG = BackgroundLoggerService.class.getSimpleName();
    private static final String FILE_NAME = "RMLogg.txt";
    private static String RM = "RMlogg";

    private static final int MIN_UPDATE_LOC_TIME_MS = 3000;
    private static final int MIN_UPDATE_LOC_DIST_M = 0;
    private  StringBuilder sb = new StringBuilder();

    private LocationManager lm;
    private LocationListener ll;



    public class BackGroundLogerServiceBinder extends Binder {
        // TODO: Anything?
    }

    public IBinder binder = new BackGroundLogerServiceBinder();

    //This one needs to be overridden for any service
    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //This one gets called even before onStartCommand, only gets called when service is created (not started, or recalled?)
    @Override
    public void onCreate() {
        Log.d(LOG_TAG, "onCreate() called");
        super.onCreate();

        FileOutputStream fos = null;
        try {
            fos=openFileOutput(FILE_NAME, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        startLocUpdates();
    }

    //This method gets called when the service is being destroyed, karl has chosen to clean up
    //and calls the removeUpdates method to his location manager.
    @Override
    public void onDestroy() {
        lm.removeUpdates(ll);
        super.onDestroy();
    }

    //This is the method that gets called when we call startService(), Karl chooses to do nothing?
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return android.app.Service.START_STICKY;
    }

    //This is the method that gets called when onCreate runs
    private boolean startLocUpdates() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        FileOutputStream fos = null;
        try {
            fos=openFileOutput(FILE_NAME, MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        ll = new LocationListener() {
            //Dessa 4 måste overridas (för att LocationListener är ett Interface?)
            @Override
            public void onLocationChanged(Location location) {
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };


        if (lm != null) {

            try {
                Log.d(LOG_TAG, "startLocUpdates(): setting up LM");
                lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                lm.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER, MIN_UPDATE_LOC_TIME_MS,
                        MIN_UPDATE_LOC_DIST_M, ll);

                lm.addNmeaListener(new OnNmeaMessageListener() {
                    @Override
                    public void onNmeaMessage(String message, long timestamp) {
                        Log.d(LOG_TAG, "Received NMEA msg: "+message);
                    }
                });

                lm.registerGnssMeasurementsCallback(new GnssMeasurementsEvent.Callback() {
                    @Override
                    public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {

                        //Log.d(LOG_TAG, "GNSS1 measurement received" + eventArgs);
                        super.onGnssMeasurementsReceived(eventArgs);

                        //Collection<GnssMeasurement> theshit =eventArgs.getMeasurements();
  //                      Log.d(LOG_TAG, "GNSS1 measurement received" + theshit);

                        for (GnssMeasurement measurement1 : eventArgs.getMeasurements()){
//                        Log.d(LOG_TAG, "GNSS1 measurement received" + measurement);
                            int Svid = measurement1.getSvid();
                            Log.d(LOG_TAG, "onGnssMeasurementsReceived: + Svid=" + Svid);
                        }
                        RM = Integer.toString(Svid);
                        fos.write()
                    }
                });

            }
            catch(SecurityException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;
    }
}
