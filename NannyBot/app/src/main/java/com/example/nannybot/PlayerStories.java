package com.example.nannybot;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;

public class PlayerStories extends AppCompatActivity {

    static MediaPlayer myMediaPlayer;
    int position;
    SeekBar storySeekBar;
    ArrayList<File> myStories;
    Thread updateSeekBar;
    Button btn_next, btn_previous,btn_pause;
    TextView storyTextLabel,storyDuration;
    String sname;
    //
    ListView storiesListView;
    String[] items;
    int pos=0;
    //
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stories2);
        //
        storiesListView = (ListView) findViewById(R.id.storiesListView);

        storyTextLabel = (TextView)findViewById(R.id.storyLabel);
        storyDuration = (TextView)findViewById(R.id.storyDurationLabel);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Povești");

        btn_next = (Button)findViewById(R.id.next);
        btn_previous = (Button)findViewById(R.id.previous);
        btn_pause = (Button)findViewById(R.id.pause);

        storySeekBar = (SeekBar) findViewById(R.id.seekBar);

//        audioArrayList = new ArrayList<>();

        updateSeekBar = new Thread(){
            @Override
            public void run() {

                int totalDuration = myMediaPlayer.getDuration();
                int currentPosition = 0;
                while (currentPosition<totalDuration)
                {
                    try {
                        sleep(500);
                        currentPosition = myMediaPlayer.getCurrentPosition();
                        storySeekBar.setProgress(currentPosition);
                    }
                    catch (InterruptedException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        };
        if(myMediaPlayer!=null){
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent i = getIntent();
        Bundle bundle = i.getExtras();

        myStories = findStory(Environment.getExternalStorageDirectory());

        items = new String[myStories.size()];
        //Log.i("TEST->",items.toString());
        for(int j=0;j<myStories.size();j++){
            items[j] = myStories.get(j).getName().toString().replace(".mp3","").replace(".wav","");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        storiesListView.setAdapter(arrayAdapter);
        storiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
//                String storyName = storiesListView.getItemAtPosition(position).toString();
                pos=position1;
                myMediaPlayer.stop();
                position=((position+1)%myStories.size());//+1
                Uri u1 = Uri.parse(myStories.get(pos).toString());
                myMediaPlayer = MediaPlayer.create(getApplicationContext(),u1);
                sname = myStories.get(pos).getName().toString();
                storyTextLabel.setText(sname);
                //////
                //storySeekBar.setProgress(myMediaPlayer.getDuration());
                /////
                try {
                    myMediaPlayer.start();
                }
                catch (Exception e){}
//                startActivity(new Intent(getApplicationContext(),PlayerStories.class)
//                        .putExtra("pos",position).putExtra("stories",myStories).putExtra("storyname",storyName));
            }});
        //

        sname = myStories.get(position).getName().toString();
        String storyName = storiesListView.getItemAtPosition(position).toString();;
        storyTextLabel.setText(storyName);
        storyTextLabel.setSelected(true);

        position = pos;
        //Log.i("Informatie->",position+"");
        Uri u = Uri.parse(myStories.get(position).toString());
        //Log.i("Informatie->",myStories.get(position).toString());

        myMediaPlayer = MediaPlayer.create(getApplicationContext(),u);
        myMediaPlayer.start();
        storySeekBar.setMax(myMediaPlayer.getDuration());

        updateSeekBar.start();
        storySeekBar.getProgressDrawable().setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.MULTIPLY);
        storySeekBar.getThumb().setColorFilter(getResources().getColor(R.color.colorPrimary),PorterDuff.Mode.SRC_IN);

        storySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                storyDuration.setText( timerConversion((long) myMediaPlayer.getCurrentPosition())+"/"+timerConversion((long) myMediaPlayer.getDuration()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.seekTo(seekBar.getProgress());

            }
        });
        btn_pause.setOnClickListener((v) -> {
            storySeekBar.setMax(myMediaPlayer.getDuration());
            if(myMediaPlayer.isPlaying()){
                btn_pause.setBackgroundResource(R.drawable.icon_play);
                myMediaPlayer.pause();
            }
            else{
                btn_pause.setBackgroundResource(R.drawable.icon_pause);
                myMediaPlayer.start();
            }
        });

        btn_next.setOnClickListener((v) -> {
            myMediaPlayer.stop();
            myMediaPlayer.release();
            position=((position+1)%myStories.size());
            Uri u1 = Uri.parse(myStories.get(position).toString());
            myMediaPlayer = MediaPlayer.create(getApplicationContext(),u1);
            sname = myStories.get(position).getName().toString();
            storyTextLabel.setText(sname);
            //////
            //storySeekBar.setProgress(myMediaPlayer.getDuration());
            /////
            try {
                myMediaPlayer.start();
            }
            catch (Exception e){}
        });
        btn_previous.setOnClickListener((v -> {
            myMediaPlayer.stop();
            myMediaPlayer.release();

            position = ((position-1)<0)?(myStories.size()-1):(position-1);
            Uri u2 = Uri.parse(myStories.get(position).toString());
            myMediaPlayer = MediaPlayer.create(getApplicationContext(),u2);
            sname = myStories.get(position).getName().toString();
            storyTextLabel.setText(sname);
            try {
                myMediaPlayer.start();
            }
            catch (Exception e){}
        }));
