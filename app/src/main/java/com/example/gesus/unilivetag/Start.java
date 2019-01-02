package com.example.gesus.unilivetag;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Start extends Activity implements View.OnClickListener {

    Button btnStartGyroscope;
    Button btnStartAcceleration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        btnStartGyroscope = (Button) findViewById(R.id.btnStartGyroscope);
        btnStartGyroscope.setOnClickListener(this);
        btnStartAcceleration = (Button) findViewById(R.id.btnStartAcceleration);
        btnStartAcceleration.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){


        switch (view.getId()){
            case R.id.btnStartGyroscope:
                Intent intent = new Intent(this, Gyroscope.class);
                startActivity(intent);
                break;
            case R.id.btnStartAcceleration:
                Intent intent2 = new Intent(this, Acceleration.class);
                startActivity(intent2);
                break;
        }
        this.finish();

    }
}
