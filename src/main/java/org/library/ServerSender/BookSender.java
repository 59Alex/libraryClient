package org.library.ServerSender;

import org.library.client.Client;
import org.library.client.ToServer;
import org.library.model.Book;

import java.util.Arrays;
import java.util.List;

public class BookSender {
    private Client client;
    private static final String bookKey = "book";
    private static final String allKey = "getAll";
    private static final String updateKey = "update";
    private static final String esc = "esc";
    private static final String saveKey = "save";
    private static final String deleteKey = "delete";
    private static final String findByNameKey = "findByName";
    private static final String findByIdKey = "findById";


    public BookSender() {
        this.client = Client.getClient();
    }

    public Book findById(Long id) {
        ToServer.send(bookKey, client.getOut());
        ToServer.send(findByIdKey, client.getOut());
        ToServer.send(id, client.getOut());
        Book book = ToServer.accept(client.getInput());
        if(book == null) {
            System.out.println("Books findById filed");
            return null;
        } else {
            return book;
        }
    }

    public List<Book> findByName(String name) {
        ToServer.send(bookKey, client.getOut());
        ToServer.send(findByNameKey, client.getOut());
        ToServer.send(name, client.getOut());
        List<Book> books = ToServer.accept(client.getInput());
        if(books == null) {
            System.out.println("Books findByName filed");
            return null;
        } else {
            return books;
        }
    }

    public Boolean deleteByKey(Long id) {
        ToServer.send(bookKey, client.getOut());
        ToServer.send(deleteKey, client.getOut());
        ToServer.send(id, client.getOut());
        return ToServer.accept(client.getInput());
    }

    public List<Book> getAll() {
        ToServer.send(bookKey, client.getOut());
        ToServer.send(allKey, client.getOut());
        List<Book> books = ToServer.accept(client.getInput());
        if(books == null) {
            System.out.println("Books getAll filed");
            return null;
        } else {
            System.out.println(books.get(0));
            return books;
        }
    }
    public void esc() {
        ToServer.send(bookKey, client.getOut());
        ToServer.send(esc, client.getOut());
    }
    public Boolean update(Book book) {
        ToServer.send(bookKey, client.getOut());
        ToServer.send(updateKey, client.getOut());
        ToServer.send(book, client.getOut());
        return ToServer.accept(client.getInput());
    }
    public Boolean save(Book book) {
        ToServer.send(bookKey, client.getOut());
        ToServer.send(saveKey, client.getOut());
        ToServer.send(book, client.getOut());
        return ToServer.accept(client.getInput());
    }
}
