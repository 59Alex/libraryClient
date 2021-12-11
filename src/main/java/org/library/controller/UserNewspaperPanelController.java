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
import org.library.ServerSender.NewspaperSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;
import org.library.model.Book;
import org.library.model.Journal;
import org.library.model.Newspaper;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;

public class UserNewspaperPanelController implements Initializable {
    @FXML
    private TableView<Newspaper> newspaperTable;
    @FXML
    private TableColumn<Newspaper, String> name;
    @FXML
    private TableColumn<Newspaper, String> description;
    @FXML
    private TableColumn<Newspaper, LocalDate> date;
    @FXML
    private TableColumn<Newspaper, String> author;
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
    private NewspaperSender newspaperSender;

    private ContentSender contentSender;
    private UserCurrent userCurrent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        newspaperSender = new NewspaperSender();
        this.contentSender = new ContentSender();
        userCurrent = UserCurrent.getUserCurrent();

        addBtn.setOnAction(event -> {
            newspaperSender.esc();
            Newspaper newspaper = newspaperTable.getSelectionModel().getSelectedItem();
            if(newspaper == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            contentSender.addNewspaperToUser(newspaper.getId(), userCurrent.getUser().getId());
        });
        List<Newspaper> list = newspaperSender.getAll();
        initTable(list);

        findByNameBtn.setOnAction(event -> {
            String name = findByNameF.getText();
            List<Newspaper> newspapers = onFindByNameBtn(name);
            if(newspapers == null) {
                return;
            }
            initTable(newspapers);
            newspaperTable.refresh();
        });
        getBodyBtn.setOnAction(event -> {
            Newspaper newspaper = newspaperTable.getSelectionModel().getSelectedItem();
            if(newspaper == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            CurrentBody.getBodyCuurent().setBody(newspaper.getBody());
            CurrentBody.getBodyCuurent().setBackPage("/userNewspaperPanel.fxml");
            NewWindow.openWindow("/bodyPanel.fxml");
        });
        backBtn.setOnAction(event -> {
            newspaperSender.esc();
            NewWindow.openWindow("/userPanel.fxml");
        });
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

    private void initTable(List<Newspaper> list) {
        if(list != null) {
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
