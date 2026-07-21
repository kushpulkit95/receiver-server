package com.pk.receiver_server;

import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FlatFileWriter implements AutoCloseable{

    private final BufferedWriter writer;

    public FlatFileWriter(File file) throws IOException {
        writer = new BufferedWriter( //instead of reading one chr at a time, reads buffered data and writes efficiently
                new FileWriter(file,true) 
                //we have 'true' because false will delete everything evertime you open a file, not what we want
        );
    }

    public void write(String record) throws IOException {
        writer.write(record);
        writer.newLine(); //automatically use line separator
        writer.flush(); //"write everything in disk now" so that we do not lose records
    }

    @Override
    public void close() throws IOException {
        writer.close();
    }
}