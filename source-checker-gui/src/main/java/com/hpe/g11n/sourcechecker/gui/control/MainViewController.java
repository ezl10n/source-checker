package com.hpe.g11n.sourcechecker.gui.control;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.swing.WindowConstants;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hp.g11n.jassolo.utils.PslUtils;
import com.hp.g11n.sdl.psl.interop.core.IPassoloApp;
import com.hp.g11n.sdl.psl.interop.core.IPslProject;
import com.hp.g11n.sdl.psl.interop.core.impl.impl.PassoloApp;
import com.hpe.g11n.sourcechecker.gui.tasks.SourceCheckerTask;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;

/**
 * Created by foy on 2016-08-05.
 */

public class MainViewController extends BaseController  implements Initializable{
	private final Logger log = LoggerFactory.getLogger(getClass());
	@FXML
	private Parent root;

	@FXML
	private TextField projectName;
	
	@FXML
	private TextField projectVersion;
	
	@FXML
	private ChoiceBox state;
	
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
	
	private int closeCount =0;

	@Inject
	@Named("ruleNames")
	List<String> checkBoxs;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		state.setItems(FXCollections.observableArrayList(Constant.STATE_ALL, Constant.STATE_NEW_CHANGED));
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
				if(PslUtils.isPassoloStarted() && closeCount ==0){
					PslUtils.killPassolo();
					closeCount++;
				}
				if(!PslUtils.isPassoloStarted()){
					closeCount = 0;
				}
				t.stop();
			});
		}else{
			if(PslUtils.isPassoloStarted() && closeCount ==0){
				PslUtils.killPassolo();
				closeCount++;
			}
			System.exit(WindowConstants.DO_NOTHING_ON_CLOSE);
			if(!PslUtils.isPassoloStarted()){
				closeCount = 0;
			}
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
		if(PslUtils.isPassoloStarted()){ 
		    Alert alert=new Alert(Alert.AlertType.INFORMATION,"We need close Passolo now. Passolo is closing...");
			alert.setHeaderText("Infomation:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				 logger.debug("MainViewController: about to shutdown passolo [{}]");
			});
			return;
		}
		if(projectName.getText() == null || "".equals(projectName.getText())){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Project name is not empty!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}
		if(!StringUtil.formatRight(projectName.getText())){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Product's format is not correct, it is not be contains \"\\\",\"/\",\"<\" and \">\"!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}
		if(projectVersion.getText() == null || "".equals(projectVersion.getText())){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Project version is not empty!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}
		if(!StringUtil.formatRight(projectVersion.getText())){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Version's format is not correct, it is not be contains \"\\\",\"/\",\"<\" and \">\"!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}
		if(state.getSelectionModel().selectedItemProperty().getValue() == null){
			Alert alert=new Alert(Alert.AlertType.ERROR,"Please choose the state!");
			alert.setHeaderText("Error:");
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}
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
		
//		String[] paths = sourceUrl.getText().split(";");
//		IPslProject project = null;
//		for(String filePath:paths){
//			if(filePath.endsWith(".lpu")){
//				try{
//					IPassoloApp app = PassoloApp.getInstance();
//					project = app.open(filePath);
//					project.getSourceLists();
//					} catch (Exception ex) {
//						log.debug("Failed to check, the version of Passolo is not 2011");
//						log.error("LPUFileParser exception:" + ex.getMessage());
//						Alert alert=new Alert(Alert.AlertType.ERROR,"Failed to check, the version of Passolo is not 2011");
//						alert.setHeaderText("Error:");
//						alert.showAndWait();
//						if(project != null){
//							project = null;
////							project.close();
//						}
//						IPassoloApp.quit();
//						return;
//					} finally {
//						if(project != null){
//							project.close();
//						}
//						if(PslUtils.isPassoloStarted()){
//							IPassoloApp.quit();
//						}
//					}
//				break;
//			}
//		}
		String s = state.getSelectionModel().selectedItemProperty().getValue().toString();
		task = new SourceCheckerTask();
		injector.injectMembers(task);
		progressBar.setVisible(true);
		progressBar.progressProperty().bind(task.progressProperty());
		task.setUp(projectName.getText(),projectVersion.getText(),s,sourceUrl.getText(), outputUrl.getText() + "/", rules);
		t = new Thread(task);
		t.setDaemon(true);
		t.start();
		closeCount = 0;
	}
	
}
