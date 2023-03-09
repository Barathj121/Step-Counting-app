package com.example.stepcount;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class stepactivity extends Activity implements SensorEventListener {
    private int steps=0;
    private boolean isSensorPresent;
    private SensorManager sm;
    private Sensor s;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sm=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        if(sm.getSensorList(Sensor.TYPE_STEP_COUNTER).size()!=0){
            s=sm.getSensorList(Sensor.TYPE_STEP_COUNTER).get(0);
            isSensorPresent=true;

        }
        else {
            isSensorPresent=false;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        if(isSensorPresent){
            sm.registerListener(this,s,SensorManager.SENSOR_DELAY_NORMAL);

        }
        else{
            Toast.makeText(this,"Sensor not found", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(isSensorPresent){
            sm.unregisterListener(this);
        }
    }


    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_STEP_DETECTOR) {
            steps++;
            TextView val=(TextView) findViewById(R.id.step);


            val.setText(steps);
        }
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        sm=null;
        s=null;
    }

    public  String getSteps() {

            return Integer.toString(steps) ;



    }
}