package com.example;

import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Multicast extends Thread {
    MulticastSocket mySocket = null;
    byte[] buffer = new byte[256];
    byte[] bufferResponse = new byte[256];
    int port;
    String address;
    int tcpPort;
    String tcpAddress;

    Multicast(int port, String address) {
        this.port = port;
        this.address = address;
    }

    public void run() {
        System.out.println("Starting multicast at: " + address + ":" + port );
        System.out.println("Sharing TCP address: " + tcpAddress + ":" + tcpPort);

        try {
            mySocket = new MulticastSocket(this.port);
        } catch(Exception e) {
            e.printStackTrace();
        }

        InetAddress group = null;
        try {
            group = InetAddress.getByName(address);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mySocket.joinGroup(group);
        } catch (Exception e) {
            e.printStackTrace();
        }


        while (true) {
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            try {
                mySocket.receive(packet);
                String received = new String(
                        packet.getData(), 0, packet.getLength());
                if ("DISCOVERY".equals(received)) {
                    String tcpInfo = "Address: " + tcpAddress+ " Port: "+tcpPort;
                    bufferResponse = tcpInfo.getBytes();
                    DatagramPacket datagramPacket = new DatagramPacket(bufferResponse,bufferResponse.length,packet.getAddress(),packet.getPort());
                    mySocket.send(datagramPacket);
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                try {
                    mySocket.leaveGroup(group);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mySocket.close();
            }
        }
    }
 }

