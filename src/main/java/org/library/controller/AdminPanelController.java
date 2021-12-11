package org.library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.DirectoryChooser;
import org.library.alerts.NewWindow;
import org.library.client.Client;
import org.library.client.ToServer;
import org.library.current.UserCurrent;
import org.library.model.Role;
import org.library.model.User;

import java.io.File;

public class AdminPanelController {

    @FXML
    private Button bookListBtn;
    @FXML
    private Button journalListBtn;
    @FXML
    private Button newspaperListBtn;
    @FXML
    private Button clientsListBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private TextArea infoArea;
    @FXML
    private Button statisticBtn;

    private UserCurrent userCurrent;
    private DirectoryChooser directoryChooser;

    @FXML
    void initialize() {
        userCurrent = UserCurrent.getUserCurrent();
        infoArea.appendText(getProfile());
        this.directoryChooser = new DirectoryChooser();
        bookListBtn.setOnAction(event -> {
            NewWindow.openWindow("/adminBooksPanel.fxml");
        });
        journalListBtn.setOnAction(event -> {
            NewWindow.openWindow("/adminJournalPanel.fxml");
        });
        newspaperListBtn.setOnAction(event -> {
            NewWindow.openWindow("/adminNewspaperPanel.fxml");
        });
        clientsListBtn.setOnAction(event -> {
            NewWindow.openWindow("/adminUsersPanel.fxml");
        });
        profileBtn.setOnAction(event -> {
            NewWindow.openWindow("/editProfilePanel.fxml");
        });
        statisticBtn.setOnAction(event -> {
            NewWindow.openWindow("/statisticPanel.fxml");
        });
        exitBtn.setOnAction(event -> {
            NewWindow.esc();
        });
    }

    private String getProfile() {
        User user = userCurrent.getUser();
        if(user == null) {
            return "Ошибка!";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("First name: %s\n\r", user.getFirstName()));
            stringBuilder.append(String.format("Last name: %s\n\r", user.getLastName()));
            stringBuilder.append(String.format("Email: %s\n\r", user.getEmail()));
            stringBuilder.append(String.format("Username name: %s\n\r", user.getUsername()));
            stringBuilder.append(String.format("Password name: %s\n\r", user.getPassword()));
            stringBuilder.append(String.format("Blocked: %s\n\r", user.getEnabled()));
            for(Role r : user.getRoles()) {
                stringBuilder.append(String.format("Role: %s\n\r", r.getName().name()));
            }

            return stringBuilder.toString();
        }
    }

}
