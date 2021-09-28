package com.poly.server.threads;

import java.io.*;
import java.util.List;
import java.util.Queue;

public class ClientThread extends Thread {

    private BufferedReader reader;
    private PrintWriter writer;
    private List<PrintWriter> writers;

    public ClientThread(InputStream inputStream, OutputStream outputStream, List<PrintWriter> writers) {
        this.reader = new BufferedReader(new InputStreamReader(inputStream));
        this.writer = new PrintWriter(new OutputStreamWriter(outputStream), true);
        this.writers = writers;
    }

    @Override
    public void run() {
        try {
            writers.add(writer);
            String message = "";
            while (true) {
                try {
                    message = "";
                    if (reader.ready()) {
                        message = reader.readLine();
                        System.out.println(message);
                    }
                    if (message != null && !message.isEmpty()) {
                        for (PrintWriter writer : writers) {
                            writer.println(message);
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } finally {
            writers.remove(writer);
        }
    }
}
