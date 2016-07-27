package com.example.recorder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AudioRecorder recorder=new AudioRecorder();
        final ImageButton recordButton=(ImageButton) findViewById(R.id.recordButton);
        recordButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction()==motionEvent.ACTION_DOWN)
                    recorder.startRecording();
                if(motionEvent.getAction()==motionEvent.ACTION_UP) {
                    recorder.stopRecording();
                }
                return false;
            }
        });
    }
}
