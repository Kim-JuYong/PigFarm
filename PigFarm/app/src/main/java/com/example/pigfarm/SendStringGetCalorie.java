package com.example.pigfarm;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Iterator;

public class SendStringGetCalorie extends Thread{
    // 서버 ip, port 정보
    String serverIP = "220.84.169.150";
    int port = 24546;
    String calorie;
    String foodname;

    public SendStringGetCalorie(String foodname) {
        this.foodname = foodname;
    }

    public void getCalorieByString(String foodname){
        try {
            Socket socket = new Socket(serverIP, port);

            OutputStream socketOutputStream = socket.getOutputStream();
            byte[] buffer;
            // 보낼 데이터 타입 전송 "string"
            buffer = "str".getBytes("utf-8");
            socketOutputStream.write(buffer);
            socketOutputStream.flush();
            // 받은 음식이름을 다시 서버로 전송해 칼로리 값을 받아옴
            buffer = foodname.getBytes("utf-8");
            socketOutputStream.write(buffer);
            socketOutputStream.flush();

            // 다시 받아온 정보를 저장
            BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            calorie = socketInput.readLine();
            System.out.println("recv Data : " + calorie);

            socketOutputStream.close();
            socketInput.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getCalorie() {
        return calorie;
    }

    public void run(){
        this.getCalorieByString(foodname);
    }
}
