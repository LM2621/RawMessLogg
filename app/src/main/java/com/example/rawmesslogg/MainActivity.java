package com.example.rawmesslogg;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.content.ContentValues.TAG;

public class MainActivity extends AppCompatActivity {

    Switch aSwitch;
    private static final String LOG_TAG = LoggerService.class.getSimpleName();
    private String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActivityCompat.requestPermissions(this, permissions, 1);

        aSwitch = findViewById(R.id.toggle_service_on_off);
        aSwitch.setChecked(false);
        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (aSwitch.isChecked()) {
                    startLoggerService();
                } else {
                    stopLoggerService();
                }
            }
        });

        String state = Environment.getExternalStorageState();
        if (!Environment.MEDIA_MOUNTED.equals(state)) {
            return;
        }
/*
        String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
        Log.d(TAG, "onCreate: "+extStorageDirectory);
        File file = new File(extStorageDirectory, "test69.txt");

        try {
            FileOutputStream fileOutputStream = openFileOutput("test69.txt", Context.MODE_PRIVATE);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Log.d(LOG_TAG, "onCreate: vi ny fil skapad" + file.getAbsolutePath());
    }
*/
    }
    public void startLoggerService(){
    Intent intent = new Intent(this, LoggerService.class);
        Log.d(LOG_TAG, "startLoggerService: Vi kallar på vår logger service");
        startService(intent);
    }

    public void stopLoggerService(){
    Intent intent = new Intent(this, LoggerService.class);
    stopService(intent);
    }

    public boolean getPermissions(){
        for (String permission:permissions) {
          if(  ActivityCompat.checkSelfPermission( this,permission) != PackageManager.PERMISSION_GRANTED){
              Log.d(LOG_TAG, "getPermissions: Vi har inte alla permissions ");
          };
            ActivityCompat.requestPermissions(this, permissions, 1);
        }

        Toast.makeText(this, "We got these permissions" , Toast.LENGTH_SHORT).show();


        return true;
    }

    public void getPermissions2(){
        for (String permission : permissions) {
            if(  ActivityCompat.checkSelfPermission( this,permission) != PackageManager.PERMISSION_GRANTED){
                Log.d(LOG_TAG, "getPermissions: Vi har inte denna permission " + permission);
            } else {
                Log.d(LOG_TAG, "getPermissions: Vi har denna:" + permission);
            }
        }
        ActivityCompat.requestPermissions(this, permissions, 1);
        Toast.makeText(this, "We got these permissions" , Toast.LENGTH_SHORT).show();
    }

}
