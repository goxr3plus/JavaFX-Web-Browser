
package main.java.com.goxr3plus.javafxwebbrowser.application;

import javafx.application.Application;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import main.java.com.goxr3plus.fxborderlessscene.borderless.BorderlessScene;
import main.java.com.goxr3plus.javafxwebbrowser.application.controllers.TopBar;
import main.java.com.goxr3plus.javafxwebbrowser.browser.WebBrowserController;
import main.java.com.goxr3plus.javafxwebbrowser.tools.InfoTool;

/**
 * From here you start the application
 * 
 * @author Electron Tech! , GOXR3PLUS STUDIO
 *
 */
public class Main extends Application {
	
	public static Stage window;
	
	/** The Top Bar of the Application */
	public static TopBar topBar;
	
	public static BorderPane root;
	
	public static BorderlessScene borderlessScene;
	
	private final int screenMinWidth = 800 , screenMinHeight = 600;
	
	@Override
	public void start(Stage primaryStage) {
		
		//TopBar
		topBar = new TopBar();
		
		//Root 
		root = new BorderPane();
		root.setStyle("-fx-background-color:#202020");
		root.setTop(topBar);
		root.setCenter(new WebBrowserController());
		
		//Prepare the Stage
		window = primaryStage;
		window.setTitle("JavaFX Web Browser");
		window.setWidth(getVisualScreenWidth() * 0.9);
		window.setHeight(getVisualScreenHeight() * 0.9);
		window.centerOnScreen();
		window.getIcons().add(InfoTool.getImageFromResourcesFolder("logo.png"));
		window.centerOnScreen();
		window.setOnCloseRequest(cl -> System.exit(0));
		
		
		// Borderless Scene
		borderlessScene = new BorderlessScene(window, StageStyle.UNDECORATED, root, screenMinWidth, screenMinHeight);
		borderlessScene.getStylesheets().add(getClass().getResource("/css/application.css").toExternalForm());
		borderlessScene.setMoveControl(topBar);
		window.setScene(borderlessScene);
		
		window.show();
		
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
