package org.library.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import org.library.alerts.NewWindow;
import org.library.current.CurrentBody;
import org.library.current.UserCurrent;

public class BodyController {

    @FXML
    private Button back;
    @FXML
    private TextArea body;

    @FXML
    void initialize() {
        body.setText(CurrentBody.getBodyCuurent().getBody());
        back.setOnAction(event -> {
            NewWindow.openWindow(CurrentBody.getBodyCuurent().getBackPage());
        });
    }
}
