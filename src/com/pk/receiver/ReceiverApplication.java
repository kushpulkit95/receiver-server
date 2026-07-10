package com.pk.receiver;

public class ReceiverApplication {
    public static void main(String[] args) {

        TcpServer server = new TcpServer();

        server.start();
    }
}