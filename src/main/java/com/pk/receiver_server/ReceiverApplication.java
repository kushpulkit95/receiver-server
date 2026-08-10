package com.pk.receiver_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReceiverApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceiverApplication.class,args);

        TcpServer tcp = new TcpServer();
        UdpServer udp = new UdpServer();

        Thread tcpThread = new Thread(tcp::start, "tcp-server");
        Thread udpThread = new Thread(udp::start, "udp-server");

        tcpThread.start();
        udpThread.start();
    }
}