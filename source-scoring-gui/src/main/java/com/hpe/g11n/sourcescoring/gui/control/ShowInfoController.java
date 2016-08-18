package com.hpe.g11n.sourcescoring.gui.control;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.scene.Parent;
import javafx.scene.text.*;
import javafx.stage.WindowEvent;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class ShowInfoController  extends BaseController  implements Initializable{
	@FXML
	private Parent root;
	@FXML
	private Text showInfo;
	private String info;
	public ShowInfoController(String info){
		this.info = info;
	}
	
	@Override
    @FXML
	public void initialize(URL arg0, ResourceBundle arg1) {
		if(showInfo == null){
			showInfo = new Text();
		}
		showInfo.setText(info);
		
	}
	@FXML
    public void closeInfoButton(ActionEvent event){
		Event.fireEvent(root.getScene().getWindow(), new WindowEvent(root.getScene().getWindow(), WindowEvent.WINDOW_CLOSE_REQUEST));
    }
}
