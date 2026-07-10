package com.pk.receiver;

public class ReceiverApplication {
    private static final String PROTOCOL = "tcp"; //!!! CHANGE THIS TO SWITCH PROTOCOL !!!
    public static void main(String[] args) {

        Server server = null;
        if(PROTOCOL.equals("tcp"))
        server = new TcpServer();
        else if(PROTOCOL.equals("udp"))
        server = new UdpServer();

        server.start();
    }
}