package com.example.stepcount;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private TextView val;
    private int steps = 0;
    private boolean isSensorPresent;
    private SensorManager sm;
    private Sensor s;
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        val = (TextView) findViewById(R.id.step);
        val.setText(Integer.toString(steps));

        sm = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if (sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size() != 0) {
            s = sm.getSensorList(Sensor.TYPE_STEP_COUNTER).get(0);
            isSensorPresent = true;
        } else {
            isSensorPresent = false;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, PERMISSION_REQUEST_CODE);
            } else {
                startSensor();
            }
        } else {
            startSensor();
        }
    }

    private void startSensor() {
        if (isSensorPresent) {
            sm.registerListener(this, s, SensorManager.SENSOR_DELAY_NORMAL);
        } else {
            Toast.makeText(this, "Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startSensor();
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        startSensor();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isSensorPresent) {
            sm.unregisterListener(this);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
           float vals= event.values[0];
           steps=(int)vals;
            val.setText(Integer.toString(steps));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        sm = null;
        s = null;
    }
}
