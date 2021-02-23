package com.example;

import java.net.InetAddress;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner myScanner = new Scanner(System.in);
        System.out.println("Enter port: ");
        int port = myScanner.nextInt();
        Server myServer = new Server();
        myServer.startServer(port);
        myScanner.nextLine();


        Multicast multicast = new Multicast(7, "224.0.0.0");
        multicast.tcpPort = port;
        try {
            multicast.tcpAddress = InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            e.printStackTrace();
        }
        multicast.start();


        boolean menuFlag = true;
        while(menuFlag){
            String command = myScanner.nextLine();
            switch (command) {
                case "-help" -> myServer.help();
                case "-info" -> myServer.printInfo();
                case "-clients" -> myServer.printClients();
                case "-stop" -> {
                    myServer.stopServer();
                    menuFlag = false;
                }
                default -> System.out.println("Unknown command. Use -help to display available commands.");
            }
        }
    }
}
