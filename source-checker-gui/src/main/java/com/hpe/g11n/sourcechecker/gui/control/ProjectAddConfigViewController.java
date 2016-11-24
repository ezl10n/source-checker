package com.hpe.g11n.sourcechecker.gui.control;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.WindowEvent;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.hpe.g11n.sourcechecker.config.guice.TempletConfigModule;
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
 * @Date: 2016年10月28日
 * @Time: 上午10:31:08
 *
 */
public class ProjectAddConfigViewController extends BaseController implements
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

	@Inject
	@Named("templetConfig")
	Config config;
	

	public ProjectAddConfigViewController() {
	}

	@FXML
	public void initialize(URL location, ResourceBundle resources) {
		lab_DateTimeFormat.setText("Date&Time Format:");
		concatenation.setWrapText(true);
		camelCase.setWrapText(true);
		dateTimeFormat.setWrapText(true);
		capital.setWrapText(true);
		spelling.setWrapText(true);
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
	public void saveConfig(ActionEvent event) {
		String projectName = product.getText();
        if(projectName.contains("_")){
        	projectName = projectName.replaceAll("_", "-");
        }
		if (logger.isDebugEnabled()) {
			logger.debug(projectName);
			logger.debug(concatenation.getText());
			logger.debug(camelCase.getText());
			logger.debug(dateTimeFormat.getText());
			logger.debug(capital.getText());
			logger.debug(spelling.getText());
		}
        if(projectName == null || "".equals(projectName)){
        	Alert alert=new Alert(Alert.AlertType.ERROR,MessageConstant.PRODUCT_NAME_MSG1);
			alert.setHeaderText(MessageConstant.ERROR);
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
        }
        
        if(StringUtil.pattern(projectName, RulePatternConstant.PRODUCT_FORMAT)){
        	Alert alert=new Alert(Alert.AlertType.ERROR,MessageConstant.PRODUCT_FORMAT_MSG);
			alert.setHeaderText(MessageConstant.ERROR);
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
        }
		
		String concatenation_words = StringUtil.getChangedString(
				concatenation.getText());
		if (!concatenation_words.equals("")) {
			config = config.withValue(Constant.CONCATENATION_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(
							Arrays.asList(concatenation_words.split(",")))));
		} else {
			config = config.withValue(Constant.CONCATENATION_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}

		String camelCase_words = StringUtil.getChangedString(
				camelCase.getText());
		if (!camelCase_words.equals("")) {
			config = config.withValue(Constant.CAMELCASE_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(
							Arrays.asList(camelCase_words.split(",")))));
		} else {
			config = config.withValue(Constant.CAMELCASE_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}
		
		String dateFormat_words = StringUtil.getChangedString(
				dateTimeFormat.getText());
		if (!dateFormat_words.equals("")) {
			config = config.withValue(Constant.DATETIMEFORMAT_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(
							Arrays.asList(dateFormat_words.split(",")))));
		} else {
			config = config.withValue(Constant.DATETIMEFORMAT_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}
		
		String capital_words = StringUtil.getChangedString(
				capital.getText());
		if (!capital_words.equals("")) {
			config = config.withValue(Constant.CAPITAL_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(
							Arrays.asList(capital_words.split(",")))));
		} else {
			config = config.withValue(Constant.CAPITAL_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}

		String spelling_words = StringUtil.getChangedString(
				spelling.getText());
		if (!spelling_words.equals("")) {
			config = config.withValue(Constant.SPELLING_PATH,
					ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(
							Arrays.asList(spelling_words.split(",")))));
		} else {
			config = config.withValue(Constant.SPELLING_PATH,
					ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		}

		TempletConfigModule.saveConfig(config);

		String templetPath = getTempletConfigPath();
		String projectPath = getProjectConfigPath(projectName);
		copyFile(templetPath, projectPath);
		
		//roll back the project templet config 
		config = config.withValue(Constant.CONCATENATION_PATH,
				ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		config = config.withValue(Constant.CAMELCASE_PATH,
				ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		config = config.withValue(Constant.DATETIMEFORMAT_PATH,
				ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		config = config.withValue(Constant.CAPITAL_PATH,
				ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		config = config.withValue(Constant.SPELLING_PATH,
				ConfigValueFactory.fromAnyRef(new ArrayList<String>()));
		
		TempletConfigModule.saveConfig(config);
		
		Alert alert=new Alert(Alert.AlertType.INFORMATION,MessageConstant.REFRESH_MSG1);
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

	public String getTempletConfigPath() {
//		String preFix = String.format(MessageConstant.SOURCE_CONFIG_PATH,File.separator);
//		String fileName = String.format(MessageConstant.TEMPLET_CONFIG_NAME,File.separator);
//		String passInDir = System.getProperty(MessageConstant.SOURCE_CONFIG_DIR);
//		if (passInDir == null) {
//			passInDir = System.getProperty(MessageConstant.USER_DIR);
//			fileName = preFix + fileName;
//		}
//		return fileName;
		
		String baseDir=System.getProperty(MessageConstant.SOURCE_CONFIG_DIR);
		String fileName = String.format(MessageConstant.TEMPLET_CONFIG_NAME,File.separator);
	        if(baseDir == null || baseDir.isEmpty()){
	            String subDir = String.format(MessageConstant.SOURCE_CONFIG_PATH, File.separator);
	            baseDir=System.getProperty(MessageConstant.USER_DIR) + subDir;
	        }
	        return baseDir + fileName;
	}

	public String getProjectConfigPath(String projectName) {
//		String preFix = String.format(MessageConstant.PROJECT_CONFIG_PATH,File.separator);
//		String path = "%1$s" + projectName + ".conf";
//		String fileName = String.format(path, File.separator);
//		String passInDir = System.getProperty(MessageConstant.SOURCE_CONFIG_DIR);
//		if (passInDir == null) {
//			passInDir = System.getProperty(MessageConstant.USER_DIR);
//			fileName = preFix + fileName;
//		}
//		return fileName;
		
		 String baseDir=System.getProperty(MessageConstant.PROJECT_CONFIG_DIR);
		 String path = "%1$s" + projectName + ".conf";
			String fileName = String.format(path, File.separator);
	        if(baseDir == null || baseDir.isEmpty()){
	            String subDir = String.format(MessageConstant.PROJECT_CONFIG_PATH, File.separator);
	            baseDir=System.getProperty(MessageConstant.USER_DIR) + subDir;
	        }
	        return baseDir + fileName;
	}

	public void copyFile(String sourcePath, String targetPath) {
		try {
			File sourceFile = new File(sourcePath);
			File targetFile = new File(targetPath);
			byte[] b = new byte[(int) sourceFile.length()];
			FileInputStream is = new FileInputStream(sourceFile);
			FileOutputStream ps = new FileOutputStream(targetFile);
			is.read(b);
			ps.write(b);
			is.close();
			ps.close();
		} catch (ArrayIndexOutOfBoundsException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
