package com.example.nannybot;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.webkit.WebView;
import android.widget.Button;

import java.io.IOException;
import java.io.OutputStream;

public class ControlRobotBluetooth extends AppCompatActivity {
    private OutputStream outputStream;
    Button btnForward, btnBackward, btnLeft, btnRight;
    WebView webStream;
    String command;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_robot_bluetooth);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Control robot");
        btnForward = (Button) findViewById(R.id.forward);
        btnBackward = (Button) findViewById(R.id.backward);
        btnLeft = (Button) findViewById(R.id.left);
        btnRight = (Button) findViewById(R.id.right);
        webStream = (WebView) findViewById(R.id.webStream);
        webStream.loadUrl("http://192.168.43.6/cam.mjpeg");

        btnForward.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                command = "d";

                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                command = "10";
                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
            }
            }
            return false;
        });
        btnBackward.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                command = "u";

                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                command = "10";
                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
        btnLeft.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                command = "l";

                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                command = "10";
                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
        btnRight.setOnTouchListener((v, event) -> {
            if(event.getAction() == MotionEvent.ACTION_DOWN)
            {
                command = "r";

                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else if(event.getAction() == MotionEvent.ACTION_UP)
            {
                command = "10";
                try {
                    outputStream.write(command.getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return false;
        });
        //BluetoothConnect.disconnect();
        BluetoothConnect.connect("00:19:10:08:47:80");
        if(BluetoothConnect.connected){
            try {
                outputStream = BluetoothConnect.getBtSocket().getOutputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
            webStream.destroy();
            BluetoothConnect.disconnect();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if((keyCode == KeyEvent.KEYCODE_BACK))
        {
            //onBackPressed();
            webStream.destroy();
            BluetoothConnect.disconnect();
        }
        return super.onKeyDown(keyCode, event);
    }
}