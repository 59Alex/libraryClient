package org.library.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import org.library.ServerSender.UserSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.current.UserCurrent;
import org.library.model.*;

import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Optional;

public class AdminUsersPanelController {

    @FXML
    private Button backBtn;
    @FXML
    private Button deleteBtn;
    @FXML
    private TextField findByNameF;
    @FXML
    private TextField findByIdF;
    @FXML
    private Button findByNameBtn;
    @FXML
    private Button findByIdBtn;
    @FXML
    private TableView<User> usersTable;
    @FXML
    private TableColumn<User, Long> id;
    @FXML
    private TableColumn<User, String> firstName;
    @FXML
    private TableColumn<User, String> lastName;
    @FXML
    private TableColumn<User, String> username;
    @FXML
    private TableColumn<User, String> password;
    @FXML
    private TableColumn<User, String> email;
    @FXML
    private TableColumn<User, String> enabled;
    @FXML
    private TableColumn<User, String> role;
    @FXML
    private TextField updateFirstNameF;
    @FXML
    private TextField updateLastNameF;
    @FXML
    private TextField updateEmailF;
    @FXML
    private TextField updatePasswordF;
    @FXML
    private ComboBox<String> updateRoleC;
    @FXML
    private ComboBox<Boolean> updateEnabledC;
    @FXML
    private Button updateBtn;
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

    private UserSender userSender;
    @FXML
    void initialize() {
        initCombox();
        this.userSender = new UserSender();
        initTable(this.userSender.getAll());

        usersTable.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            if(newValue != null) {
                onSelectedRow(newValue);
            }
        }));
        backBtn.setOnAction(event -> {
            userSender.esc();
            NewWindow.openWindow("/adminPanel.fxml");
        });

        findByIdBtn.setOnAction(event -> {
            String id = findByIdF.getText();
            User user = onFindByIdBtn(id);
            if(user == null) {
                return;
            }
            initTable(Collections.singletonList(user));
        });

        findByNameBtn.setOnAction(event -> {
            List<User> users = onFindByName();
            if(users == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            initTable(users);
        });

        updateBtn.setOnAction(event -> {
            User user = usersTable.getSelectionModel().getSelectedItem();
            Boolean updated = onUpdateBtn(user);
            if(updated == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(updated) {
                initTable(userSender.getAll());
                usersTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });

        deleteBtn.setOnAction(event -> {
            User user = usersTable.getSelectionModel().getSelectedItem();
            if(user == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            Boolean deleted = onDeleteBtn(user.getId());
            if(deleted == null) {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
                return;
            }
            if(deleted) {
                initTable(userSender.getAll());
                usersTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });
        saveBtn.setOnAction(event -> {
            Boolean saved = onSaveBtn();
            if(saved == null) {
                AlertWindow.alertWindow.alertWindow("Возможно такой username уже занят!");
            }
            if(saved) {
                initTable(userSender.getAll());
                usersTable.refresh();
            } else {
                AlertWindow.alertWindow.alertWindow("Ошибка!");
            }
        });
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

        return userSender.save(user);
    }

    public Boolean onDeleteBtn(Long id) {
        if(id == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        return userSender.deleteByKey(id);
    }

    public Boolean onUpdateBtn(User user) {
        if(user == null) {
            AlertWindow.alertWindow.alertWindow("Выберите элемент");
            return false;
        }
        String firstName = updateFirstNameF.getText();
        String lastName = updateLastNameF.getText();
        String email = updateEmailF.getText();
        String password = updatePasswordF.getText();
        String role = updateRoleC.getSelectionModel().getSelectedItem();
        Boolean enabled = updateEnabledC.getSelectionModel().getSelectedItem();
        if(firstName.equals("") || lastName.equals("") || email.equals("") ||
        password.equals("") || role == null || enabled == null) {
            AlertWindow.alertWindow.alertWindow("Заполните все поля!");
        }
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        user.setEnabled(enabled);
        if(RoleEnum.USER.name().equals(role)) {
            user.setRoles(Collections.singleton(Role.createBuilder().setName(RoleEnum.USER).build()));
        } else if(RoleEnum.ADMIN.name().equals(role)) {
            user.setRoles(Collections.singleton(Role.createBuilder().setName(RoleEnum.ADMIN).build()));
        }
        return userSender.update(user);
    }

    public List<User> onFindByName() {
        String name = findByNameF.getText();
        if(name.equals("")) {
            initTable(this.userSender.getAll());
        }
        return userSender.findByName(name);
    }
    public User onFindByIdBtn(String idS) {
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
        return userSender.findById(id);
    }

    public void onSelectedRow(User user) {
        updateFirstNameF.setText(user.getFirstName());
        updateLastNameF.setText(user.getLastName());
        updateEmailF.setText(user.getEmail());
        updatePasswordF.setText(user.getPassword());
        Optional<Role> role = user.getRoles().stream().findFirst();
        if(role.isPresent()) {
            updateRoleC.getSelectionModel().select(role.get().getName().name());
        }
        updateEnabledC.getSelectionModel().select(user.getEnabled());
    }

    private void initCombox() {
        ObservableList<String> roles = FXCollections.observableArrayList("ADMIN", "USER");
        ObservableList<Boolean> enableds = FXCollections.observableArrayList(true, false);
        this.saveRoleC.getItems().setAll(roles);
        this.saveEnabledC.getItems().setAll(enableds);
        this.updateRoleC.getItems().setAll(roles);
        this.updateEnabledC.getItems().setAll(enableds);
    }

    private void initTable(List<User> list) {
        if(list != null) {
            id.setCellValueFactory(new PropertyValueFactory("id"));
            firstName.setCellValueFactory(new PropertyValueFactory("firstName"));
            lastName.setCellValueFactory(new PropertyValueFactory("lastName"));
            username.setCellValueFactory(new PropertyValueFactory("username"));
            password.setCellValueFactory(new PropertyValueFactory("password"));
            email.setCellValueFactory(new PropertyValueFactory("email"));
            enabled.setCellValueFactory(new PropertyValueFactory("enabled"));
            role.setCellValueFactory(new PropertyValueFactory("roles"));
            usersTable.setItems(FXCollections.observableList(list));
        } else {
            AlertWindow.alertWindow.alertWindow("Ошибка");
        }
    }
}
