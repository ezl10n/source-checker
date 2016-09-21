package com.hpe.g11n.sourcechecker.gui.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.WindowConstants;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.gui.tasks.SourceCheckerTask;

/**
 * Created by foy on 2016-08-05.
 */

public class MainViewController extends BaseController  implements Initializable{

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
	
	public String info;
	
	private SourceCheckerTask task;
	
	private String chooseSourcePath;
	private String outputPath;

	@Inject
	@Named("ruleNames")
	List<String> checkBoxs;

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
		if(sourceUrl.getText() != null && !sourceUrl.getText().equals("")){
			if(!sourceUrl.getText().contains(";")){
				File file = new File(sourceUrl.getText());
				if(file.isDirectory()){
					chooseSourcePath = sourceUrl.getText();
				}else{
					int index = sourceUrl.getText().lastIndexOf("\\");
					chooseSourcePath = sourceUrl.getText().substring(0,index);
				}
			}else{
				String[] path = sourceUrl.getText().split(";");
				File file = new File(path[0]);
				if(file.isDirectory()){
					chooseSourcePath = path[0];
				}else{
					int index = path[0].lastIndexOf("\\");
					chooseSourcePath = path[0].substring(0,index);
				}
			}
		}
		if(chooseSourcePath !=null){
			fileChooser.setInitialDirectory(new File(chooseSourcePath));
		}
		List<File> lstFile = fileChooser.showOpenMultipleDialog(root.getScene().getWindow());
		if (lstFile != null) {
			String path = "";
			for(File file:lstFile){
				path = path+file.getAbsolutePath()+";";
			}
			sourceUrl.setText(path.substring(0,path.length()-1));
			chooseSourcePath = getDirectoryPath(path.substring(0,path.length()-1));
		}
//		final DirectoryChooser directoryChooser = new DirectoryChooser();
//	    final File selectedDirectory =
//	            directoryChooser.showDialog(root.getScene().getWindow());
//	    if (selectedDirectory != null) {
//	    	sourceUrl.setText(selectedDirectory.getAbsolutePath());
//	    }
	}

	private String getDirectoryPath(String filePath){
		 String[] s =filePath.split(";");
         return s[0].substring(0, s[0].lastIndexOf("\\"));
	}
	
	@FXML
	public void chooseOutput(ActionEvent event) {
		if(outputUrl.getText() != null && !outputUrl.getText().equals("")){
			outputPath = outputUrl.getText();
			File tempFile = new File(outputPath);
			if(outputPath != null && tempFile.exists()){
				chooser.setInitialDirectory(new File(outputPath));
			}
		}
		File file = chooser.showDialog(root.getScene().getWindow());
		if (file != null) {
			outputUrl.setText(file.getAbsolutePath());
			outputPath = file.getAbsolutePath();
		}
	}

	@FXML
	public void close(ActionEvent event) throws IOException, InterruptedException {
		if(t != null && t.isAlive()){
			Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"Files ware processing,Do you want to cancle?");
			alert.setHeaderText("Warning:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				progressBar.setVisible(false);
//				task.stopChecker();
				t.stop();
			});
		}else{
			System.exit(WindowConstants.DO_NOTHING_ON_CLOSE);
		}
		
	}

	public Parent getRulesConfigView() throws IOException {
		return loadView("fxml/rulesConfigView.fxml", new RulesConfigViewController());
	}
	
	@FXML
	public void rulesConfigPage(ActionEvent event) throws IOException {
		openPage(getRulesConfigView(),"Global Rules Setting!");
	}

	/**
	 * 
	 * @Descripation open a fxml on UI
	 * @CreatedBy: Ali Cao
	 * @Date: 2016年8月18日
	 * @Time: 下午3:06:27
	 * @param parent
	 * @param title
	 */
	public void openPage(Parent parent,String title){
		Stage stage=new Stage();
		stage.setScene(new Scene(parent));
		stage.setResizable(false);
		stage.setTitle(title);
		stage.initModality(Modality.APPLICATION_MODAL);
		stage.initOwner(root.getScene().getWindow());
		stage.show();
	}
	
	@FXML
	public void checker(ActionEvent event) {
		List<Integer> rules = new ArrayList<Integer>();
		for (int i = 0; i < checkRules.getChildren().size(); i++) {
			CheckBox cb = (CheckBox) checkRules.getChildren().get(i);
			if (cb.isSelected()) {
				rules.add(i);
			}
		}
		if(sourceUrl.getText() ==null || "".equals(sourceUrl.getText())){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Please choose the source file!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}else{
			String path = sourceUrl.getText();
			if(path.contains(";")){
				String[] paths =path.split(";");
				for(String subPath:paths){
					File file = new File(subPath);
					if(file.isFile() && !file.exists()){
						int index =subPath.lastIndexOf("\\");
						Alert alert=new Alert(Alert.AlertType.ERROR,"The file ‘"+subPath.substring(index+1,subPath.length())+"’ is not existed, please try again.");
						alert.setHeaderText("Error:");
						alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
							return;
						});
						return;
					}
					if(file.isDirectory()){
						Alert alert=new Alert(Alert.AlertType.ERROR,"It is not file!");
						alert.setHeaderText("Error:");
						alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
							return;
						});
						return;
					}
				}
			}else{
				File file = new File(path);
				if(!file.exists()){
					int index =path.lastIndexOf("\\");
					Alert alert=new Alert(Alert.AlertType.ERROR,"The file ‘"+path.substring(index+1,path.length())+"’ is not existed, please try again.");
					alert.setHeaderText("Error:");
					alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
						return;
					});
					return;
				}
				if(file.isDirectory()){
					Alert alert=new Alert(Alert.AlertType.ERROR,"It is not file!");
					alert.setHeaderText("Error:");
					alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
						return;
					});
					return;
				}
			}
		}
		if(outputUrl.getText() ==null || "".equals(outputUrl.getText())){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Please choose the folder that write output to it!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}else{
			File file = new File(outputUrl.getText());
			if(!file.exists()){
				Alert alert=new Alert(Alert.AlertType.CONFIRMATION,"The folder is not existed ! Create it ?");
				alert.setHeaderText("Confirmation:");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.isPresent() && result.get() == ButtonType.OK) {
					try {
						file.mkdir();
					} catch (Exception e) {
						e.printStackTrace();
						return;
					}
				}
				if (result.isPresent() && result.get() == ButtonType.CANCEL) {
					return;
				}
			}
			
		}
		if(rules.size()==0){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Please select a check point or more than one!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}
		task = new SourceCheckerTask();
		injector.injectMembers(task);
		progressBar.setVisible(true);
		progressBar.progressProperty().bind(task.progressProperty());
		task.setUp(sourceUrl.getText(), outputUrl.getText() + "/", rules);
		t = new Thread(task);
		t.setDaemon(true);
		t.start();
	}
}
