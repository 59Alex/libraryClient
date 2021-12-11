package org.library.ServerSender;

import org.library.client.Client;
import org.library.client.ToServer;
import org.library.model.Newspaper;
import org.library.model.User;

import java.util.List;

public class UserSender {

    private Client client;

    private static final String userKey = "user";
    private static final String allKey = "getAll";
    private static final String updateKey = "update";
    private static final String esc = "esc";
    private static final String saveKey = "save";
    private static final String deleteKey = "delete";
    private static final String findByUsernameKey = "findByUsername";
    private static final String findByIdKey = "findById";
    private static final String findByName = "findByFirstName";


    public UserSender() {
        this.client = Client.getClient();
    }

    public User findById(Long id) {
        ToServer.send(userKey, client.getOut());
        ToServer.send(findByIdKey, client.getOut());
        ToServer.send(id, client.getOut());
        User user = ToServer.accept(client.getInput());
        if(user == null) {
            System.out.println("User findById filed");
            return null;
        } else {
            return user;
        }
    }

    public User findByUsername(String name) {
        ToServer.send(userKey, client.getOut());
        ToServer.send(findByUsernameKey, client.getOut());
        ToServer.send(name, client.getOut());
        User user = ToServer.accept(client.getInput());
        if(user == null) {
            System.out.println("User findByUsername filed");
            return null;
        } else {
            return user;
        }
    }

    public List<User> findByName(String name) {
        ToServer.send(userKey, client.getOut());
        ToServer.send(findByName, client.getOut());
        ToServer.send(name, client.getOut());
        List<User> users = ToServer.accept(client.getInput());
        if(users == null) {
            System.out.println("User findByName filed");
            return null;
        } else {
            return users;
        }
    }

    public Boolean deleteByKey(Long id) {
        ToServer.send(userKey, client.getOut());
        ToServer.send(deleteKey, client.getOut());
        ToServer.send(id, client.getOut());
        return ToServer.accept(client.getInput());
    }

    public List<User> getAll() {
        ToServer.send(userKey, client.getOut());
        ToServer.send(allKey, client.getOut());
        List<User> users = ToServer.accept(client.getInput());

        if(users == null) {
            System.out.println("User getAll filed");
            return null;
        } else {
            return users;
        }
    }
    public void esc() {
        ToServer.send(userKey, client.getOut());
        ToServer.send(esc, client.getOut());
    }
    public Boolean update(User user) {
        ToServer.send(userKey, client.getOut());
        ToServer.send(updateKey, client.getOut());
        ToServer.send(user, client.getOut());
        return ToServer.accept(client.getInput());
    }
    public Boolean save(User user) {
        ToServer.send(userKey, client.getOut());
        ToServer.send(saveKey, client.getOut());
        ToServer.send(user, client.getOut());
        return ToServer.accept(client.getInput());
    }

}
