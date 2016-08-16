package com.hpe.g11n.sourcescoring.gui.control;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcescoring.utils.Constant;
import com.typesafe.config.Config;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;


import java.net.URL;
import java.util.ResourceBundle;

/**
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-16
 * Time: 14:50
 */
public class RulesConfigViewController extends BaseController implements Initializable {
    @FXML
    private Parent root;

    @FXML
    private TextArea concatenationKeyWords;

    @FXML
    private TextField concatenationVariables;

    @FXML
    private TextField camelCaseFormat;

    @Inject
    @Named("sourceScoringConfig")
    Config config;

    public RulesConfigViewController(){

    }

    @FXML
    public void initialize(java.net.URL location, java.util.ResourceBundle resources) {
       concatenationKeyWords.setText(config.getStringList(Constant.CONCATENATION_KEYWORDS_KEY).toString());
        concatenationVariables.setText(config.getStringList(Constant.CONCATENATION_KEYWORDS_VARIABLES).toString());
        camelCaseFormat.setText(config.getString(Constant.CAMELCASE_FORMAT));
    }

    @FXML
    public void saveConfig(ActionEvent event){
        if(logger.isDebugEnabled()){
            logger.debug(concatenationKeyWords.getText());
            logger.debug(concatenationVariables.getText());
            logger.debug(camelCaseFormat.getText());
        }

    }

    @FXML
    public void close(ActionEvent event){
        Event.fireEvent(root.getScene().getWindow(),new WindowEvent(root.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
