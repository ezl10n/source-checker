package com.hpe.g11n.sourcescoring.gui;


import com.hpe.g11n.sourcescoring.gui.control.MainViewController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;


public class Application extends javafx.application.Application {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    public Parent getMainView() throws IOException {
        return loadView("fxml/mainView.fxml", MainViewController.class);
    }

    protected Parent loadView(String url) throws IOException{
        return loadView(url,null);
    }

    protected Parent loadView(String url,Class clz) throws IOException {
        InputStream fxmlStream = null;
        try {
            fxmlStream = getClass().getClassLoader().getResourceAsStream(url);
            FXMLLoader loader = new FXMLLoader();
            if(clz != null){
                try {
                    Object controller = clz.newInstance();
                    loader.setController(controller);
                } catch (InstantiationException e) {
                    logger.error("can't init javaFX Controller.",e);
                } catch (IllegalAccessException e) {
                    logger.error("can't init javaFX Controller.",e);
                }
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
        stage.setTitle("Source Scoring");
        stage.setScene(new Scene(getMainView()));
        stage.setResizable(true);
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
