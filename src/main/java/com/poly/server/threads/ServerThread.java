package com.poly.server.threads;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ServerThread extends Thread {

    private static Integer PORT = 65432;
    private List<PrintWriter> writers;

    private ServerSocket serverSocket;

    public ServerThread(Queue<String> queue) throws IOException {
        this.serverSocket = new ServerSocket(PORT);
        this.writers = new LinkedList<>();
        this.start();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                ClientThread clientThread = new ClientThread(clientSocket.getInputStream(), clientSocket.getOutputStream(), writers);
                clientThread.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
