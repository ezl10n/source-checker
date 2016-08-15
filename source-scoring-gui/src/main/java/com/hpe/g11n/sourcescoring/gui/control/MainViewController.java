package com.hpe.g11n.sourcescoring.gui.control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.swing.WindowConstants;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcescoring.config.guice.ConfigModule;
import com.hpe.g11n.sourcescoring.core.guice.CoreModule;
import com.hpe.g11n.sourcescoring.gui.guice.GUIModule;
import com.hpe.g11n.sourcescoring.gui.tasks.SourceScoringTask;

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
	private GridPane checkRules;

	@FXML
	private ProgressBar progressBar;

	private DirectoryChooser chooser;

	private FileChooser fileChooser;

	private Thread t;

	@Inject
	@Named("ruleNames")
	List<String> checkBoxs;

	SourceScoringTask task = new SourceScoringTask();

	public MainViewController() {
		Injector injector = Guice.createInjector(new CoreModule(),
				new ConfigModule(), new GUIModule());
		injector.injectMembers(this);
		injector.injectMembers(task);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		if (chooser == null) {
			chooser = new DirectoryChooser();
		}
		if (fileChooser == null) {
			fileChooser = new FileChooser();
		}
		
		int k = 0;
		int j = 0;
		for(String checkBoxValue:checkBoxs){
			checkRules.add(new CheckBox(checkBoxValue), j, k);
			j++;
			if(j==4){
				k++;
				j=0;
			}
		}
			
	}

	@FXML
	public void chooseSource(ActionEvent event) {
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("LPU", "*.lpu"),
                new FileChooser.ExtensionFilter("JSON", "*.json"),
                new FileChooser.ExtensionFilter("properties", "*.properties")
            );
		List<File> lstFile = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
		if (lstFile != null) {
			String path = "";
			for(File file:lstFile){
				path = path+file.getAbsolutePath()+";";
			}
			sourceUrl.setText(path.substring(0,path.length()-1));
		}
	}

	@FXML
	public void chooseOutput(ActionEvent event) {
		File file = chooser.showDialog(root.getScene().getWindow());
		if (file != null) {
			outputUrl.setText(file.getAbsolutePath());
		}
	}

	@FXML
	public void close(ActionEvent event) {
		if (t != null && t.isAlive()) {
			t.stop();
		} else {
			deletedFile(outputUrl.getText());
			System.exit(WindowConstants.DO_NOTHING_ON_CLOSE);
		}
	}

	@FXML
	public void scoring(ActionEvent event) {
		List<Integer> rules = new ArrayList<Integer>();
		for (int i = 0; i < checkRules.getChildren().size(); i++) {
			CheckBox cb = (CheckBox) checkRules.getChildren().get(i);
			if (cb.isSelected()) {
				rules.add(i);
			}
		}

		progressBar.progressProperty().bind(task.progressProperty());
		task.setUp(sourceUrl.getText(), outputUrl.getText() + "/", rules);
		t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}

	public void deletedFile(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if(tempList[i].endsWith(".csv")){
					if (filePath.endsWith(File.separator)) {
						temp = new File(filePath + tempList[i]);
					} else {
						temp = new File(filePath + File.separator + tempList[i]);
					}
					if (temp.isFile()) {
						temp.delete();
					}
				}
			}
		}
	}
}
