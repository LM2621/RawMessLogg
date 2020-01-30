package com.example.rawmeasurementviewer;

import android.location.GnssClock;
import android.location.GnssMeasurementsEvent;
import android.location.GnssMeasurement;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.rawmeasurementview.R;

public class MainActivity extends AppCompatActivity {

    public String mStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int score = 2000;
        double measurement = 2;
        TextView helloWorld = (TextView) findViewById(R.id.helloWorldTextView);



        deleteBajs();
        String returnedName = hey();
        Log.v("Method", returnedName);

          helloWorld.setText(returnedName);
        //helloWorld.setText(mStream);

    }

    GnssMeasurementsEvent.Callback gnssCallback;

    public void deleteBajs() {
        Log.v("Method", "deleting bajs igen");
        gnssCallback = new GnssMeasurementsEvent.Callback() {
            @Override
            public void onGnssMeasurementsReceived(GnssMeasurementsEvent eventArgs) {
                super.onGnssMeasurementsReceived(eventArgs);

                GnssClock gnssClock = eventArgs.getClock();
                Log.v("Method2", "gnssClock");


                for (GnssMeasurement measurement : eventArgs.getMeasurements()) {
                   // int measState = measurement.getState();
                    int Svid = measurement.getSvid();
                    double CN0 = measurement.getCn0DbHz();

                    String mStream = String.format("%s,%s",Svid, CN0);
                    Log.v("Method3", "mStream");
                }
            }
        };
    }

    private String hey() {
        String name = "yo";
        deleteBajs();

        return name;
    }

}
