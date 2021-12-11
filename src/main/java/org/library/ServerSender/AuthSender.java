package org.library.ServerSender;

import org.library.client.Client;
import org.library.client.ToServer;
import org.library.current.UserCurrent;
import org.library.model.User;

import java.net.Socket;

public class AuthSender {
    private Client client;
    private static final String authComand = "auth";

    public AuthSender() {
        this.client = Client.getClient();
    }

    public boolean auth(String login, String password) {
        ToServer.send(authComand, client.getOut());
        ToServer.send(login + " " + password, client.getOut());
        User user = ToServer.accept(client.getInput());

        if(user == null) {
            System.out.println("filed");
            return false;
        } else {
            UserCurrent.add(user);
            System.out.println(user);
            return true;
        }
    }
}
