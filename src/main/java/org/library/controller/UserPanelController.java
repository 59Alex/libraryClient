package org.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.library.alerts.NewWindow;
import org.library.current.UserCurrent;
import org.library.model.Role;
import org.library.model.User;

import java.net.URL;
import java.util.ResourceBundle;

public class UserPanelController implements Initializable {

    @FXML
    private Button bookListBtn;
    @FXML
    private Button journalListBtn;
    @FXML
    private Button newspaperListBtn;
    @FXML
    private Button myContentBtn;
    @FXML
    private Button exitBtn;
    @FXML
    private Button profileBtn;
    @FXML
    private TextArea infoArea;
    private UserCurrent userCurrent;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userCurrent = UserCurrent.getUserCurrent();
        infoArea.appendText(getProfile());
        exitBtn.setOnAction(event -> {
            NewWindow.esc();
        });
        bookListBtn.setOnAction(event -> {
            NewWindow.openWindow("/userBooksPanel.fxml");
        });
        journalListBtn.setOnAction(event -> {
            NewWindow.openWindow("/userJournalsPanel.fxml");
        });
        newspaperListBtn.setOnAction(event -> {
            NewWindow.openWindow("/userNewspaperPanel.fxml");
        });
        myContentBtn.setOnAction(event -> {
            NewWindow.openWindow("/userContentPanel.fxml");
        });
        profileBtn.setOnAction(event -> {
            NewWindow.openWindow("/editProfilePanel.fxml");
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
