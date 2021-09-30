package com.poly.server.threads;

import com.poly.parser.Message;
import com.poly.server.utils.MessageReader;
import com.poly.server.utils.MessageWriter;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
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
                        Integer fileSize = message.getFileSize();
                        System.out.println("FILE SIZE= " + fileSize);
                        if(fileName != null && !fileName.isEmpty() && fileSize != null && fileSize > 0) {
                            file = messageReader.readFile(fileSize);
                            System.out.println("FILE SIZE ACT = " + file.length);
                            for (int i = 0; i < fileSize; i++) {
                                System.out.print(file[i] + " ");
                            }
                        }
                    }
                    if(message != null) {
                        message.setDate((LocalDate.now().toString() + " " + LocalTime.now().toString()).replace(":", "."));
                        messageWriter.writeMessage(message);
                        System.out.println("WRITE MSH");
                        if(file != null && file.length > 0) {
                            messageWriter.writeFile(file);
                            System.out.println("WRITE FILE");
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
