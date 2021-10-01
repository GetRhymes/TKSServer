package com.poly.server.threads;

import com.poly.parser.Message;
import com.poly.server.utils.MessageReader;
import com.poly.server.utils.MessageWriter;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

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
                    if (messageReader.readyForMessageReading()) {
                        message = messageReader.readMessage();
                        String fileName = message.getFileName();
                        Integer fileSize = message.getFileSize();
                        if (fileName != null && !fileName.isEmpty() && fileSize != null && fileSize > 0) {
                            file = messageReader.readFile(fileSize);
                        }
                    }
                    if (message != null) {
                        for (MessageWriter writer : writers) {
                            message.setDate((LocalDate.now().toString() + " " + LocalTime.now().toString()).replace(":", "."));
                            writer.writeMessage(message);
                            if (file != null && file.length > 0) {
                                writer.writeFile(file);
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            try {
                messageReader.close();
                messageWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            writers.remove(messageWriter);
        }
    }
}
