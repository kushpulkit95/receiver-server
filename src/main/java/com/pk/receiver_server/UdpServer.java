package com.pk.receiver_server;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServer{
    
    private int receivedCount = 0;

    public void start(){
        //A shutdown hook is a small piece of code the JVM executes while it's shutting down.
        Runtime.getRuntime().addShutdownHook( 
            //.getRuntime means "give me currently running java virtual machine"
            //.addShutDownHook means "Before JVM shuts down, please execute this code"                                
            new Thread(() -> {

                System.out.println("\n==========================================================");
                System.out.println("UDP Receiver Summary");
                System.out.println("==========================================================");
                System.out.println("Received: " + receivedCount);
                System.out.println("==========================================================");

            })
        );
        File file = new File("..\\output\\received-nat.csv");
        boolean newFile = !file.exists();
        try( 
            FlatFileWriter fileWriter = new FlatFileWriter(file);
            //Creates file
            DatagramSocket socket = new DatagramSocket(5001);
            //means "i am waiting for UDP packets at port 5000"
        ){ 
            if(newFile){
                fileWriter.write("Private_IP,Private_Port,Public_IP,Public_Port,Destination_IP,Destination_Port,Protocol,Timestamp");
            }
            System.out.println("UDP server listening on port 5001...");
            while(true){
                byte[] buffer = new byte[4096]; //simply because we need space for message, 4096 is safe spot for sim

                DatagramPacket packet = new DatagramPacket(buffer, buffer.length); //this packet is for receiving
                socket.receive(packet); // "sit here until someone sends me a UDP packet"
                receivedCount++;

                String message = new String(
                            packet.getData(), // returns 'byte[]' the raw bytes
                            0,  // 0? means, start reading from the beginning
                            packet.getLength()); // "only convert the bytes that actually arrived"
                            //if buffer is 4096 but we only only 120, there is no need for rest (they are all empty characters)

                fileWriter.write(message);
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
