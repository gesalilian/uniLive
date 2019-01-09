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

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class Gyroscope extends Activity implements SensorEventListener, View.OnClickListener{

    Button btnBack;
    TextView txtGyroX, txtGyroY, txtGyroZ, txtOutput;
    private SensorManager mSensorManager;
    private Sensor mSensor;
    float accX = 0;
    float accY = 0;
    float accZ = 0;
    float frequenz = 0;
    Camera.Parameters p;
    MediaPlayer sound;
    AudioDispatcher dispatcher;
    AudioProcessor audioP;
    PitchDetectionHandler pdh;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyroscope);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        txtGyroX = (TextView) findViewById(R.id.txtGyroX);
        txtGyroY = (TextView) findViewById(R.id.txtGyroY);
        txtGyroZ = (TextView) findViewById(R.id.txtGyroZ);
        txtOutput = (TextView) findViewById(R.id.txtOutput);
        frequence();
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        //die Werte des Beschleunigungssensors, um die Rotation des Handys zu ermitteln
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];

        //https://stackoverflow.com/questions/13950338/how-to-make-an-android-device-vibrate
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        String OutputString = "else";
        String flashLightOff = "flashLightOff";
        String flashLightOn = "flashLightOn";
        String soundOff = "soundOff";
        String soundOn = "soundOn";

        //Folgende Werte geben an in welche Richtung das Display zeigt (mithilfe der ermittelten Werte des Beschleunigungssensors
        if (accZ>=9 && accZ<=11){
            v.cancel();
            OutputString = "oben";
            sound(soundOff);
            flashLight(flashLightOff);
            if (frequenz<500 && frequenz>300){
                v.vibrate(100);
            }

        } else if (accY>=9 && accY<=11){
            v.cancel();
            OutputString = "vorne";
            sound(soundOff);
            flashLight(flashLightOn);

        } else if (accZ>=-11 && accZ<=-8) {
            v.cancel();
            OutputString = "unten";
            flashLight(flashLightOff);
            sound(soundOn);

        } else if (accY>=-11 && accY<=-8) {
            v.cancel();
            OutputString = "hinten";
            sound(soundOff);
            flashLight(flashLightOff);

        } else if (accX>=-11 && accX<=-8) {
            v.cancel();
            OutputString = "rechts";
            flashLight(flashLightOff);
            sound(soundOff);

        } else if (accX>=9 && accX<=11) {
            v.cancel();
            OutputString = "links";
            sound(soundOff);
            flashLight(flashLightOff);
            v.vibrate(100);


        } else{
            v.cancel();
            sound(soundOff);
            flashLight(flashLightOff);
        }
        txtOutput.setText(OutputString);
    }



    //Schaltet den Sound an und aus
    public void sound (String s){
        txtGyroX.setText("Sound: " + s);
        if (s == "soundOn"){
            sound= MediaPlayer.create(Gyroscope.this,R.raw.tap);
            try {
                if (sound.isPlaying()) {
                    sound.stop();
                    sound.release();
                    sound= MediaPlayer.create(Gyroscope.this,R.raw.tap);
                }
                sound.start();
            } catch (Exception e) {
                Toast.makeText(getBaseContext(), "Exception Sound()",
                        Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    //Schaltet das Kameralicht an und aus
    //https://stackoverflow.com/questions/6068803/how-to-turn-on-front-flash-light-programmatically-in-android
    public void flashLight(String s) {
        txtGyroY.setText("Licht: "+ s);
        if (s == "flashLightOn") {
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

    //Zeigt die Frquenz der Töne an, die das Handy wahrnimmt
    public void frequence(){
        dispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050, 1024, 0);
        pdh = new PitchDetectionHandler() {
            @Override
            public void handlePitch(PitchDetectionResult result, AudioEvent e) {
                final float pitchInHz = result.getPitch();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        frequenz = pitchInHz;
                        txtGyroZ.setText("Frequenz in Hz: " + Float.toString(pitchInHz));
                    }
                });
            }
        };
        audioP = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(audioP);
        new Thread(dispatcher, "Audio Dispatcher").start();

    }








    //
    //ab hier muss nichts mehr geändert werden
    //


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
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //Do nothing.
    }


}


// Klatscherkennung
//
//        https://stackoverflow.com/questions/36971839/tarsosdsp-clap-detection
//        mDispatcher = AudioDispatcherFactory.fromDefaultMicrophone(22050,1024,0);
//
//        double threshold = 8;
//        double sensitivity = 20;
//
//        mPercussionDetector = new PercussionOnsetDetector(22050, 1024,
//                new OnsetHandler() {
//
//                    @Override
//                    public void handleOnset(double time, double salience) {
//                        Log.d(TAG, "Clap detected!");
//                        txtGyroY.setText("Clap Detected!");
//                    }
//                }, sensitivity, threshold);
//
//        mDispatcher.addAudioProcessor(mPercussionDetector);
//        new Thread(mDispatcher,"Audio Dispatcher").start();