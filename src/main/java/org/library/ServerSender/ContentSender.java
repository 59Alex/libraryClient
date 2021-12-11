package org.library.ServerSender;

import org.library.client.Client;
import org.library.client.ToServer;
import org.library.model.Book;
import org.library.model.Content;
import org.library.model.Journal;
import org.library.model.Newspaper;

import java.util.ArrayList;
import java.util.List;

public class ContentSender {

    private final Client client;

    private static final String esc = "esc";
    private static final String getByUserId = "getByUserId";
    private static final String addBookToUser = "addBookToUser";
    private static final String addJournalToUser = "addJournalToUser";
    private static final String addNewspaperToUser = "addNewspaperToUser";
    private static final String content = "content";

    public ContentSender() {
        this.client = Client.getClient();
    }

    public void addBookToUser(Long bookId, Long userId) {
        ToServer.send(content, client.getOut());
        ToServer.send(addBookToUser, client.getOut());
        ToServer.send(userId, client.getOut());
        ToServer.send(bookId, client.getOut());
    }
    public void addNewspaperToUser(Long newspaperId, Long userId) {
        ToServer.send(content, client.getOut());
        ToServer.send(addNewspaperToUser, client.getOut());
        ToServer.send(userId, client.getOut());
        ToServer.send(newspaperId, client.getOut());
    }
    public void addJournalToUser(Long journalId, Long userId) {
        ToServer.send(content, client.getOut());
        ToServer.send(addJournalToUser, client.getOut());
        ToServer.send(userId, client.getOut());
        ToServer.send(journalId, client.getOut());
    }
    public List<Content> getByUserId(Long id) {
        ToServer.send(content, client.getOut());
        ToServer.send(getByUserId, client.getOut());
        ToServer.send(id, client.getOut());
        List<Content> contents = ToServer.accept(client.getInput());
        return contents;
    }
    public void esc() {
        ToServer.send(content, client.getOut());
        ToServer.send(esc, client.getOut());
    }
}
