package main.java.com.goxr3plus.javafxwebbrowser.application.controllers;

import java.io.IOException;
import org.apache.log4j.Logger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import main.java.com.goxr3plus.javafxwebbrowser.tools.Constants;

/**
 * The Top bar of the application Window.
 *
 * @author GOXR3PLUS
 */
public class TopBar extends BorderPane {

    // ----------------------------------------------
    @FXML
    private Label label;
    private static final Logger LOGGER = Logger.getLogger(TopBar.class);

    /**
     * Constructor.
     */
    public TopBar() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(Constants.TOPBAR_FXML));
        loader.setController(this);
        loader.setRoot(this);
        try {
            loader.load();
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());
        }
    }

    /**
     * Called as soon as .fxml is initialized [[SuppressWarningsSpartan]]
     */
    @FXML
    private void initialize() {
        //Root
        setRight(new CloseAppBox());
    }

    public void setTitle(String title) {
        label.setText(title);
    }
}
