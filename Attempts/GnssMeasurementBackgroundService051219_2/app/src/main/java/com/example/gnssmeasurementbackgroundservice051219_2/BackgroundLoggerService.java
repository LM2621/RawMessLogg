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
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Collection;

import static android.content.ContentValues.TAG;

//Kanske bättre med IntentService?
public class BackgroundLoggerService extends Service {

    private static final String LOG_TAG = BackgroundLoggerService.class.getSimpleName();
   // private static final String FILE_NAME = "RMLogg.txt";
   // private static String RM = "RMlogg";
    private static final String ERROR_WRITING_FILE = "Problem writing to file.";
    private static final int MIN_UPDATE_LOC_TIME_MS = 3000;
    private static final int MIN_UPDATE_LOC_DIST_M = 0;
    private  StringBuilder sb = new StringBuilder();
    private LocationManager lm;
    private LocationListener ll;
    FileOutputStream fos = null;


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

//        try {
//            fos=openFileOutput(FILE_NAME, MODE_PRIVATE);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }

        //Checking the availability state of the External Storage.
        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            //If it isn't mounted - we can't write into it.
            return;
        }
        //Create a new file that points to the root directory, with the given name:
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
       // File path = getExternalFilesDir(null);
        File file = new File(extStorageDirectory, "test343.txt");
        try {
            Log.d(TAG, "onCreate: nu skapar vi fil");
            file.createNewFile();
            //second argument of FileOutputStream constructor indicates whether
            //to append or create new file if one exists
            Log.d(TAG, "onCreate: nu skapar " + file.getAbsolutePath());
            fos  = new FileOutputStream(file, true);
            Log.d(TAG, "onCreate: vi ny fil skapad" +file.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
        startLocUpdates();
    }

    //This method gets called when the service is being destroyed, karl has chosen to clean up
    //and calls the removeUpdates method to his location manager.
    @Override
    public void onDestroy() {
        lm.removeUpdates(ll);
        Log.d(TAG, "stopService: vi kallar stopService");
        if(fos!=null){
            try {
                fos.close();
                Log.d(TAG, "stopService: vi kallar stopService och fos.close");
            } catch (IOException e) {
                e.printStackTrace();
        }
        } else{
            Log.d(TAG, "onDestroy: fos är noll");
        }
        super.onDestroy();
    }

    @Override
    public boolean stopService(Intent name) {

        return super.stopService(name);
    }

    //This is the method that gets called when we call startService()
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand: ");
        return android.app.Service.START_STICKY;
    }

    //This is the method that gets called when onCreate runs
    private boolean startLocUpdates() {
        lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

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
                        super.onGnssMeasurementsReceived(eventArgs);
                        //Varje measurement är alla measurements Max, det är en collection av alla measurements.
                        for (GnssMeasurement test : eventArgs.getMeasurements()){
                        int SatID = test.getSvid();
                        Log.d(LOG_TAG, "onGnssMeasurementsReceived: + SatID= " + SatID);
                        writeGnssMeasurement(test);
                        }
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

    public void writeGnssMeasurement(GnssMeasurement GnssMeasurement){
        int svid = GnssMeasurement.getSvid();
        //fos  = new FileOutputStream(file, true);
        try {
            fos.write(Integer.toString(svid).getBytes());

            //File dir= getFilesDir();
            //Log.d(TAG, "writeGnssMeasurement: Du kom hit 2 "+ dir);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
                    Log.d(TAG, "writeGnssMeasurement: vi har stängt");
            try {
                fos.close();
                Log.d(TAG, "writeGnssMeasurement: nu med");
            } catch (IOException e) {
                Log.d(TAG, "writeGnssMeasurement: vi har inte stängt");
                e.printStackTrace();
            }
        }
        }
        }


