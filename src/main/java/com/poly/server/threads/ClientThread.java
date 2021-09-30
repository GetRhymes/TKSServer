package com.poly.server.threads;

import com.poly.parser.Message;
import com.poly.server.utils.MessageReader;
import com.poly.server.utils.MessageWriter;

import java.io.*;
import java.util.Date;
import java.util.List;
import java.util.Queue;

public class ClientThread extends Thread {

    private MessageReader messageReader;
    private MessageWriter messageWriter;
    private List<MessageWriter> writers;

    public ClientThread(InputStream inputStream, OutputStream outputStream, List<MessageWriter> writers) {
        this.messageReader = new MessageReader(inputStream);
        this.messageWriter = new MessageWriter(outputStream);
        this.writers = writers;
    }

    @Override
    public void run() {
        try {
            writers.add(messageWriter);
            Message message;
            byte[] file;
            while (true) {
                try {
                    message = null;
                    file = null;
                    if(messageReader.readyForMessageReading()) {
                        message = messageReader.readMessage();
                        String fileName = message.getFileName();
                        Long fileSize = message.getFileSize();
                        if(fileName != null && !fileName.isEmpty() && fileSize != null && fileSize > 0) {
                            file = messageReader.readFile(fileSize.intValue());
                        }
                    }
                    if(message != null) {
                        message.setDate(new Date(System.currentTimeMillis()).toString());
                        messageWriter.writeMessage(message);
                        if(file != null && file.length > 0) {
                            messageWriter.writeFile(file);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            writers.remove(messageWriter);
        }
    }
}
