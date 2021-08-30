package com.example.nannybot;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class PlayerStoriesList extends AppCompatActivity {
    ListView storiesListView;
    String[] items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player_stories);
        storiesListView = (ListView) findViewById(R.id.storiesListView);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        //doStuff();
                        display();
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
        //runtimePermission();
    }

//    public void runtimePermission(){
//
//    }
    public ArrayList<File> findStory(File root){
        Log.i("Info====>",root+"");
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