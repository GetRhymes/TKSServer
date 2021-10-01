package com.poly.server.utils;

import com.poly.parser.Message;

import java.io.IOException;
import java.io.OutputStream;


public class MessageWriter {

    private OutputStream outputStream;


    public MessageWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
    }

    public void writeMessage(Message message) {
        String strMessage = message.toTransferString();
        try {
            outputStream.write(strMessage.length() << 24);
            outputStream.write(strMessage.length() << 16);
            outputStream.write(strMessage.length() << 8);
            outputStream.write(strMessage.length());
            outputStream.write(strMessage.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeFile(byte[] file) throws IOException {
        outputStream.write(file);
        outputStream.flush();
    }

    public void close() throws IOException {
        outputStream.close();
    }
}