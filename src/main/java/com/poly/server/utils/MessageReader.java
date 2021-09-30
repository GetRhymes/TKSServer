package com.poly.server.utils;

import com.poly.parser.Message;

import java.io.*;

public class MessageReader {

    private InputStream inputStream;
    private BufferedReader bufferedReader;

    public MessageReader(InputStream inputStream) {
        this.inputStream = inputStream;
        //this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public byte[] readFile(int size) throws IOException {
        System.out.println("READ FILE");
        return inputStream.readNBytes(size);
    }

    public Message readMessage() throws IOException {
        int size = 0;
        for (int i = 0; i < 4; i++) {
            size = size << 8;
            size += inputStream.read();
        }
        Message message = new Message();
        String s = new String(inputStream.readNBytes(size));
        message.parseToMessage(s);
        return message;
    }

    public boolean readyForMessageReading() throws IOException {
        return inputStream.available() > 0;
    }
}