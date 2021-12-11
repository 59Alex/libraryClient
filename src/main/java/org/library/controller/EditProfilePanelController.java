package org.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import org.library.ServerSender.UserSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.UserCurrent;
import org.library.model.Role;
import org.library.model.RoleEnum;
import org.library.model.User;

import java.net.URL;
import java.util.Collections;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditProfilePanelController implements Initializable {
    @FXML
    private TextField saveFirstNameF;
    @FXML
    private TextField saveLastNameF;
    @FXML
    private TextField saveEmailF;
    @FXML
    private TextField savePasswordF;
    @FXML
    private TextField saveUsernameF;
    @FXML
    private ComboBox<String> saveRoleC;
    @FXML
    private ComboBox<Boolean> saveEnabledC;
    @FXML
    private Button saveBtn;
    @FXML
    private TextArea profileA;
    @FXML
    private Button backBtn;

    private UserCurrent userCurrent;
    private UserSender userSender;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.userCurrent = UserCurrent.getUserCurrent();
        this.userSender = new UserSender();
        initCombox();
        profileA.setText(getProfile());


        saveBtn.setOnAction(event -> {
            Boolean saved = onSaveBtn();
            if(saved == null) {
                AlertWindow.alertWindow.alertWindow("Возможно такой username уже занят!");
            }
            if(saved) {
                User user = userSender.findById(userCurrent.getUser().getId());
                if(user != null) {
                    UserCurrent.add(user);
                    profileA.setText(getProfile());
                }
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });
        backBtn.setOnAction(event -> {
            userSender.esc();
            if(userCurrent.getUser().getRoles().stream().findFirst().get().equals(Role.createBuilder().setName(RoleEnum.ADMIN).build())) {
                NewWindow.openWindow("/adminPanel.fxml");
            } else if(userCurrent.getUser().getRoles().stream().findFirst().get().equals(Role.createBuilder().setName(RoleEnum.USER).build())) {
                NewWindow.openWindow("/userPanel.fxml");
            }

        });
    }

    private void initCombox() {
        ObservableList<String> roles = FXCollections.observableArrayList("ADMIN", "USER");
        ObservableList<Boolean> enableds = FXCollections.observableArrayList(true, false);
        this.saveRoleC.getItems().setAll(roles);
        this.saveEnabledC.getItems().setAll(enableds);
    }

    public Boolean onSaveBtn() {
        String firstName = saveFirstNameF.getText();
        String lastName = saveLastNameF.getText();
        String email = saveEmailF.getText();
        String password = savePasswordF.getText();
        String username = saveUsernameF.getText();
        String role = saveRoleC.getSelectionModel().getSelectedItem();
        Boolean enabled = saveEnabledC.getSelectionModel().getSelectedItem();
        if(firstName.equals("") || lastName.equals("") || email.equals("") ||
                password.equals("") || role == null || enabled == null) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
            return false;
        }
        User user = User.createBuilder()
                .setId(userCurrent.getUser().getId())
                .setFirstName(firstName)
                .setLastName(lastName)
                .setEmail(email)
                .setPassword(password)
                .setEnabled(enabled)
                .setUsername(username)
                .build();
        if(RoleEnum.USER.name().equals(role)) {
            user.setRoles(Collections.singleton(Role.createBuilder().setName(RoleEnum.USER).build()));
        } else if(RoleEnum.ADMIN.name().equals(role)) {
            user.setRoles(Collections.singleton(Role.createBuilder().setName(RoleEnum.ADMIN).build()));
        }

        return userSender.update(user);
    }

    private String getProfile() {
        User user = userCurrent.getUser();
        if (user == null) {
            return "Ошибка!";
        } else {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(String.format("First name: %s\n\r", user.getFirstName()));
            stringBuilder.append(String.format("Last name: %s\n\r", user.getLastName()));
            stringBuilder.append(String.format("Email: %s\n\r", user.getEmail()));
            stringBuilder.append(String.format("Username name: %s\n\r", user.getUsername()));
            stringBuilder.append(String.format("Password name: %s\n\r", user.getPassword()));
            stringBuilder.append(String.format("Blocked: %s\n\r", user.getEnabled()));
            for (Role r : user.getRoles()) {
                stringBuilder.append(String.format("Role: %s\n\r", r.getName().name()));
            }

            saveFirstNameF.setText(user.getFirstName());
            saveLastNameF.setText(user.getLastName());
            saveEmailF.setText(user.getEmail());
            savePasswordF.setText(user.getPassword());
            saveUsernameF.setText(user.getUsername());
            Optional<Role> role = user.getRoles().stream().findFirst();
            if(role.isPresent()) {
                saveRoleC.getSelectionModel().select(role.get().getName().name());
            }
            saveEnabledC.getSelectionModel().select(user.getEnabled());

            return stringBuilder.toString();
        }
    }
}
