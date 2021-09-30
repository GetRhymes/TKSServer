package com.poly.server.utils;

import com.poly.parser.Message;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MessageReader {

    private InputStream inputStream;
    private BufferedReader bufferedReader;

    public MessageReader(InputStream inputStream) {
        this.inputStream = inputStream;
        this.bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
    }

    public byte[] readFile(int size) throws IOException {
        return inputStream.readNBytes(size);
    }

    public Message readMessage() throws IOException {
            Message message = new Message();
            message.parseToMessage(bufferedReader.readLine());
            return message;
    }

    public boolean readyForMessageReading() throws IOException {
        return bufferedReader.ready();
    }
}