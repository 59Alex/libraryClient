package org.library.alerts;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import org.library.client.Client;
import org.library.client.ToServer;

import java.io.IOException;

public class NewWindow {
    private static NewWindow newWindowOpen;
    private Stage primaryStage;

    private NewWindow(Stage primaryStage){
        this.primaryStage = primaryStage;
    }


    public static void init(Stage primaryStage) {
        newWindowOpen = new NewWindow(primaryStage);
        primaryStage.setOnCloseRequest(w -> esc());
    }
    public static void openWindow(String file)  {
        if(newWindowOpen != null) {
            Parent root = null;
            try {
                root = FXMLLoader.load(newWindowOpen.getClass().getResource(file));
            } catch (IOException e) {
                e.printStackTrace();
            }
            newWindowOpen.getPrimaryStage().setScene(new Scene(root));
            newWindowOpen.getPrimaryStage().show();
        }
    }
    public static void esc() {
        ToServer.esc(Client.getClient().getOut());
        newWindowOpen.getPrimaryStage().close();
    }
    public static NewWindow getNewWindow() {
        return newWindowOpen;
    }
    public Stage getPrimaryStage() {
        return primaryStage;
    }
}
