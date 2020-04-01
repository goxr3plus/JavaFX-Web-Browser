package main.java.com.goxr3plus.javafxwebbrowser.application.controllers;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import main.java.com.goxr3plus.javafxwebbrowser.application.Main;
import main.java.com.goxr3plus.javafxwebbrowser.tools.Constants;
import org.apache.log4j.Logger;

public class CloseAppBox extends StackPane {

    //--------------------------------------------------------------
    @FXML
    private JFXButton minimize;

    @FXML
    private JFXButton maxOrNormalize;

    @FXML
    private JFXButton exitApplication;
    
    private static final Logger LOGGER = Logger.getLogger(CloseAppBox.class);

    // -------------------------------------------------------------
    /**
     * Constructor.
     */
    public CloseAppBox() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.CLOSEAPP_FXML));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException ex) {
            LOGGER.error(String.format("Error when instantiaing: %s", ex.getMessage()));
        }
    }

    /**
     * Called as soon as fxml is initialized
     */
    @FXML
    private void initialize() {
        // minimize
        minimize.setOnAction(ac -> Main.window.setIconified(true));

        // maximize_normalize
        maxOrNormalize.setOnAction(ac -> Main.borderlessScene.maximizeStage());

        // close
        exitApplication.setOnAction(ac -> System.exit(0));
    }
}
