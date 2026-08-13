package com.pk.receiver_server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/*
 * TcpServer is the receiving side of the simulator.
 *
 * Responsibilities:
 * - Listen for incoming TCP connections on a specific port.
 * - Accept a client connection.
 * - Receive text data sent by the client.
 * - Write the received message in a flat file.
 *
 * Current Implementation:
 * Simulator (Client) ----TCP----> TcpServer (Receiver)
 *
 */

public class TcpServer{

    private static final Logger logger =
            LoggerFactory.getLogger(TcpServer.class);

    private int totalRecordsReceived = 0;

    public void start(){ //named it "start()" because it will start the server

     try{

        Path path = Paths.get("..", "output");
        Files.createDirectories(path);
        Path file = path.resolve("received-cdr.csv");
        boolean newFile = !(file.toFile()).exists();

        try (
            FlatFileWriter fileWriter = new FlatFileWriter(file.toFile());
            //Creates file
            ServerSocket server = new ServerSocket(5000);
            //Creates TCP server that listens for incoming connections on port 5000
        ){
            if(newFile){
                fileWriter.write("IMSI,MSISDN,IMEI,APN,RATType,Action,Timestamp");
            }
            logger.info("TCP Server listening on port 5000...");
            
            while(true){
                try(
                    Socket socket = server.accept();
                    // Waits (blocks) until a client connects.
                    // Once connected, a Socket representing that client is returned.
                     BufferedReader reader =
                        new BufferedReader( // Reads text efficiently and allows reading one complete line at a time.
                            new InputStreamReader( // Converts incoming bytes into readable characters.
                                socket.getInputStream())); // Gets the stream used to receive data from the connected client.
                ){
                String message; 
                
                while((message = reader.readLine()) != null){ // Reads one complete line sent by the client.

                    if(message.isBlank()){
                        continue;
                    }
                    fileWriter.write(message);
                    totalRecordsReceived++;
                }

                logger.info(
                    "TCP: CDR record received | Total: {}",
                                totalRecordsReceived
                            );
                        
                //socket.close(); // Closes the connection with this client.
                //server.close(); // Stops the server from listening for new connections.
            }
        }
    }
    } catch (Exception e) {
        logger.error("TCP server stopped due to an error", e);
        }
    }
}