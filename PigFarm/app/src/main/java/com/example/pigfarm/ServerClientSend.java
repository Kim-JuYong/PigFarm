package com.example.pigfarm;

import android.icu.text.Edits;
import android.util.JsonReader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.Set;

public class ServerClientSend {
    public static final int DEFAULT_BUFFER_SIZE = 4096;
    InetAddress serverIP = null;            //String serverIP = "127.0.0.1";
    int port = 24546;   //int port = 9999;

    public void jsonParsing(String jsonString) {
        try {
            JSONObject jobject = new JSONObject(jsonString);
            Iterator<String> keyList = jobject.keys();
            while(keyList.hasNext()) {
                String a = keyList.next();
                System.out.println(a);
                System.out.println(jobject.getString(a));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    public void clientTest(File file){
        if (!file.exists()) {
            System.out.println("File not Exist.");
            System.exit(0);
        }

        long fileSize = file.length();
        long totalReadBytes = 0;
        byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
        int readBytes;
        double startTime = 0;

        try {
            serverIP = InetAddress.getByName("220.84.169.150");
            FileInputStream fis = new FileInputStream(file);
            Socket socket = new Socket(serverIP, port);
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if(!socket.isConnected()){
                System.out.println("Socket Connect Error.");
                System.exit(0);
            }

            startTime = System.currentTimeMillis();
            OutputStream os = socket.getOutputStream();
            while ((readBytes = fis.read(buffer)) > 0) {
                os.write(buffer, 0, readBytes);
                totalReadBytes += readBytes;
                System.out.println("In progress: " + totalReadBytes + "/"
                        + fileSize + " Byte(s) ("
                        + (totalReadBytes * 100 / fileSize) + " %)");
            }

            System.out.println("File transfer completed.");
            String read = input.readLine(); // 서버에서 보낸 값
            System.out.println(read);
            jsonParsing(read);
            fis.close();
            os.close();
            socket.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        double endTime = System.currentTimeMillis();
        double diffTime = (endTime - startTime)/ 1000;;
        double transferSpeed = (fileSize / 1000)/ diffTime;

        System.out.println("time: " + diffTime+ " second(s)");
        System.out.println("Average transfer speed: " + transferSpeed + " KB/s");
    }
}


