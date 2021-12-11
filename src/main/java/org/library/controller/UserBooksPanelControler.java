package org.library.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.ServerSender.BookSender;
import org.library.ServerSender.ContentSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;
import org.library.model.Book;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UserBooksPanelControler implements Initializable {
    @FXML
    private TableView<Book> booksTable;
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
    private Button findByNameBtn;
    @FXML
    private Button getBodyBtn;
    @FXML
    private Button addBtn;
    @FXML
    private Button backBtn;
    private BookSender bookSender;
    private ContentSender contentSender;
    private UserCurrent userCurrent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        bookSender = new BookSender();
        this.contentSender = new ContentSender();
        this.userCurrent = UserCurrent.getUserCurrent();
        List<Book> list = bookSender.getAll();
        initTable(list);

        addBtn.setOnAction(event -> {
            bookSender.esc();
            Book book = booksTable.getSelectionModel().getSelectedItem();
            if(book == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            contentSender.addBookToUser(book.getId(), userCurrent.getUser().getId());
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
        getBodyBtn.setOnAction(event -> {
            Book book = booksTable.getSelectionModel().getSelectedItem();
            if(book == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            CurrentBody.getBodyCuurent().setBody(book.getBody());
            CurrentBody.getBodyCuurent().setBackPage("/userBooksPanel.fxml");
            NewWindow.openWindow("/bodyPanel.fxml");
        });
        backBtn.setOnAction(event -> {
            bookSender.esc();
            NewWindow.openWindow("/userPanel.fxml");
        });
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

    private void initTable(List<Book> list) {
        if(list != null) {
            name.setCellValueFactory(new PropertyValueFactory("name"));
            description.setCellValueFactory(new PropertyValueFactory("description"));
            date.setCellValueFactory(new PropertyValueFactory("date"));
            author.setCellValueFactory(new PropertyValueFactory("author"));
            booksTable.setItems(FXCollections.observableList(list));
        } else {
            AlertWindow.alertWindow.alertWindow("Ошибка");
        }
    }
}
