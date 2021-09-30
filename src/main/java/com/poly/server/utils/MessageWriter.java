package com.poly.server.utils;

import com.poly.parser.Message;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

public class MessageWriter {

    private OutputStream outputStream;
    private PrintWriter printWriter;


    public MessageWriter(OutputStream outputStream) {
        this.outputStream = outputStream;
        this.printWriter = new PrintWriter(new OutputStreamWriter(outputStream), true);
    }

    public void writeMessage(Message message) {
        printWriter.println(message.toTransferString());
    }

    public void writeFile(byte[] file) throws IOException {
        outputStream.write(file);
        outputStream.flush();
    }
}