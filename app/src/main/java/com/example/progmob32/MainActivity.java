package com.example.progmob32;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private MediaRecorder mr = null;
    private MediaPlayer mp = null;
    private String outputFile = null;
    private boolean currentlyRecording = false;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;

    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);

        outputFile = getExternalCacheDir().getAbsolutePath();
        outputFile += "/recording.mpeg4";
    }

    /**
     * Initialize and define new media recorder instance onclick
     * @param v
     */
    public void startRecording(View v){
        currentlyRecording = true;

        mr = new MediaRecorder();
        mr.setAudioSource(MediaRecorder.AudioSource.MIC);
        mr.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mr.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mr.setOutputFile(outputFile);

        try {
            mr.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }
        mr.start();
        Toast.makeText(getApplicationContext(), "Now Recording", Toast.LENGTH_LONG).show();
    }

    /**
     * Stop recording onclick
     * @param v
     */
    public void stopRecording(View v) {
        if(currentlyRecording){
            mr.stop();
            mr.reset();
            mr.release();
            currentlyRecording = false;
            mr = null;
            Toast.makeText(getApplicationContext(), "Recording Stopped", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Play back recording onclick
     * @param v
     */
    public void playRecording(View v){
        mp = new MediaPlayer();
        try {
            mp.setDataSource(outputFile);
            mp.prepare();
            mp.start();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "No Recording Available", Toast.LENGTH_LONG).show();
        }
    }
}