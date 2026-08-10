package com.pk.receiver_server;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UdpServer{

    private static final Logger logger =
        LoggerFactory.getLogger(UdpServer.class);

    private int totalRecordsReceived = 0;

    public void start(){
        
        try{

        Path path = Paths.get("..", "output");
        Files.createDirectories(path);

        Path file = path.resolve("received-nat.csv");
        boolean newFile = !(file.toFile()).exists();

        try( 
            FlatFileWriter fileWriter = new FlatFileWriter(file.toFile());
            //Creates file
            DatagramSocket socket = new DatagramSocket(5001);
            //means "i am waiting for UDP packets at port 5001"
        ){ 
            if(newFile){
                fileWriter.write("Private_IP,Private_Port,Public_IP,Public_Port,Destination_IP,Destination_Port,Protocol,Timestamp");
            }
            logger.info("UDP server listening on port 5001...");

            while(true){

                byte[] buffer = new byte[4096]; //simply because we need space for message, 4096 is safe spot for sim

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); //this packet is for receiving
                socket.receive(packet); // "sit here until someone sends me a UDP packet"

                String message = new String(
                            packet.getData(), // returns 'byte[]' the raw bytes
                            0,  // 0? means, start reading from the beginning
                            packet.getLength()); // "only convert the bytes that actually arrived"
                            //if buffer is 4096 but we only only 120, there is no need for rest (they are all empty characters)
                
                if(message.isBlank())
                    continue;

                fileWriter.write(message);

                totalRecordsReceived++;

                logger.info(
                            "UDP: NAT record received | Total: {}",
                            totalRecordsReceived
                    );
                }
            }
        } catch(Exception e){
            logger.error("UDP server stopped due to some error",e);
        }
    }
}
