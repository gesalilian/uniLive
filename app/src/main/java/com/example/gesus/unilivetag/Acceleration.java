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
import android.os.Handler;
import android.os.VibrationEffect;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.Vibrator;
import android.widget.Toast;

import java.io.IOException;

import be.tarsos.dsp.AudioDispatcher;
import be.tarsos.dsp.AudioEvent;
import be.tarsos.dsp.AudioProcessor;
import be.tarsos.dsp.io.android.AudioDispatcherFactory;
import be.tarsos.dsp.pitch.PitchDetectionHandler;
import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.PitchProcessor;

public class Acceleration extends Activity implements SensorEventListener, View.OnClickListener{

    Button btnBack;
    TextView txtAccX, txtAccY, txtAccZ, txtOutput;
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
        setContentView(R.layout.activity_acceleration);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        btnBack = (Button) findViewById(R.id.btnBack);
        btnBack.setOnClickListener(this);
        txtAccX = (TextView) findViewById(R.id.txtAccX);
        txtAccY = (TextView) findViewById(R.id.txtAccY);
        txtAccZ = (TextView) findViewById(R.id.txtAccZ);
        txtOutput = (TextView) findViewById(R.id.txtOutput);
        frequence();
    }



    @Override
    public void onSensorChanged(SensorEvent event) {

        //die Werte des Beschleunigungssensors, um die Rotation des Handys zu ermitteln
        accX = event.values[0];
        accY = event.values[1];
        accZ = event.values[2];


        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        String OutputString = "else";
        String flashLightOff = "flashLightOff";
        String flashLightOn = "flashLightOn";
        String soundOff = "soundOff";
        String soundOn = "soundOn";



        //Das Handy kann verschiedene Aktionen ausführen:
        //Vibration
        //  anschalten:     v.vibrate(100); ->die Zahl in Klammern gibt die Zeit in Millisekunden an
        //  ausschalten:    v.cancel();
        //
        //Sound
        //  anschalten:     sound(soundOn);
        //  ausschalten:    sound(soundOff);
        //
        //Licht
        //  anschalten:     flashLight(flashLightOn);
        //  ausschalten:    flashLight(flashLightOff);
        //
        //Frequenzanzeige
        //  die Frequenz eines Tons wird auf dem Handy angezeigt. Außerdem werden die Werte in der Variablen "frequenz" gespeichert
        //  Wenn du diese Frequenz nutzen möchtest, nutze diese Variable zum Beispiel in einer "if-Anweisung"
        //
        //
        //Ab hier kannst du die Funktionen nach belieben einfügen
        //Folgende Abschnitte geben an, in welche Richtung das Display zeigt (mithilfe der ermittelten Werte des Beschleunigungssensors)
        //Ab hier kannst du die oben stehenden Funktionen nach belieben einfügen
        //
        if (accZ>=9 && accZ<=11){
            //Was soll passieren, wenn der Bildschirm nach oben zeigt?
            OutputString = "oben";

        } else if (accY>=9 && accY<=11){
            //Was soll passieren, wenn der Bildschirm nach vorne zeigt?
            OutputString = "vorne";

        } else if (accZ>=-11 && accZ<=-8) {
            //Was soll passieren, wenn der Bildschirm nach unten zeigt?
            OutputString = "unten";

        } else if (accY>=-11 && accY<=-8) {
            //Was soll passieren, wenn der Bildschirm nach hinten zeigt?
            OutputString = "hinten";

        } else if (accX>=-11 && accX<=-8) {
            //Was soll passieren, wenn der Bildschirm nach rechts zeigt?
            OutputString = "rechts";

        } else if (accX>=9 && accX<=11) {
            //Was soll passieren, wenn der Bildschirm nach links zeigt?
            OutputString = "links";


        } else{
            //Was soll passieren, wenn keiner der vorher genannten Fälle auftritt?
        }
        txtOutput.setText(OutputString);
    }







    //
    //hier kommen die Funktionen, die für den Sound, das Licht und die Frequenzanzeige verantwortlich sind.
    // hier muss nichts mehr geändert werden
    //

    //Schaltet den Sound an und aus
    public void sound (String s){
        txtAccX.setText("Sound: " + s);
        if (s == "soundOn"){
            sound= MediaPlayer.create(Acceleration.this,R.raw.tap);
            try {
                if (sound.isPlaying()) {
                    sound.stop();
                    sound.release();
                    sound= MediaPlayer.create(Acceleration.this,R.raw.tap);
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
    public void flashLight(String s) {
        txtAccY.setText("Licht: "+ s);
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
                        txtAccZ.setText("Frequenz in Hz: " + Float.toString(pitchInHz));
                    }
                });
            }
        };

        audioP = new PitchProcessor(PitchProcessor.PitchEstimationAlgorithm.FFT_YIN, 22050, 1024, pdh);
        dispatcher.addAudioProcessor(audioP);
        new Thread(dispatcher, "Audio Dispatcher").start();

    }








    //
    //hier kommen die benötigten Funktionen, die für eine App wichtig sind. hier muss nichts mehr geändert werden
    //


    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, Start.class);
        startActivity(intent);
        this.finish();
    }

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
