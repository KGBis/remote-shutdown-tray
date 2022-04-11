package com.kikegg.remote.shutdown;

import lombok.extern.java.Log;
import org.apache.commons.lang3.StringUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.*;

@Log
public class NetListener implements Runnable {

    public static final int PORT = 6800;
    private ServerSocket serverSocket;

    @SuppressWarnings("InfiniteLoopStatement")
    public void listen() throws IOException, ExecutionException, InterruptedException {
        // Initialize
        serverSocket = new ServerSocket(PORT);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        // Result from threaded execution is condition in this while
        // While the param string received from socket is blank
        while(true) {
            log.info("Socket listening @ port " + PORT);
            Future<?> submit = executor.submit(this);
            submit.get();
        }


    }

    @Override
    public void run() {
        try {
            // Wait to incomming messages (accept method is blocking)
            Socket socket = serverSocket.accept();
            log.info("Socket message received");

            // When server receives message, get message, send ack
            DataOutputStream outToClient = new DataOutputStream(socket.getOutputStream());
            outToClient.writeBytes("primljeno");
            log.info("Response ACK sent");

            BufferedReader inFromClient = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String clientSentence = inFromClient.readLine();
            log.info("Socket message read");

            // Check if client message is not blank
            if(StringUtils.isNotBlank(clientSentence)) {
                clientSentence = StringUtils.substring(clientSentence, 5, clientSentence.length() - 9);
                CMDWriter cMDWriter = new CMDWriter(clientSentence);
                cMDWriter.execShutdown();
                cMDWriter.print();
            }
        } catch (IOException e) {
            log.severe("Socket error: " + e);
        }
    }

}
