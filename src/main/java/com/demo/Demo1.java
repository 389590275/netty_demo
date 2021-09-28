package com.demo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author xiangchijie
 * @date 2021/9/28 11:11 上午
 */
public class Demo1 {

    public static void main(String[] args) throws IOException {
        new Thread(() -> {
            try {
                startServer();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                startClient();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private static String processRequest(String request) {
        return request + ":answer";
    }

    public static void startServer() throws IOException {
        ServerSocket serverSocket = new ServerSocket(8000);
        Socket clientSocket = serverSocket.accept();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
        String request, response;
        while ((request = bufferedReader.readLine()) != null) {
            System.out.println(request);
            response = processRequest(request);
            out.println(response);
        }
    }

    public static void startClient() throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 8000);
        PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        new Thread(() -> {
            try {
                String request;
                while ((request = bufferedReader.readLine()) != null) {
                    System.out.println(request);
                }
            } catch (Exception e) {
            }
        }).start();

        for (int i = 0; i < 10; i++) {
            out.println("嘿嘿");
            Thread.sleep(1000L);
        }

        socket.close();
    }

}
