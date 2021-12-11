package org.library.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import org.library.ServerSender.AuthSender;
import org.library.alerts.AlertWindow;
import org.library.alerts.NewWindow;
import org.library.client.Client;
import org.library.client.ToServer;
import org.library.current.UserCurrent;
import org.library.model.Role;
import org.library.model.RoleEnum;
import org.library.model.User;

import javax.swing.text.View;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.Set;

public class LoginController {
    /*@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;*/

    @FXML
    private TextField loginTextField;

    @FXML
    private TextField passwordField;

    @FXML
    private Button loginButton;

    private AuthSender authSender;

    @FXML
    void initialize() {
        if(authSender == null) {
            authSender = new AuthSender();
        }
        textFieldInitialize();
        loginButton.setOnAction(event -> {
            confirmLoginAndPassword();
            textFieldInitialize();
        });
    }

    private void confirmLoginAndPassword(){

        String login =  loginTextField.getText();
        String password = passwordField.getText();
        System.out.println(login + " " + password);
        if(!login.equals("") && !password.equals("")) {
            boolean auth = authSender.auth(login, password);
            if(!auth) {
                AlertWindow.alertWindow.alertWindow("Не правильный логин или пароль!");
            } else {
                Set<Role> roles = UserCurrent.getUserCurrent().getUser().getRoles();
                if(roles != null) {
                    if(!UserCurrent.getUserCurrent().getUser().getEnabled()) {
                        AlertWindow.alertWindow.alertWindow("Ваш аккаунт заблокирован!");
                        return;
                    }
                    if(roles.contains(Role.createBuilder().setName(RoleEnum.ADMIN).build())) {
                        NewWindow.openWindow("/adminPanel.fxml");
                    } else if(roles.contains(Role.createBuilder().setName(RoleEnum.USER).build())) {
                        NewWindow.openWindow("/userPanel.fxml");
                    }

                }
            }

        } else{
            AlertWindow.alertWindow.alertWindow("Заполните все ячейки !");
        }
    }

    private void textFieldInitialize(){
        loginTextField.setText("");
        passwordField.setText("");
    }

}
