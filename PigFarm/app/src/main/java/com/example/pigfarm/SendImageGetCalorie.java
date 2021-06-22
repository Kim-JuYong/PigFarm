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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class SendImageGetCalorie extends Thread{
    // 서버 ip, port 정보
    String serverIP = "127.0.0.1";
    int port = 24546;
    public static final int DEFAULT_BUFFER_SIZE = 32768;
    HashMap<String, String> foods = new HashMap<String, String>();
    File imageFile;

    public SendImageGetCalorie(File file){
        this.imageFile = file;
    }
    public HashMap<String, String> jsonParsing(String jsonString){
        // 서버에서 받은 json 형식의 데이터를 파싱해서 HashMap 형태로 바꿔서 리턴해 줌

        try{
            JSONObject jsonObject = new JSONObject(jsonString);
            Iterator<String> keyList = jsonObject.keys();
            while(keyList.hasNext()) {
                String key = keyList.next();
                foods.put(key, jsonObject.getString(key));
                System.out.println(key+" : "+foods.get(key));
            }
        } catch(JSONException e){
            e.printStackTrace();
        }
        return foods;
    }

    // file class로 받음
    public void getCalorie(File file){
        if(!file.exists()){
            System.out.println("File not Exist");
            System.exit(0);
        }

        try{
            Socket socket = new Socket(serverIP, port);
            if(!socket.isConnected()){
                System.out.println("Socket Connect Error.");
                System.exit(0);
            }
            // 보낼 데이터 타입 전송 "image"
            OutputStream socketOutputStream = socket.getOutputStream();
            byte[] datatype = "img".getBytes("UTF-8");
            socketOutputStream.write(datatype,0,datatype.length);
            socketOutputStream.flush();
            System.out.println("send Data Type");

            // 파일 전송 (4069 byte씩 끊어서 전송)
            // 그냥 read함수를 쓰면 1byte씩 받아 오므로 적당한 buffer공간을 만들어 더 큰 단위로 끊아서 전송
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int readCount;
            FileInputStream fileInputStream = new FileInputStream(file);

            while( (readCount = fileInputStream.read(buffer)) > 0 ) {
                socketOutputStream.write(buffer,0, readCount);
            }
            System.out.println("file transfer is Done");

            // 받아온 data를 jsonParsing 함수로 parsing 해서 foods 에 저장함
            BufferedReader socketInput = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String recvData = socketInput.readLine();
            System.out.println("received : "+recvData);
            jsonParsing(recvData);

            fileInputStream.close();
            socketOutputStream.close();
            socketInput.close();
            socket.close();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public HashMap<String,String> getFoods(){
        return foods;
    }

    public void run(){
        this.getCalorie(imageFile);
    }
}
