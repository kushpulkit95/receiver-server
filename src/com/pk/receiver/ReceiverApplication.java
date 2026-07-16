package com.pk.receiver;

public class ReceiverApplication {
    public static void main(String[] args) {

        TcpServer tcp = new TcpServer();
        UdpServer udp = new UdpServer();

        new Thread(() -> tcp.start()).start();
        new Thread(() -> udp.start()).start();
    }
}