package com.hpe.g11n.sourcescoring.gui.control;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.hpe.g11n.sourcescoring.config.guice.ConfigModule;
import com.hpe.g11n.sourcescoring.core.guice.CoreModule;
import com.hpe.g11n.sourcescoring.gui.guice.GUIModule;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;

import java.io.IOException;
import java.io.InputStream;

/**
 *
 * Created with IntelliJ IDEA.
 * User: Foy Lian
 * Date: 2016-08-16
 * Time: 14:33
 */
public abstract class BaseController implements Initializable {
    protected Injector injector= Guice.createInjector(new CoreModule(),
            new ConfigModule(), new GUIModule());
    public BaseController(){
        injector.injectMembers(this);
    }

    protected Parent loadView(String url) throws IOException {
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
}
