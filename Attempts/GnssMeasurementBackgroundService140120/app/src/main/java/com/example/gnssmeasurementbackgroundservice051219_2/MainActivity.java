package com.example.gnssmeasurementbackgroundservice051219_2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Switch tmpToggleSwitch;
    private static final String LOG_TAG = BackgroundLoggerService.class.getSimpleName();
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
           };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getPermission();

        tmpToggleSwitch = findViewById(R.id.toggle_service_on_off);
        tmpToggleSwitch.setChecked(false);
        tmpToggleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (tmpToggleSwitch.isChecked()) {
                    //MAXMAXMAX, om någon klickar på switchen så startas servicen.
                    Log.d(LOG_TAG, "onCheckedChanged:  vi kallar startbackgroundservice");
                    startBackgroundLoggerService();
                } else {
                    Log.d(LOG_TAG, "onCheckedChanged:  vi kallar stopbackgroundservice");
                    stopBackgroundLoggerService();
                }
            }
        });
    }

    private void startBackgroundLoggerService() {
        Intent intent = new Intent(this, BackgroundLoggerService.class);
        startService(intent);
        // Toast.makeText(getActivity(), "Service started", Toast.LENGTH_SHORT).show();
    }

    private void stopBackgroundLoggerService() {
        Intent intent = new Intent(this, BackgroundLoggerService.class);
//        stopService(new Intent(getActivity(), BackgroundLoggerService.class));
        Log.d(LOG_TAG, "stopBackgroundLoggerService: din service har slutat");
        stopService(intent);
        // Toast.makeText(getActivity(), "Service stopped", Toast.LENGTH_SHORT).show();
    }

    public void getPermission() {
        ActivityCompat.requestPermissions(this,
                permissions, 1);
    }
}
    /*
    public void getPermission1() {
        //Här kollar vi om vi har permission till fine location

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            // Och fråga användaren om permission:
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }}

        public void getPermission2() {
            //Här kollar vi om vi har permission till external storage
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Och fråga användaren om permission:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
            }
/*
            //Här kollar vi om vi har permission till external storage
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                // Ifall vi inte hade permission så skall vi visa användaren varför vi vill ha det:
                if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Write to external storage access is required",
                            Toast.LENGTH_SHORT).show();
                }

                // Och fråga användaren om permission:
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 3);
            }
            */



/*
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        Log.d(null, "onRequestPermissionResult() called");
        if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            Log.d(null, "GRANTED");
        } else {
            Log.d(null, "NOT GRANTED");
        }
        return;

    }
*/


