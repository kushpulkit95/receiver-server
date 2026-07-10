package com.pk.receiver;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

import com.pk.receiver.FlatFileWriter;
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

public class TcpServer implements Server{

    public void start(){ //named it "start()" because it will start the server

        try (
            FlatFileWriter fileWriter = new FlatFileWriter("..\\output\\received-records.txt");
            //Creates file
            ServerSocket server = new ServerSocket(5000);
            //Creates TCP server that listens for incoming connections on port 5000
        ){
            System.out.println("TCP Server listening on port 5000...");
            
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
                String message = reader.readLine(); // Reads one complete line sent by the client.
                        
                fileWriter.write(message);
                        
                //socket.close(); // Closes the connection with this client.
                //server.close(); // Stops the server from listening for new connections.
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        }
    }
}