//        myMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
////            @Override
////            public void onCompletion(MediaPlayer mp) {
////                //myMediaPlayer.stop();
//////                myMediaPlayer.release();
//////                position++;
//////
//////                myMediaPlayer.reset();
//////                //Uri u3 = Uri.parse(myStories.get(position).toString());
//////                myMediaPlayer = MediaPlayer.create(getApplicationContext(),Uri.parse(myStories.get(position).toString()));
//////                sname = myStories.get(position).getName().toString();
//////                storyTextLabel.setText(sname);
////                btn_next.callOnClick();
////                //////
////                //storySeekBar.setProgress(myMediaPlayer.getDuration());
////                /////
//////                try {
//////                    myMediaPlayer.start();
//////                }
//////                catch (Exception e){}
////            }
//        }
        //);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==android.R.id.home){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
    //
    public String timerConversion(long value)
    {
        String audioTime;
        int duration = (int)value;
        int hours = (duration / 3600000);
        int minutes = (duration / 60000) % 60000;
        int seconds = duration % 60000 / 1000;
        if(hours > 0){
            audioTime = String.format("%02d:%02d:%02d",hours,minutes,seconds);
        }
        else{
            audioTime = String.format("%02d:%02d",minutes,seconds);
        }
        return audioTime;
    }
    public ArrayList<File> findStory(File root){
        Log.i("Info====><",root+"");
        ArrayList<File> arrFile = new ArrayList<File>();
        File[] files = root.listFiles();


        //Log.i("Info====>",root.listFiles().toString());
        if(files!=null)
            for(File singleFile : files){
                if(singleFile.isDirectory() && !singleFile.isHidden()){
                    arrFile.addAll(findStory(singleFile));
                }
                else{
                    if(singleFile.getName().endsWith(".mp3") || singleFile.getName().endsWith(".wav")){
                        arrFile.add(singleFile);
                    }
                }
            }
        return arrFile;
    }
    public void display(){
        final ArrayList<File> myStories = findStory(Environment.getExternalStorageDirectory());
        items = new String[myStories.size()];
        for(int i=0;i<myStories.size();i++){
            items[i] = myStories.get(i).getName().toString().replace(".mp3","").replace(".wav","");
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        storiesListView.setAdapter(arrayAdapter);

        storiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String storyName = storiesListView.getItemAtPosition(position).toString();

                startActivity(new Intent(getApplicationContext(),PlayerStories.class)
                        .putExtra("pos",position).putExtra("stories",myStories).putExtra("storyname",storyName));
            }
        });
    }

}