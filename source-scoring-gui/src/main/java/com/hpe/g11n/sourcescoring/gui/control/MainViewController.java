package com.hpe.g11n.sourcescoring.gui.control;


import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcescoring.gui.tasks.SourceScoringTask;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Created by foy on 2016-08-05.
 */

public class MainViewController implements Initializable {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    @FXML
    private Parent root;

    @FXML
    private TextField sourceUrl;

    @FXML
    private TextField outputUrl;

    @FXML
    private HBox checkRules;

    @FXML
    private ProgressBar progressBar;

    private DirectoryChooser chooser;

    private FileChooser fileChooser;

    @Inject
    @Named("ruleNames")
    List<String> checkBoxs;

    @Inject
    SourceScoringTask task;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(chooser == null){
            chooser = new DirectoryChooser();
        }
        if(fileChooser == null){
            fileChooser=new FileChooser();
        }
        checkBoxs.forEach(checkBoxValue -> {
            checkRules.getChildren().add(new CheckBox(checkBoxValue));
        });
    }

    @FXML
    public void chooseSource(ActionEvent event){
        File file = fileChooser.showOpenDialog(root.getScene().getWindow());
        if (file != null) {
            sourceUrl.setText(file.getAbsolutePath());
        }
    }
    @FXML
    public void chooseOutput(ActionEvent event){
        File file = chooser.showDialog(root.getScene().getWindow());
        if (file != null) {
            outputUrl.setText(file.getAbsolutePath());
        }
    }

    @FXML
    public void scoring(ActionEvent event){
        List<Integer> rules=new ArrayList<Integer>();
        for(int i=0;i < checkRules.getChildren().size();i++){
            CheckBox cb=(CheckBox)checkRules.getChildren().get(i);
            if(cb.isSelected()){
                rules.add(i);
            }
        }

        progressBar.progressProperty().bind(task.progressProperty());
        task.setUp(sourceUrl.getText(), outputUrl.getText() + "/" + getFileName(sourceUrl.getText()) + ".csv", rules);
        Thread t =new Thread(task);
        t.setDaemon(true);
        t.start();
    }
    
    public String getFileName(String filePath){
    	File file = new File(filePath);
    	String name = file.getName();
    	int index = name.lastIndexOf(".");
    	return name.substring(0,index);
    }
}
