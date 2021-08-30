package com.example.nannybot;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;

import java.io.IOException;
import java.util.UUID;

public class BluetoothConnect {
    static final UUID mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    static BluetoothAdapter btAdapter = null ;
        //System.out.println(btAdapter.getBondedDevices());
    static boolean connected = false;
    static BluetoothDevice device = null;

    public static BluetoothSocket getBtSocket() {
        return btSocket;
    }

    //System.out.println(hc05.getName());
    static BluetoothSocket btSocket = null;
   public static void connect(String adressDevice)
   {
       btAdapter = BluetoothAdapter.getDefaultAdapter();
       device = btAdapter.getRemoteDevice(adressDevice);//HC-05=> 00:19:10:08:47:80  //Hands-free =>74:5C:4B:C2:6C:EC

       int counter=0;

       do {
           try {
               btSocket = device.createRfcommSocketToServiceRecord(mUUID);
               System.out.println(btSocket);
               btSocket.connect();
               connected = btSocket.isConnected();
               System.out.println(btSocket.isConnected());
           } catch (IOException e) {
               e.printStackTrace();
           }
           counter++;
       }while(!btSocket.isConnected() && counter<3);
   }
   public static void disconnect(){
       try {
           btSocket.close();
           connected = btSocket.isConnected();
           System.out.println(btSocket.isConnected());
       } catch (IOException e) {
           e.printStackTrace();
       }
   }
}
