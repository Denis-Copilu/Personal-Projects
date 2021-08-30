package com.example.nannybot;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button btnOpenStoriesPage, btnOpenStreamVideoPage, btnControlRobot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setTitle("NannyBot");


        btnOpenStoriesPage =  findViewById(R.id.button);
        btnOpenStreamVideoPage = findViewById(R.id.buttonOpenStream);
        btnControlRobot = findViewById(R.id.buttonControlRobot);
        btnOpenStoriesPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlayerStoriesPage();
            }
        });
        btnOpenStreamVideoPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { openStreamVideoPage();}
        });

        btnControlRobot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {openControlRobot();
            }
        });
    }
    public void openPlayerStoriesPage(){
    Intent intent = new Intent(this, PlayerStories.class);
    startActivity(intent);
    }
    public void openStreamVideoPage()
    {
        Intent intent = new Intent(this, StreamVideo.class);
        startActivity(intent);
    }

    public void openControlRobot(){
        Intent intent = new Intent(this, ControlRobotBluetooth.class);
        startActivity(intent);
    }
//
}