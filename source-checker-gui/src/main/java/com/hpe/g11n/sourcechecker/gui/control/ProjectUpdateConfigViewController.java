package com.hpe.g11n.sourcechecker.gui.control;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import com.hpe.g11n.sourcechecker.config.guice.ProjectConfigModule;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;
import com.hpe.g11n.sourcechecker.utils.constant.RulePatternConstant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValueFactory;

/**
 * 
 * 
 * @Descripation
 * @CreatedBy: Ali Cao
 * @Date: 2016年10月31日
 * @Time: 上午10:31:08
 *
 */
public class ProjectUpdateConfigViewController extends BaseController implements
		Initializable {
	@FXML
	private Parent root;
	
	@FXML
	private Label lab_DateTimeFormat;
	
	@FXML
	private TextField product;
	
	@FXML
	private TextArea concatenation;

	@FXML
	private TextArea camelCase;

	@FXML
	private TextArea dateTimeFormat;

	@FXML
	private TextArea capital;

	@FXML
	private TextArea spelling;

	Config config;
	
	private String projectName;

	public ProjectUpdateConfigViewController(String projectName) {
		this.projectName = projectName;
	}

	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		lab_DateTimeFormat.setText("Date&Time Format:");
		concatenation.setWrapText(true);
		camelCase.setWrapText(true);
		dateTimeFormat.setWrapText(true);
		capital.setWrapText(true);
		spelling.setWrapText(true);
		config = ProjectConfigModule.loadConfig(projectName);
		product.setText(projectName);
		concatenation.setText(config.getStringList(Constant.CONCATENATION_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		camelCase.setText(config.getStringList(Constant.CAMELCASE_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		dateTimeFormat.setText(config.getStringList(Constant.DATETIMEFORMAT_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		capital.setText(config.getStringList(Constant.CAPITAL_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		spelling.setText(config.getStringList(Constant.SPELLING_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	}

	@FXML
	public void updateConfig(ActionEvent event) {
		String newProjectName = product.getText();
        if(newProjectName.contains("_")){
        	newProjectName = newProjectName.replaceAll("_", "-");
        }
		if (logger.isDebugEnabled()) {
			logger.debug(newProjectName);
			logger.debug(concatenation.getText());
			logger.debug(camelCase.getText());
			logger.debug(dateTimeFormat.getText());
			logger.debug(capital.getText());
			logger.debug(spelling.getText());
		}
        if(newProjectName == null || "".equals(newProjectName)){
        	Alert alert=new Alert(Alert.AlertType.ERROR,MessageConstant.PRODUCT_NAME_MSG1);
			alert.setHeaderText(MessageConstant.ERROR);
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
        }
        if(StringUtil.pattern(newProjectName, RulePatternConstant.PRODUCT_FORMAT)){
        	Alert alert=new Alert(Alert.AlertType.ERROR,MessageConstant.PRODUCT_FORMAT_MSG);
			alert.setHeaderText(MessageConstant.ERROR);
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
        }
        
        
		String concatenation_words = StringUtil.getChangedString(concatenation
				.getText());
		if (!concatenation_words.equals("")) {
			config = config.withValue(Constant.CONCATENATION_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(Arrays
							.asList(concatenation_words.split(",")))));
		} else {
			config = config.withValue(Constant.CONCATENATION_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}

		String camelCase_words = StringUtil.getChangedString(camelCase
				.getText());
		if (!camelCase_words.equals("")) {
			config = config.withValue(Constant.CAMELCASE_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(Arrays.asList(camelCase_words
							.split(",")))));
		} else {
			config = config.withValue(Constant.CAMELCASE_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}
		
		String dateFormat_words = StringUtil.getChangedString(dateTimeFormat
				.getText());
		if (!dateFormat_words.equals("")) {
			config = config.withValue(Constant.DATETIMEFORMAT_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(Arrays.asList(dateFormat_words
							.split(",")))));
		} else {
			config = config.withValue(Constant.DATETIMEFORMAT_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}
		
		String capital_words = StringUtil.getChangedString(capital
				.getText());
		if (!capital_words.equals("")) {
			config = config.withValue(Constant.CAPITAL_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(Arrays.asList(capital_words
							.split(",")))));
		} else {
			config = config.withValue(Constant.CAPITAL_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}

		String spelling_words = StringUtil.getChangedString(spelling
				.getText());
		if (!spelling_words.equals("")) {
			config = config.withValue(Constant.SPELLING_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(Arrays.asList(spelling_words
							.split(",")))));
		} else {
			config = config.withValue(Constant.SPELLING_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}

		if(!"".equals(newProjectName)){
			ProjectConfigModule.saveConfig(config,newProjectName);
		}
		if(!newProjectName.equals(projectName)){
			ProjectConfigModule.deleteConfig(projectName);
		}
		
		Alert alert=new Alert(Alert.AlertType.INFORMATION,MessageConstant.UPDATE_MSG1);
		alert.setHeaderText(MessageConstant.INFORMATION);
		alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
			close(event);
			return;
		});
		
		close(event);
	}

	@FXML
	public void close(ActionEvent event) {
		Event.fireEvent(root.getScene().getWindow(), new WindowEvent(root
				.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	public Parent getImportView() throws IOException {
		return loadView("fxml/importView.fxml", new ProjectConfigImportViewController(projectName));
	}
	
	@FXML
	public void importView(ActionEvent event) throws IOException {
		openPage(getImportView(),MessageConstant.IMPORT);
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
	public void refresh(ActionEvent event) throws IOException {
		config = ProjectConfigModule.loadConfig(projectName);
		product.setText(projectName);
		concatenation.setText(config.getStringList(Constant.CONCATENATION_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		camelCase.setText(config.getStringList(Constant.CAMELCASE_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		dateTimeFormat.setText(config.getStringList(Constant.DATETIMEFORMAT_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		capital.setText(config.getStringList(Constant.CAPITAL_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
		spelling.setText(config.getStringList(Constant.SPELLING_PATH)
				.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
	}
}
