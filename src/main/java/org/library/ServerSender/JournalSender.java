package org.library.ServerSender;

import org.library.client.Client;
import org.library.client.ToServer;
import org.library.model.Book;
import org.library.model.Journal;

import java.util.List;

public class JournalSender {
    private Client client;
    private static final String journalKey = "journal";
    private static final String allKey = "getAll";
    private static final String updateKey = "update";
    private static final String esc = "esc";
    private static final String saveKey = "save";
    private static final String deleteKey = "delete";
    private static final String findByNameKey = "findByName";
    private static final String findByIdKey = "findById";


    public JournalSender() {
        this.client = Client.getClient();
    }

    public Journal findById(Long id) {
        ToServer.send(journalKey, client.getOut());
        ToServer.send(findByIdKey, client.getOut());
        ToServer.send(id, client.getOut());
        Journal journal = ToServer.accept(client.getInput());
        if(journal == null) {
            System.out.println("Journal findById filed");
            return null;
        } else {
            return journal;
        }
    }

    public List<Journal> findByName(String name) {
        ToServer.send(journalKey, client.getOut());
        ToServer.send(findByNameKey, client.getOut());
        ToServer.send(name, client.getOut());
        List<Journal> journals = ToServer.accept(client.getInput());
        if(journals == null) {
            System.out.println("Journals findByName filed");
            return null;
        } else {
            return journals;
        }
    }

    public Boolean deleteByKey(Long id) {
        ToServer.send(journalKey, client.getOut());
        ToServer.send(deleteKey, client.getOut());
        ToServer.send(id, client.getOut());
        return ToServer.accept(client.getInput());
    }

    public List<Journal> getAll() {
        ToServer.send(journalKey, client.getOut());
        ToServer.send(allKey, client.getOut());
        List<Journal> journals = ToServer.accept(client.getInput());
        if(journals == null) {
            System.out.println("Journals getAll filed");
            return null;
        } else {
            return journals;
        }
    }
    public void esc() {
        ToServer.send(journalKey, client.getOut());
        ToServer.send(esc, client.getOut());
    }
    public Boolean update(Journal journal) {
        ToServer.send(journalKey, client.getOut());
        ToServer.send(updateKey, client.getOut());
        ToServer.send(journal, client.getOut());
        return ToServer.accept(client.getInput());
    }
    public Boolean save(Journal journal) {
        ToServer.send(journalKey, client.getOut());
        ToServer.send(saveKey, client.getOut());
        ToServer.send(journal, client.getOut());
        return ToServer.accept(client.getInput());
    }
}
