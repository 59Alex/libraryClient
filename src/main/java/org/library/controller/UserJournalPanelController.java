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
import org.library.ServerSender.JournalSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;
import org.library.model.Book;
import org.library.model.Journal;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UserJournalPanelController implements Initializable {
    @FXML
    private TableView<Journal> journalTable;
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
    private JournalSender journalSender;
    private ContentSender contentSender;
    private UserCurrent userCurrent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        journalSender = new JournalSender();
        this.contentSender = new ContentSender();
        userCurrent = UserCurrent.getUserCurrent();
        List<Journal> list = journalSender.getAll();
        initTable(list);

        addBtn.setOnAction(event -> {
            journalSender.esc();
            Journal journal = journalTable.getSelectionModel().getSelectedItem();
            if(journal == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            contentSender.addJournalToUser(journal.getId(), userCurrent.getUser().getId());
        });

        findByNameBtn.setOnAction(event -> {
            String name = findByNameF.getText();
            List<Journal> journals = onFindByNameBtn(name);
            if(journals == null) {
                return;
            }
            initTable(journals);
            journalTable.refresh();
        });
        getBodyBtn.setOnAction(event -> {
            Journal journal = journalTable.getSelectionModel().getSelectedItem();
            if(journal == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            CurrentBody.getBodyCuurent().setBody(journal.getBody());
            CurrentBody.getBodyCuurent().setBackPage("/userJournalsPanel.fxml");
            NewWindow.openWindow("/bodyPanel.fxml");
        });
        backBtn.setOnAction(event -> {
            journalSender.esc();
            NewWindow.openWindow("/userPanel.fxml");
        });
    }

    public List<Journal> onFindByNameBtn(String name) {
        if(name == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return null;
        }
        if(name.equals("")) {
            initTable(journalSender.getAll());
            journalTable.refresh();
            return null;
        }
        return journalSender.findByName(name);
    }

    private void initTable(List<Journal> list) {
        if(list != null) {
            name.setCellValueFactory(new PropertyValueFactory("name"));
            description.setCellValueFactory(new PropertyValueFactory("description"));
            date.setCellValueFactory(new PropertyValueFactory("date"));
            author.setCellValueFactory(new PropertyValueFactory("author"));
            journalTable.setItems(FXCollections.observableList(list));
        } else {
            AlertWindow.alertWindow.alertWindow("Ошибка");
        }
    }
}
