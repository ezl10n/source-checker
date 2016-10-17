package com.hpe.g11n.sourcechecker.gui.control;

import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.config.guice.ConfigModule;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValueFactory;

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
    @Named("sourceCheckerConfig")
    Config config;

    public RulesConfigViewController(){

    }

    @FXML
    public void initialize(URL location,ResourceBundle resources) {
        concatenationKeyWords.setText(config.getStringList(Constant.CONCATENATION_KEYWORDS_KEY).toString());
        concatenationVariables.setText(config.getStringList(Constant.CONCATENATION_KEYWORDS_VARIABLES).toString());
        camelCaseFormat.setText(config.getStringList(Constant.CAMELCASE_FORMAT).toString());
    }

    @FXML
    public void saveConfig(ActionEvent event){
        if(logger.isDebugEnabled()){
            logger.debug(concatenationKeyWords.getText());
            logger.debug(concatenationVariables.getText());
            logger.debug(camelCaseFormat.getText());
        }

        //TODO just demo, need more work, like rule validate.. e.g. list should use ""
        String keywords=concatenationKeyWords.getText().replaceAll("\\[","").replaceAll("\\]","");
        config=config.withValue(Constant.CONCATENATION_KEYWORDS_KEY, ConfigValueFactory.fromAnyRef(Arrays.asList(keywords.split(","))));

        String varibales=concatenationVariables.getText().replaceAll("\\[","").replaceAll("\\]","");
        config=config.withValue(Constant.CONCATENATION_KEYWORDS_VARIABLES, ConfigValueFactory.fromAnyRef(Arrays.asList(varibales.split(","))));

        String camelCase =camelCaseFormat.getText().replaceAll("\\[","").replaceAll("\\]","");
        config=config.withValue(Constant.CAMELCASE_FORMAT, ConfigValueFactory.fromAnyRef(Arrays.asList(camelCase.split(","))));
        ConfigModule.saveConfig(config);
        close(event);
    }

    @FXML
    public void close(ActionEvent event){
        Event.fireEvent(root.getScene().getWindow(),new WindowEvent(root.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }

}
