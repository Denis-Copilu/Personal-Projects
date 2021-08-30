package com.example.nannybot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioRecord;
import android.media.AudioTrack;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class StreamVideo extends AppCompatActivity {
    WebView webStream;
    //
    private TextView textViewStatus;
    private EditText editTextGainFactor;
    private Button btnStartSendVoice,btnStopSendVoice;
    private boolean isActiveThread = false;
    ThreadAudio tAudio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stream_video);

        //code connection bluetooth
        //
        //BluetoothConnect.connect("16:07:54:CC:81:3A");
        //
        //code video streaming
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Supraveghere");
        btnStartSendVoice =  findViewById(R.id.start);
        btnStopSendVoice =  findViewById(R.id.stop);
        webStream = (WebView) findViewById(R.id.webStream);
        webStream.loadUrl("http://192.168.43.6/cam.mjpeg");
        //code send vocal message
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, PackageManager.PERMISSION_GRANTED);

        textViewStatus = findViewById(R.id.textViewStatus);
        btnStartSendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { buttonStart();}
        });
        btnStopSendVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { buttonStop();}
        });
        //-------------------------------------------------------

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            webStream.destroy();
            //BluetoothConnect.disconnect();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK))
        {
            //onBackPressed();
            webStream.destroy();
            //BluetoothConnect.disconnect();
        }
        return super.onKeyDown(keyCode, event);
    }
    public void buttonStart(){
        tAudio = new ThreadAudio();
        textViewStatus.setText("Activ");
        //System.out.println(Thread.currentThread());
    }

    public void buttonStop(){
        tAudio.stop();
        textViewStatus.setText("Inactiv");
    }
}