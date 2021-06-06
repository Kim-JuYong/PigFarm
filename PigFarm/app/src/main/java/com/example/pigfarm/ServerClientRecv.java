package com.example.pigfarm;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerClientRecv {
    public static final int DEFAULT_BUFFER_SIZE = 10000;

    public void RecvText() {
        int port = Integer.parseInt("24546");  //int port =  9999;
        String filename = "test.inp";              //String filename = "test.mp4"; //저장할 파일 이름

        try {
            ServerSocket server = new ServerSocket(port);
            System.out.println("This server is listening... (Port: " + port + ")");
            Socket socket = server.accept();  //새로운 연결 소켓 생성 및 accept대기
            InetSocketAddress isaClient = (InetSocketAddress) socket.getRemoteSocketAddress();

            System.out.println("A client(" + isaClient.getAddress().getHostAddress() +
                    " is connected. (Port: " + isaClient.getPort() + ")");

            FileOutputStream fos = new FileOutputStream(filename);
            InputStream is = socket.getInputStream();

            double startTime = System.currentTimeMillis();
            byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
            int readBytes;
            while ((readBytes = is.read(buffer)) != -1) {
                fos.write(buffer, 0, readBytes);
            }
            double endTime = System.currentTimeMillis();
            double diffTime = (endTime - startTime) / 1000;


            System.out.println("time: " + diffTime + " second(s)");

            is.close();
            fos.close();
            socket.close();
            server.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
