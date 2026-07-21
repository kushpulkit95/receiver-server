package com.pk.receiver_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ReceiverApplication {
    public static void main(String[] args) {
        SpringApplication.run(ReceiverApplication.class,args);

        TcpServer tcp = new TcpServer();
        UdpServer udp = new UdpServer();

        new Thread(() -> tcp.start()).start();
        new Thread(() -> udp.start()).start();
    }
}