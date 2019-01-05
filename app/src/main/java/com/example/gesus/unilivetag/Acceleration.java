package com.example.gesus.unilivetag;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

public class Acceleration extends Activity implements SensorEventListener, View.OnClickListener{
//   TODO: kommentare einfügen auf deutsch!! erst wieder sensor abfragen wenn zu ende? oder mit case arbeiten?
    Button btnBack;
    TextView txtAccX, txtAccY, txtAccZ, txtOutput;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float accX = 0;
    float accY = 0;
    float accZ = 0;
    //private Camera camera;
    private boolean isFlashOn;
    private boolean hasFlash;
    Camera.Parameters p;
    MediaPlayer sound;
    public static Camera cam = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acceleration);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        txtAccX = (TextView) findViewById(R.id.txtAccX);
        txtAccY = (TextView) findViewById(R.id.txtAccY);
        txtAccZ = (TextView) findViewById(R.id.txtAccZ);
        txtOutput = (TextView) findViewById(R.id.txtOutput);
    }

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
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
    public void onSensorChanged(SensorEvent event) {

        //die Werte des Beschleunigungssensors
        accX = event.values[0];
        txtAccX.setText("Orientation X:"+ Float.toString(accX));
        accY = event.values[1];
        txtAccY.setText("Orientation Y:"+ Float.toString(accY));
        accZ = event.values[2];
        txtAccZ.setText("Orientation Z:"+ Float.toString(accZ));

//        MediaPlayer sound= MediaPlayer.create(Acceleration.this,R.raw.sound);
        //https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        String OutputString = "";
        if (accZ>=9 && accZ<=11){
            v.cancel();
//            sound.stop();
            OutputString = "obenoben";
            flashLightOn(OutputString);
            soundOn(OutputString);

            v.vibrate(500);
        } else if (accY>=9 && accY<=11){
            v.cancel();
            OutputString = "vorne";
            flashLightOn(OutputString);
            soundOn(OutputString);

            // Start without a delay
            // Vibrate for 100 milliseconds
            // Sleep for 1000 milliseconds
            long[] pattern = {0, 100, 10000};

            // The '0' here means to repeat indefinitely
            // '0' is actually the index at which the pattern keeps repeating from (the start)
            // To repeat the pattern from any other point, you could increase the index, e.g. '1'
            v.vibrate(pattern, 0);
        } else if (accZ>=-11 && accZ<=-8) {
            v.cancel();
            OutputString = "unten";
//            sound.start();
            flashLightOn(OutputString);
            soundOn(OutputString);

        } else if (accY>=-11 && accY<=-8) {
            v.cancel();
            OutputString = "hinten";
            flashLightOn(OutputString);
            soundOn(OutputString);

            long[] pattern = {0, 100, 1000};
            v.vibrate(pattern, -1);
        } else if (accX>=-11 && accX<=-8) {
            v.cancel();
            OutputString = "rechts";
            flashLightOn(OutputString);
            soundOn(OutputString);

        } else if (accX>=9 && accX<=11) {
            v.cancel();
            OutputString = "links";
            soundOn(OutputString);
            flashLightOn(OutputString);


        } else{
            v.cancel();
            OutputString="else";
            soundOn(OutputString);
            flashLightOn(OutputString);
        }
        txtOutput.setText(OutputString);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do nothing.
    }


    public void soundOn (String s){
        sound= MediaPlayer.create(Acceleration.this,R.raw.tap);
        if (s == "unten"){
            sound.start();
            sound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.reset();
                    mp.release();
                }
            });
        }else{
            if(sound!=null) {
                if(sound.isPlaying())
                    sound.stop();
                sound.reset();
                sound.release();
                sound=null;
            }
        }
    }
    //https://stackoverflow.com/questions/6068803/how-to-turn-on-front-flash-light-programmatically-in-android
    public void flashLightOn(String s) {
        String OutputString = "";
        if (s == "links") {
            OutputString = "turnOnFlash";
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String cameraId = null; // Usually back camera is at 0 position.
                try {
                    cameraId = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(cameraId, true);   //Turn ON
                } catch (CameraAccessException e) {
                    Toast.makeText(getBaseContext(), "Exception flashLightOn-links()",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                CameraManager camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                String cameraId = null; // Usually back camera is at 0 position.
                try {
                    cameraId = camManager.getCameraIdList()[0];
                    camManager.setTorchMode(cameraId, false);   //Turn OFF
                } catch (CameraAccessException e) {
                    Toast.makeText(getBaseContext(), "Exception flashLightOn-links()",
                            Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
            }
        }
    }


}
