package com.hpe.g11n.sourcescoring.gui.control;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.typesafe.config.Config;
import javafx.fxml.FXML;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-16
 * Time: 14:50
 */
public class RulesConfigViewController extends BaseController {

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
    public void cancelAndClose(ActionEvent event){

    }

}
