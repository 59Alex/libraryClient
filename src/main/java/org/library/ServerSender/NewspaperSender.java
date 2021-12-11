package org.library.ServerSender;

import org.library.client.Client;
import org.library.client.ToServer;
import org.library.model.Book;
import org.library.model.Newspaper;

import java.util.List;

public class NewspaperSender {
    private Client client;
    private static final String newspaperKey = "newspaper";
    private static final String allKey = "getAll";
    private static final String updateKey = "update";
    private static final String esc = "esc";
    private static final String saveKey = "save";
    private static final String deleteKey = "delete";
    private static final String findByNameKey = "findByName";
    private static final String findByIdKey = "findById";


    public NewspaperSender() {
        this.client = Client.getClient();
    }

    public Newspaper findById(Long id) {
        ToServer.send(newspaperKey, client.getOut());
        ToServer.send(findByIdKey, client.getOut());
        ToServer.send(id, client.getOut());
        Newspaper newspaper = ToServer.accept(client.getInput());
        if(newspaper == null) {
            System.out.println("Newspaper findById filed");
            return null;
        } else {
            return newspaper;
        }
    }

    public List<Newspaper> findByName(String name) {
        ToServer.send(newspaperKey, client.getOut());
        ToServer.send(findByNameKey, client.getOut());
        ToServer.send(name, client.getOut());
        List<Newspaper> newspapers = ToServer.accept(client.getInput());
        if(newspapers == null) {
            System.out.println("Newspaper findByName filed");
            return null;
        } else {
            return newspapers;
        }
    }

    public Boolean deleteByKey(Long id) {
        ToServer.send(newspaperKey, client.getOut());
        ToServer.send(deleteKey, client.getOut());
        ToServer.send(id, client.getOut());
        return ToServer.accept(client.getInput());
    }

    public List<Newspaper> getAll() {
        ToServer.send(newspaperKey, client.getOut());
        ToServer.send(allKey, client.getOut());
        List<Newspaper> newspapers = ToServer.accept(client.getInput());
        if(newspapers == null) {
            System.out.println("Newspaper getAll filed");
            return null;
        } else {
            return newspapers;
        }
    }
    public void esc() {
        ToServer.send(newspaperKey, client.getOut());
        ToServer.send(esc, client.getOut());
    }
    public Boolean update(Newspaper newspaper) {
        ToServer.send(newspaperKey, client.getOut());
        ToServer.send(updateKey, client.getOut());
        ToServer.send(newspaper, client.getOut());
        return ToServer.accept(client.getInput());
    }
    public Boolean save(Newspaper newspaper) {
        ToServer.send(newspaperKey, client.getOut());
        ToServer.send(saveKey, client.getOut());
        ToServer.send(newspaper, client.getOut());
        return ToServer.accept(client.getInput());
    }
}
