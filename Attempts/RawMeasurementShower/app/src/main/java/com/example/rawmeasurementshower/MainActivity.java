package com.example.rawmeasurementshower;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.location.GnssMeasurementsEvent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.OnNmeaMessageListener;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private LocationManager lm;
    private LocationListener ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startLocUpdates();
    }

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
                        Log.d(LOG_TAG, "Received NMEA msg: " + message);
                    }
                });

                lm.registerGnssMeasurementsCallback(new GnssMeasurementsEvent.Callback() {
                    @Override
                    public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                        Log.d(LOG_TAG, "GNSS measurement received" + eventArgs);
                        super.onGnssMeasurementsReceived(eventArgs);

                    }
                });

            } catch (SecurityException e) {
                e.printStackTrace();
                return false;
            }
        }

        return true;

    }

}
