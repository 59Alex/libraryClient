package org.library.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.ServerSender.BookSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.client.ToServer;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;
import org.library.model.Author;
import org.library.model.Book;
import org.library.model.User;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AdminBooksPanelController {

    @FXML
    private TableView<Book> booksTable;
    @FXML
    private TableColumn<Book, Long> id;
    @FXML
    private TableColumn<Book, String> name;
    @FXML
    private TableColumn<Book, String> description;
    @FXML
    private TableColumn<Book, LocalDate> date;
    @FXML
    private TableColumn<Book, String> author;
    @FXML
    private TextField findByNameF;
    @FXML
    private TextField findByIdF;
    @FXML
    private Button findByNameBtn;
    @FXML
    private Button findByIdBtn;
    @FXML
    private Button getBodyBtn;
    @FXML
    private TextField updateNameF;
    @FXML
    private TextField updateDesF;
    @FXML
    private TextField updateBodyF;
    @FXML
    private Button updateBtn;
    @FXML
    private TextField saveNameF;
    @FXML
    private TextField saveDesF;
    @FXML
    private TextField saveBodyF;
    @FXML
    private Button saveBtn;
    @FXML
    private Button backBtn;
    @FXML
    private Button deleteBtn;


    private BookSender bookSender;

    public AdminBooksPanelController() {
    }

    @FXML
    void initialize() {
        bookSender = new BookSender();
        List<Book> list = bookSender.getAll();
        initTable(list);


        backBtn.setOnAction(event -> {
            bookSender.esc();
            NewWindow.openWindow("/adminPanel.fxml");
        });
        saveBtn.setOnAction(event -> {
             Boolean saved = onSaveBtn();
             if(saved == null) {
                 AlertWindow.alertWindow.alertWindow("Ошибка!");
             }
            if(saved) {
                initTable(bookSender.getAll());
                booksTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });

        getBodyBtn.setOnAction(event -> {
            Book book = booksTable.getSelectionModel().getSelectedItem();
            if(book == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            CurrentBody.getBodyCuurent().setBody(book.getBody());
            CurrentBody.getBodyCuurent().setBackPage("/adminBooksPanel.fxml");
            NewWindow.openWindow("/bodyPanel.fxml");
        });
        booksTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null) {
                onSelectedRow(newValue);
            }
        }));
        updateBtn.setOnAction(event -> {
            Book book = booksTable.getSelectionModel().getSelectedItem();
            Boolean updated = onUpdateBtn(book);
            if(updated == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(updated) {
                initTable(bookSender.getAll());
                booksTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });
        deleteBtn.setOnAction(event -> {
            Book book = booksTable.getSelectionModel().getSelectedItem();
            if(book == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            Boolean deleted = onDeleteBtn(book.getId());
            if(deleted == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(deleted) {
                initTable(bookSender.getAll());
                booksTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });

        findByNameBtn.setOnAction(event -> {
            String name = findByNameF.getText();
            List<Book> books = onFindByNameBtn(name);
            if(books == null) {
                return;
            }
            initTable(books);
            booksTable.refresh();
        });

        findByIdBtn.setOnAction(event -> {
            String id = findByIdF.getText();
            Book book = onFindByIdBtn(id);
            if(book == null) {
                return;
            }
            initTable(Collections.singletonList(book));
            booksTable.refresh();
        });

    }

    public Book onFindByIdBtn(String idS) {
        Long id;
        try {
            id = Long.valueOf(idS);
            if(id < 1) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException ex) {
            AlertWindow.alertWindow.alertWindow("Введите корректное число");
            return null;
        }
        return bookSender.findById(id);
    }
    public List<Book> onFindByNameBtn(String name) {
        if(name == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return null;
        }
        if(name.equals("")) {
            initTable(bookSender.getAll());
            booksTable.refresh();
            return null;
        }
        return bookSender.findByName(name);
    }

    public Boolean onDeleteBtn(Long id) {
        if(id == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        return bookSender.deleteByKey(id);
    }

    public Boolean onUpdateBtn(Book book) {
        if(book == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        String name = updateNameF.getText();
        String body = updateBodyF.getText();
        String des = updateDesF.getText();
        if(name.equals("") || name.equals("") || name.equals("")) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
        }
        book.setName(name);
        book.setBody(body);
        book.setDescription(des);
        return bookSender.update(book);
    }

    public Boolean onSaveBtn() {
        Book book = new Book();
        String name = saveNameF.getText();
        String body = saveBodyF.getText();
        String des = saveDesF.getText();
        if(name.equals("") || name.equals("") || name.equals("")) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
        }
        book.setName(name);
        book.setBody(body);
        book.setDescription(des);
        Author author = Author.createBuilder()
                .setFirstName(UserCurrent.getUserCurrent().getUser().getFirstName())
                .setLastName(UserCurrent.getUserCurrent().getUser().getLastName())
                .setDescription("admin")
                .build();
        book.setAuthor(author);
        return bookSender.save(book);
    }

    public void onSelectedRow(Book book) {
        updateNameF.setText(book.getName());
        updateBodyF.setText(book.getBody());
        updateDesF.setText(book.getDescription());
    }

    private void initTable(List<Book> list) {
        if(list != null) {
            id.setCellValueFactory(new PropertyValueFactory<Book, Long>("id"));
            name.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
            description.setCellValueFactory(new PropertyValueFactory<Book, String>("description"));
            date.setCellValueFactory(new PropertyValueFactory<Book, LocalDate>("date"));
            author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
            booksTable.setItems(FXCollections.observableList(list));
        } else {
            AlertWindow.alertWindow.alertWindow("Ошибка");
        }
    }
}
