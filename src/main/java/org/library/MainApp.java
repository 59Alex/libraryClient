package org.library;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.library.alerts.NewWindow;
import org.library.client.Client;

import java.net.URL;

public class MainApp extends Application {

    public static void main(String[] args) {
        Client.start();
        Application.launch();
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        NewWindow.init(primaryStage);
        NewWindow.openWindow("/log.fxml");
    }
}
