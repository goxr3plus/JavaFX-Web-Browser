
package main.java.com.changes.javafxwebbrowser.application;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import main.java.com.changes.javafxwebbrowser.browser.WebBrowserController;
import main.java.com.changes.javafxwebbrowser.tools.InfoTool;

/**
 * From here you start the application
 * 
 * @author Electron Tech!
 *
 */
public class Main extends Application {
	
	/*
	 * (non-Javadoc)
	 * @see javafx.application.Application#start(javafx.stage.Stage)
	 */
	@Override
	public void start(Stage primaryStage) {
		
		//Root
		BorderPane root = new BorderPane();
		root.setCenter(new WebBrowserController());
		
		//Scene
		Scene scene = new Scene(root, getVisualScreenWidth() / 2, getVisualScreenHeight() / 2);
		
		//Prepare the Stage
		primaryStage.setTitle("JavaFX Web Browser");
		primaryStage.getIcons().add(InfoTool.getImageFromResourcesFolder("logo.png"));
		primaryStage.setScene(scene);
		primaryStage.setOnCloseRequest(cl -> System.exit(0));
		primaryStage.show();
		
	}
	
	/**
	 * Gets the visual screen width.
	 *
	 * @return The screen <b>Width</b> based on the <b>visual bounds</b> of the Screen.These bounds account for objects in the native windowing system
	 *         such as task bars and menu bars. These bounds are contained by Screen.bounds.
	 */
	public static double getVisualScreenWidth() {
		return Screen.getPrimary().getVisualBounds().getWidth();
	}
	
	/**
	 * Gets the visual screen height.
	 *
	 * @return The screen <b>Height</b> based on the <b>visual bounds</b> of the Screen.These bounds account for objects in the native windowing
	 *         system such as task bars and menu bars. These bounds are contained by Screen.bounds.
	 */
	public static double getVisualScreenHeight() {
		return Screen.getPrimary().getVisualBounds().getHeight();
	}
	
	/**
	 * Main Method
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		System.setProperty("sun.net.http.allowRestrictedHeaders", "true");
		launch(args);
	}
}
