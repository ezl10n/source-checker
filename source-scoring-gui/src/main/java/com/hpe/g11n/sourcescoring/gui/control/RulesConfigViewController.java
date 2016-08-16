package com.hpe.g11n.sourcescoring.gui.control;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.stage.WindowEvent;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-16
 * Time: 14:50
 */
public class RulesConfigViewController extends BaseController {
    @FXML
    private Parent root;
    @Inject
    @Named("sourceScoringConfig")
    Config config;

    public RulesConfigViewController(){

    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }
    @FXML
    public void saveConfig(ActionEvent event){

    }

    @FXML
    public void close(ActionEvent event){
        Event.fireEvent(root.getScene().getWindow(),new WindowEvent(root.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
