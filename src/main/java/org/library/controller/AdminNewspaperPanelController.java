package org.library.controller;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.ServerSender.BookSender;
import org.library.ServerSender.NewspaperSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;
import org.library.model.Author;
import org.library.model.Book;
import org.library.model.Newspaper;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

public class AdminNewspaperPanelController {
    @FXML
    private TableView<Newspaper> newspaperTable;
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


    private NewspaperSender newspaperSender;

    public AdminNewspaperPanelController() {
    }

    @FXML
    void initialize() {
        newspaperSender = new NewspaperSender();
        List<Newspaper> list = newspaperSender.getAll();
        initTable(list);


        backBtn.setOnAction(event -> {
            newspaperSender.esc();
            NewWindow.openWindow("/adminPanel.fxml");
        });
        saveBtn.setOnAction(event -> {
            Boolean saved = onSaveBtn();
            if(saved == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
            if(saved) {
                initTable(newspaperSender.getAll());
                newspaperTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });

        getBodyBtn.setOnAction(event -> {
            Newspaper newspaper = newspaperTable.getSelectionModel().getSelectedItem();
            if(newspaper == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            CurrentBody.getBodyCuurent().setBody(newspaper.getBody());
            CurrentBody.getBodyCuurent().setBackPage("/adminNewspaperPanel.fxml");
            NewWindow.openWindow("/bodyPanel.fxml");
        });
        newspaperTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null) {
                onSelectedRow(newValue);
            }
        }));
        updateBtn.setOnAction(event -> {
            Newspaper newspaper = newspaperTable.getSelectionModel().getSelectedItem();
            Boolean updated = onUpdateBtn(newspaper);
            if(updated == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(updated) {
                initTable(newspaperSender.getAll());
                newspaperTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });
        deleteBtn.setOnAction(event -> {
            Newspaper newspaper = newspaperTable.getSelectionModel().getSelectedItem();
            if(newspaper == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            Boolean deleted = onDeleteBtn(newspaper.getId());
            if(deleted == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(deleted) {
                initTable(newspaperSender.getAll());
                newspaperTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });

        findByNameBtn.setOnAction(event -> {
            String name = findByNameF.getText();
            List<Newspaper> newspapers = onFindByNameBtn(name);
            if(newspapers == null) {
                return;
            }
            initTable(newspapers);
            newspaperTable.refresh();
        });

        findByIdBtn.setOnAction(event -> {
            String id = findByIdF.getText();
            Newspaper newspaper = onFindByIdBtn(id);
            if(newspaper == null) {
                return;
            }
            initTable(Collections.singletonList(newspaper));
            newspaperTable.refresh();
        });

    }

    public Newspaper onFindByIdBtn(String idS) {
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
        return newspaperSender.findById(id);
    }
    public List<Newspaper> onFindByNameBtn(String name) {
        if(name == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return null;
        }
        if(name.equals("")) {
            initTable(newspaperSender.getAll());
            newspaperTable.refresh();
            return null;
        }
        return newspaperSender.findByName(name);
    }

    public Boolean onDeleteBtn(Long id) {
        if(id == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        return newspaperSender.deleteByKey(id);
    }

    public Boolean onUpdateBtn(Newspaper newspaper) {
        if(newspaper == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        String name = updateNameF.getText();
        String body = updateBodyF.getText();
        String des = updateDesF.getText();
        if(name.equals("") || name.equals("") || name.equals("")) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
        }
        newspaper.setName(name);
        newspaper.setBody(body);
        newspaper.setDescription(des);
        return newspaperSender.update(newspaper);
    }

    public Boolean onSaveBtn() {
        Newspaper newspaper = new Newspaper();
        String name = saveNameF.getText();
        String body = saveBodyF.getText();
        String des = saveDesF.getText();
        if(name.equals("") || name.equals("") || name.equals("")) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
        }
        newspaper.setName(name);
        newspaper.setBody(body);
        newspaper.setDescription(des);
        Author author = Author.createBuilder()
                .setFirstName(UserCurrent.getUserCurrent().getUser().getFirstName())
                .setLastName(UserCurrent.getUserCurrent().getUser().getLastName())
                .setDescription("admin")
                .build();
        newspaper.setAuthor(author);
        return newspaperSender.save(newspaper);
    }

    public void onSelectedRow(Newspaper newspaper) {
        updateNameF.setText(newspaper.getName());
        updateBodyF.setText(newspaper.getBody());
        updateDesF.setText(newspaper.getDescription());
    }

    private void initTable(List<Newspaper> list) {
        if(list != null) {
            id.setCellValueFactory(new PropertyValueFactory("id"));
            name.setCellValueFactory(new PropertyValueFactory("name"));
            description.setCellValueFactory(new PropertyValueFactory("description"));
            date.setCellValueFactory(new PropertyValueFactory("date"));
            author.setCellValueFactory(new PropertyValueFactory("author"));
            newspaperTable.setItems(FXCollections.observableList(list));
        } else {
            AlertWindow.alertWindow.alertWindow("Ошибка");
        }
    }
}
