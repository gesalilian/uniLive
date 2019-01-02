package com.example.gesus.unilivetag;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Gyroscope extends Activity implements SensorEventListener, View.OnClickListener{

    Button btnBack;
    TextView txtGyroX, txtGyroY, txtGyroZ, txtOutput;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float gyroX = 0;
    float gyroY = 0;
    float gyroZ = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        txtGyroX = (TextView) findViewById(R.id.txtGyroX);
        txtGyroY = (TextView) findViewById(R.id.txtGyroY);
        txtGyroZ = (TextView) findViewById(R.id.txtGyroZ);
        txtOutput = (TextView) findViewById(R.id.txtOutput);
    }

    //Back-Button
    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Start.class);
        startActivity(intent);
        this.finish();
    }

    //http://www.41post.com/3745/programming/android-acessing-the-gyroscope-sensor-for-simple-applications
    //when this Activity starts
    @Override
    protected void onResume()
    {
        super.onResume();
        /*register the sensor listener to listen to the gyroscope sensor, use the
        callbacks defined in this class, and gather the sensor information as quick
        as possible*/
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_FASTEST);
    }

    //When this Activity isn't visible anymore
    @Override
    protected void onStop()
    {
        //unregister the sensor listener
        mSensorManager.unregisterListener(this);
        super.onStop();
    }

    @Override
    public void onAccuracyChanged(Sensor arg0, int arg1)
    {
        //Do nothing.
    }

    //Prints the gyroscope values
    @Override
    public void onSensorChanged(SensorEvent event)
    {
        //if sensor is unreliable, return void
        if (event.accuracy == SensorManager.SENSOR_STATUS_UNRELIABLE)
        {
            return;
        }

        //else it will output the Roll, Pitch and Yawn values
        gyroX += event.values[0];
        txtGyroX.setText("Orientation X:"+ Float.toString(gyroX));
        gyroY += event.values[1];
        txtGyroY.setText("Orientation Y:"+ Float.toString(gyroY));
        gyroZ += event.values[2];
        txtGyroZ.setText("Orientation Z:"+ Float.toString(gyroZ));

        String OutputString = "";
//        switch (Float.toString(wuerfel)){
//            case (Float.toString(0-100)):  XString = "oben";
//                        break;
//            case 101-200: XString = "unten";
//
//        }
        if (gyroX>=-300 && gyroX<=300 && gyroY>=-300 && gyroY<=300){
            OutputString = "oben";
        } else if (gyroX>=300 && gyroX<=900 && gyroZ>=-300 && gyroZ<=300){
            OutputString = "vorne";
        } else if (gyroX>=900 && gyroX<=1500 && gyroY>=-300 && gyroZ<=300) {
            OutputString = "unten";
        } else if (gyroX>=-900 && gyroX<=-300 && gyroZ>=-300 && gyroZ<=300) {
            OutputString = "hinten";
        } else if (gyroY>=300 && gyroY<=900 && gyroZ>=-300 && gyroZ<=300) {
            OutputString = "rechts";
        } else if (gyroY>=-900 && gyroY<=-300 && gyroZ>=-300 && gyroZ<=300) {
            OutputString = "links";
        }
        txtOutput.setText(OutputString);
    }


}
