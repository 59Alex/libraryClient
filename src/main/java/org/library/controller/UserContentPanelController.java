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
import org.library.ServerSender.NewspaperSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;
import org.library.model.Book;
import org.library.model.Content;
import org.library.model.ContentType;
import org.library.model.Journal;

import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class UserContentPanelController implements Initializable {
    @FXML
    private TableView<Content> contentTable;
    @FXML
    private TableColumn<Content, String> name;
    @FXML
    private TableColumn<Content, String> description;
    @FXML
    private TableColumn<Content, LocalDate> date;
    @FXML
    private TableColumn<Content, String> author;
    @FXML
    private TableColumn<Content, String> type;
    @FXML
    private TextField findByNameF;
    @FXML
    private Button findByNameBtn;
    @FXML
    private Button getBodyBtn;
    @FXML
    private Button backBtn;

    private ContentSender contentSender;

    private UserCurrent userCurrent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.contentSender = new ContentSender();
        this.userCurrent = UserCurrent.getUserCurrent();
        initTable(contentSender.getByUserId(userCurrent.getUser().getId()));
        findByNameBtn.setOnAction(event -> {
            String name = findByNameF.getText();
            onFindByNameBtn(name);
        });
        getBodyBtn.setOnAction(event -> {
            Content content = contentTable.getSelectionModel().getSelectedItem();
            if(content == null) {
                AlertWindow.alertWindow.alertWindow("Выберите элемент!");
                return;
            }
            CurrentBody.getBodyCuurent().setBody(content.getBody());
            CurrentBody.getBodyCuurent().setBackPage("/userContentPanel.fxml");
            NewWindow.openWindow("/bodyPanel.fxml");
        });
        backBtn.setOnAction(event -> {
            contentSender.esc();
            NewWindow.openWindow("/userPanel.fxml");
        });

    }

    public void onFindByNameBtn(String name) {
        if(name == null) {
            return;
        }
        if(name.equals("")) {
            initTable(contentSender.getByUserId(userCurrent.getUser().getId()));
            return;
        }
        List<Content> finded = contentTable.getItems().stream()
                .filter(c -> c.getName().contains(name))
                .collect(Collectors.toList());
        initTable(finded);
    }

    private void initTable(List<Content> list) {
        if(list != null) {
            name.setCellValueFactory(new PropertyValueFactory<>("name"));
            description.setCellValueFactory(new PropertyValueFactory<>("description"));
            date.setCellValueFactory(new PropertyValueFactory<>("date"));
            author.setCellValueFactory(new PropertyValueFactory<>("author"));
            type.setCellValueFactory(new PropertyValueFactory<>("contentType"));
            contentTable.setItems(FXCollections.observableList(list));
            contentTable.refresh();
        } else {
            AlertWindow.alertWindow.alertWindow("Ошибка");
        }
    }
}
