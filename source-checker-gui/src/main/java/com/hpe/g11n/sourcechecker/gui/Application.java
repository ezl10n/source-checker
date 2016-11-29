package com.hpe.g11n.sourcechecker.gui;


import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hpe.g11n.sourcechecker.gui.control.MainViewController;


public class Application extends javafx.application.Application {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    MainViewController mainController = new MainViewController();

    public Parent getMainView() throws IOException {
        return loadView("fxml/mainView.fxml", mainController);
    }

    protected Parent loadView(String url) throws IOException{
        return loadView(url,null);
    }

    protected Parent loadView(String url,Object controller) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            if(controller != null){
                    loader.setController(controller);
            }
            loader.load(fxmlStream);

            return loader.getRoot();// loader.getController());
        } finally {
            if (fxmlStream != null) {
                fxmlStream.close();
            }
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Source Checker");
        stage.setScene(new Scene(getMainView()));
        stage.setResizable(false);
        stage.centerOnScreen();
        setUpStyle();
        stage.show();
    }
    private void setUpStyle() {
        URL url = Application.class.getResource(String.format("/styles/themes/default/theme.css"));
        javafx.application.Application.setUserAgentStylesheet(url.toExternalForm());
    }
    public static void main(String[] args) {
        launch(args);
    }

}
