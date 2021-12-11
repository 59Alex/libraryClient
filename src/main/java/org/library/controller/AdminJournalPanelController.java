package org.library.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.ServerSender.BookSender;
import org.library.ServerSender.JournalSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;
import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Journal;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AdminJournalPanelController {
    @FXML
    private TableView<Journal> journalTable;
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


    private JournalSender journalSender;

    public AdminJournalPanelController() {
    }

    @FXML
    void initialize() {
        journalSender = new JournalSender();
        List<Journal> list = journalSender.getAll();
        initTable(list);


        backBtn.setOnAction(event -> {
            journalSender.esc();
            NewWindow.openWindow("/adminPanel.fxml");
        });
        saveBtn.setOnAction(event -> {
            Boolean saved = onSaveBtn();
            if(saved == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
            if(saved) {
                initTable(journalSender.getAll());
                journalTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });

        getBodyBtn.setOnAction(event -> {
            Journal journal = journalTable.getSelectionModel().getSelectedItem();
            if(journal == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            CurrentBody.getBodyCuurent().setBody(journal.getBody());
            CurrentBody.getBodyCuurent().setBackPage("/adminJournalPanel.fxml");
            NewWindow.openWindow("/bodyPanel.fxml");
        });
        journalTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null) {
                onSelectedRow(newValue);
            }
        }));
        updateBtn.setOnAction(event -> {
            Journal journal = journalTable.getSelectionModel().getSelectedItem();
            Boolean updated = onUpdateBtn(journal);
            if(updated == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(updated) {
                initTable(journalSender.getAll());
                journalTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });
        deleteBtn.setOnAction(event -> {
            Journal journal = journalTable.getSelectionModel().getSelectedItem();
            if(journal == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            Boolean deleted = onDeleteBtn(journal.getId());
            if(deleted == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(deleted) {
                initTable(journalSender.getAll());
                journalTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
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

        findByIdBtn.setOnAction(event -> {
            String id = findByIdF.getText();
            Journal journal = onFindByIdBtn(id);
            if(journal == null) {
                return;
            }
            initTable(Collections.singletonList(journal));
            journalTable.refresh();
        });

    }

    public Journal onFindByIdBtn(String idS) {
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
        return journalSender.findById(id);
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

    public Boolean onDeleteBtn(Long id) {
        if(id == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        return journalSender.deleteByKey(id);
    }

    public Boolean onUpdateBtn(Journal journal) {
        if(journal == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        String name = updateNameF.getText();
        String body = updateBodyF.getText();
        String des = updateDesF.getText();
        if(name.equals("") || name.equals("") || name.equals("")) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
        }
        journal.setName(name);
        journal.setBody(body);
        journal.setDescription(des);
        return journalSender.update(journal);
    }

    public Boolean onSaveBtn() {
        Journal journal = new Journal();
        String name = saveNameF.getText();
        String body = saveBodyF.getText();
        String des = saveDesF.getText();
        if(name.equals("") || name.equals("") || name.equals("")) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
        }
        journal.setName(name);
        journal.setBody(body);
        journal.setDescription(des);
        Author author = Author.createBuilder()
                .setFirstName(UserCurrent.getUserCurrent().getUser().getFirstName())
                .setLastName(UserCurrent.getUserCurrent().getUser().getLastName())
                .setDescription("admin")
                .build();
        journal.setAuthor(author);
        return journalSender.save(journal);
    }

    public void onSelectedRow(Journal journal) {
        updateNameF.setText(journal.getName());
        updateBodyF.setText(journal.getBody());
        updateDesF.setText(journal.getDescription());
    }

    private void initTable(List<Journal> list) {
        if(list != null) {
            id.setCellValueFactory(new PropertyValueFactory<Book, Long>("id"));
            name.setCellValueFactory(new PropertyValueFactory<Book, String>("name"));
            description.setCellValueFactory(new PropertyValueFactory<Book, String>("description"));
            date.setCellValueFactory(new PropertyValueFactory<Book, LocalDate>("date"));
            author.setCellValueFactory(new PropertyValueFactory<Book, String>("author"));
            journalTable.setItems(FXCollections.observableList(list));
        } else {
            AlertWindow.alertWindow.alertWindow("Ошибка");
        }
    }
}
