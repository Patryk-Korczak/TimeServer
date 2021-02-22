package com.example;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.time.Instant;

public class Service extends Thread {
    Socket client;
    boolean started = true;
    byte [] message = new byte[1024];

    public Service(Socket client) {
        this.client = client;
    }

    public void run() {
        System.out.println(this.client.getInetAddress().getHostName() + " connected from port " + this.client.getPort());

        try {
            DataInputStream toReceive = new DataInputStream(this.client.getInputStream());
            DataOutputStream toSend = new DataOutputStream(this.client.getOutputStream());
            while(this.started) {
                int bytesReceived = toReceive.read(message);
                String messageString = new String(this.message, 0, bytesReceived);

                /*

                System.out.println(this.client.getInetAddress().getHostName() + " - "
                                    + this.client.getInetAddress().getHostAddress() + " on Port " +
                                    + this.client.getPort() + " - " + messageString + " (" + bytesReceived + " bytes)");

                 */

                if(messageString.contains("exit")) {
                    this.started = false;
                    System.out.println("Disconnecting " + this.client.getInetAddress().getHostName());
                }
                if(messageString.equalsIgnoreCase("getTime")) {
                    long unixTimestamp = Instant.now().toEpochMilli();
                    String timeValue = String.valueOf(unixTimestamp);
                    toSend.write(timeValue.getBytes(), 0, timeValue.getBytes().length);
                }else {
                    toSend.write(messageString.getBytes(), 0, bytesReceived);
                }
                toSend.flush();


            }
        } catch(Exception e) {
            System.out.println(this.client.getInetAddress().getHostName() + " disconnected. " + e.getMessage());
        }

        try {
            this.client.close();
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }


}
