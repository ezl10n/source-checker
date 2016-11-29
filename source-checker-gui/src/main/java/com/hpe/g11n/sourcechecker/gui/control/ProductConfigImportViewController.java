package com.hpe.g11n.sourcechecker.gui.control;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.WindowEvent;

import com.hpe.g11n.sourcechecker.config.guice.ProductConfigModule;
import com.hpe.g11n.sourcechecker.utils.ExcelPoiUtils;
import com.hpe.g11n.sourcechecker.utils.StringUtil;
import com.hpe.g11n.sourcechecker.utils.constant.Constant;
import com.hpe.g11n.sourcechecker.utils.constant.MessageConstant;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigValueFactory;

public class ProductConfigImportViewController extends BaseController implements Initializable {

	@FXML
	private Parent root;
	
	@FXML
	private TextField filePath;
	
	private FileChooser fileChooser;
	private String chooseSourcePath;
	Config config;
	String product;
	
	List<String> lstConcatenation;
	List<String> lstCamelCase;
	List<String> lstDateTimeFormat;
	List<String> lstCapital;
	List<String> lstSpelling;
	
	public ProductConfigImportViewController(String product){
		this.product = product;
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		if (fileChooser == null) {
			fileChooser = new FileChooser();
		}
		config = StringUtil.loadConfig(product,null);
		lstConcatenation = config.getStringList(Constant.CONCATENATION_PATH);
		lstCamelCase = config.getStringList(Constant.CAMELCASE_PATH); 
		lstDateTimeFormat = config.getStringList(Constant.DATETIMEFORMAT_PATH); 
		lstCapital = config.getStringList(Constant.CAPITAL_PATH); 
		lstSpelling = config.getStringList(Constant.SPELLING_PATH); 
	}
	
	@FXML
	public void chooseExcel(ActionEvent event) {
		fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("xls", "*.xls")
            );
		if(filePath.getText() != null && !filePath.getText().equals("")){
			int index = filePath.getText().lastIndexOf("\\");
			chooseSourcePath = filePath.getText().substring(0, index);
		}
		if(chooseSourcePath !=null){
			fileChooser.setInitialDirectory(new File(chooseSourcePath));
		}
		File file = fileChooser.showOpenDialog(root.getScene().getWindow());
		if(file != null){
			filePath.setText(file.getAbsolutePath());
		}
	}
	
	@FXML
	public void close(ActionEvent event) {
		Event.fireEvent(root.getScene().getWindow(), new WindowEvent(root
				.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
	}
	
	@FXML
	public void update(ActionEvent event) {
		String path = filePath.getText();
		if(path == null || "".equals(path)){
			Alert alert=new Alert(Alert.AlertType.ERROR,MessageConstant.IMPORT_MSG1);
			alert.setHeaderText(Constant.ERROR);
			alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
				return;
			});
			return;
		}
		Map<String,List<String>> map = ExcelPoiUtils.getExcelContent(path);
		
		//from excel
		List<String> lstTempConcatenation = map.get("lstConcatenation");
		List<String> lstTempCamelCase = map.get("lstCamelCase");
		List<String> lstTempDateTimeFormat = map.get("lstDateTimeFormat");
		List<String> lstTempCapital = map.get("lstCapital");
		List<String> lstTempSpelling = map.get("lstSpelling");
		List<String> lstCount = map.get("lstCount");
		
		List<String> lstInvalid = new ArrayList<String>();
		lstInvalid.addAll(lstTempConcatenation);
		lstInvalid.addAll(lstTempCamelCase);
		lstInvalid.addAll(lstTempDateTimeFormat);
		lstInvalid.addAll(lstTempCapital);
		lstInvalid.addAll(lstTempSpelling);
		
		//from white list
		List<String> lstTotal = new ArrayList<String>();
		lstTotal.addAll(lstConcatenation);
		lstTotal.addAll(lstCamelCase);
		lstTotal.addAll(lstDateTimeFormat);
		lstTotal.addAll(lstCapital);
		lstTotal.addAll(lstSpelling);
		
		lstConcatenation.addAll(lstTempConcatenation);
		lstCamelCase.addAll(lstTempCamelCase);
		lstDateTimeFormat.addAll(lstTempDateTimeFormat);
		lstCapital.addAll(lstTempCapital);
		lstSpelling.addAll(lstTempSpelling);
		
		// union total
		List<String> lstUnionTotal = new ArrayList<String>();
		lstUnionTotal.addAll(lstConcatenation);
		lstUnionTotal.addAll(lstCamelCase);
		lstUnionTotal.addAll(lstDateTimeFormat);
		lstUnionTotal.addAll(lstCapital);
		lstUnionTotal.addAll(lstSpelling);
		
		List<String> lstUnionUniqueTotal = new ArrayList<String>();
		lstUnionUniqueTotal.addAll(StringUtil.getUniqueList(lstConcatenation));
		lstUnionUniqueTotal.addAll(StringUtil.getUniqueList(lstCamelCase));
		lstUnionUniqueTotal.addAll(StringUtil.getUniqueList(lstDateTimeFormat));
		lstUnionUniqueTotal.addAll(StringUtil.getUniqueList(lstCapital));
		lstUnionUniqueTotal.addAll(StringUtil.getUniqueList(lstSpelling));
		
		config = config.withValue(Constant.CONCATENATION_PATH,
				ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(lstConcatenation)));
		config = config.withValue(Constant.CAMELCASE_PATH,
				ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(lstCamelCase)));
		config = config.withValue(Constant.DATETIMEFORMAT_PATH,
				ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(lstDateTimeFormat)));
		config = config.withValue(Constant.CAPITAL_PATH,
				ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(lstCapital)));
		config = config.withValue(Constant.SPELLING_PATH,
				ConfigValueFactory.fromAnyRef(StringUtil.getUniqueList(lstSpelling)));
		ProductConfigModule.saveConfig(config,product);
		
		int sourceTotal = lstTotal.size();
		int success = lstUnionUniqueTotal.size() - sourceTotal;
		int duplicated = lstUnionTotal.size() - lstUnionUniqueTotal.size();
		int commentCount = lstCount.size();
		Alert alert=new Alert(Alert.AlertType.INFORMATION,MessageConstant.IMPORT_MSG2_START + commentCount 
				+ MessageConstant.IMPORT_MSG2_MIND1 + lstInvalid.size() + MessageConstant.IMPORT_MSG2_MIND2 + success 
				+ MessageConstant.IMPORT_MSG2_END + duplicated);
		alert.setHeaderText(Constant.INFORMATION);
		alert.showAndWait().filter(response -> response == ButtonType.OK).ifPresent(response -> {
			close(event);
			return;
		});
		
		close(event);
	}

}
