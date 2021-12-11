package org.library.client;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class Client {

    private static Client client;
    private Socket socket;
    private InputStream input;
    private OutputStream out;

    public void connectToServer() {
        try {
            this.socket = new Socket("localhost", 8080);
            this.out = socket.getOutputStream();
            this.input = socket.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void start() {
        if(client == null) {
            client = new Client();
            client.connectToServer();
        }
    }
    public static Client getClient() {
        if(client != null) {
            return client;
        } else {
            Client.start();
            return client;
        }
    }

    public Socket getSocket() {
        return socket;
    }

    public InputStream getInput() {
        return input;
    }

    public OutputStream getOut() {
        return out;
    }
}
