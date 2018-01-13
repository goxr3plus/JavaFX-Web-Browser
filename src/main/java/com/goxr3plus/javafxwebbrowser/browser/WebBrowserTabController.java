/**
 * 
 */
package com.goxr3plus.javafxwebbrowser.browser;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.validator.routines.UrlValidator;

import com.goxr3plus.javafxwebbrowser.marquee.Marquee;
import com.goxr3plus.javafxwebbrowser.tools.InfoTool;
import com.jfoenix.controls.JFXButton;

import commons.javafx.webbrowser.browser.SearchEngineComboBox;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebHistory;
import javafx.scene.web.WebHistory.Entry;
import javafx.scene.web.WebView;
import net.sf.image4j.codec.ico.ICODecoder;

/**
 * This class represents a Tab from The WebBrowser
 * 
 * @author GOXR3PLUS
 *
 */
public class WebBrowserTabController extends StackPane {
	
	/** The logger. */
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	//------------------------------------------------------------
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private Button backwardButton;
	
	@FXML
	private Button forwardButton;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private Button goButton;
	
	@FXML
	private Button addOrRemoveBookMark;
	
	@FXML
	private Button reloadButton;
	
	@FXML
	private SearchEngineComboBox searchEngineComboBox;
	
	@FXML
	private WebView webView;
	
	@FXML
	private VBox errorPane;
	
	@FXML
	private JFXButton tryAgain;
	
	// -------------------------------------------------------------
	
	/** The engine. */
	WebEngine webEngine;
	
	/** The web history */
	private WebHistory history;
	private ObservableList<WebHistory.Entry> historyEntryList;
	
	private final Tab tab;
	private String firstWebSite;
	
	private final WebBrowserController webBrowserController;
	
	private final ImageView facIconImageView = new ImageView();
	
	/**
	 * Constructor
	 * 
	 * @param tab
	 * @param firstWebSite
	 */
	public WebBrowserTabController(WebBrowserController webBrowserController, Tab tab, String firstWebSite) {
		this.webBrowserController = webBrowserController;
		this.tab = tab;
		this.firstWebSite = firstWebSite;
		this.tab.setContent(this);
		
		// ------------------------------------FXMLLOADER ----------------------------------------
		FXMLLoader loader = new FXMLLoader(getClass().getResource(InfoTool.FXMLS + "WebBrowserTabController.fxml"));
		loader.setController(this);
		loader.setRoot(this);
		
		try {
			loader.load();
		} catch (IOException ex) {
			logger.log(Level.SEVERE, "", ex);
		}
	}
	
	/**
	 * Called as soon as .fxml is initialized [[SuppressWarningsSpartan]]
	 */
	@FXML
	private void initialize() {
		
		//---------------ERROR PANE---
		//tryAgain
		tryAgain.setOnAction(a -> checkForInternetConnection());
		
		//-------------------WebView------------------------	
		// hide webview scrollbars whenever they appear.
		//	webView.getChildrenUnmodifiable().addListener((Change<? extends Node> change) -> {
		//	    Set<Node> deadSeaScrolls = webView.lookupAll(".scroll-bar");
		//	    for (Node scroll : deadSeaScrolls) {
		//		scroll.setVisible(false);
		//		scroll.setManaged(false);
		//	    }
		//	});
		
		//-------------------WebEngine------------------------
		webEngine = webView.getEngine();
		webEngine.getLoadWorker().exceptionProperty().addListener(error -> checkForInternetConnection());
		//Add listener to the WebEngine
		webEngine.getLoadWorker().stateProperty().addListener(new FavIconProvider()); 
		webEngine.getLoadWorker().stateProperty().addListener(new DownloadDetector()); 
		
		webEngine.setOnError(error -> checkForInternetConnection());
		
		//handle pop up windows
		webEngine.setCreatePopupHandler(l -> webBrowserController.createAndAddNewTab().getWebView().getEngine());
		
		//History
		setHistory(webEngine.getHistory());
		historyEntryList = getHistory().getEntries();
		SimpleListProperty<Entry> list = new SimpleListProperty<>(historyEntryList);
		
		//-------------------TAB------------------------
		tab.setTooltip(new Tooltip(""));
		tab.getTooltip().textProperty().bind(webEngine.titleProperty());
		
		// Graphic
		StackPane stack = new StackPane();
		
		// indicator
		ProgressBar indicator = new ProgressBar();
		indicator.progressProperty().bind(webEngine.getLoadWorker().progressProperty());
		indicator.visibleProperty().bind(webEngine.getLoadWorker().runningProperty());
		indicator.setMaxSize(30, 11);
		
		// label
		Label label = new Label();
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setAlignment(Pos.CENTER);
		label.setStyle("-fx-font-weight:bold; -fx-text-fill: white; -fx-font-size:10; -fx-background-color: rgb(0,0,0,0.3);");
		label.textProperty().bind(Bindings.max(0, indicator.progressProperty()).multiply(100).asString("%.00f %%"));
		//label.textProperty().bind(Bindings.max(0, indicator.progressProperty()).multiply(100.00).asString("%.02f %%"))
		// text.visibleProperty().bind(library.getSmartController().inputService.runningProperty())
		
		Marquee marquee = new Marquee();
		marquee.textProperty().bind(tab.getTooltip().textProperty());
		
		stack.getChildren().addAll(indicator, label);
		stack.setManaged(false);
		stack.setVisible(false);
		
		// stack
		indicator.visibleProperty().addListener(l -> {
			if (indicator.isVisible()) {
				stack.setManaged(true);
				stack.setVisible(true);
			} else {
				stack.setManaged(false);
				stack.setVisible(false);
			}
		});
		
		//facIconImageView 
		facIconImageView.setFitWidth(20);
		facIconImageView.setFitHeight(20);
		facIconImageView.setSmooth(true);
		facIconImageView.visibleProperty().bind(indicator.visibleProperty().not());
		facIconImageView.managedProperty().bind(facIconImageView.imageProperty().isNotNull().and(indicator.visibleProperty().not()));
		
		// HBOX
		HBox hBox = new HBox();
		hBox.getChildren().addAll(facIconImageView, stack, marquee);
		tab.setGraphic(hBox);
		
		//ContextMenu
		tab.setContextMenu(new WebBrowserTabContextMenu(this, webBrowserController));
		
		//-------------------Items------------------------
		
		//searchBar
		webEngine.getLoadWorker().runningProperty().addListener((observable , oldValue , newValue) -> {
			//if (list.size() > 0)
			//	System.out.println(getHistory().getEntries().get(getHistory().getCurrentIndex()).getUrl());
			
			if (!newValue) // if !running
				searchBar.textProperty().unbind();
			else
				searchBar.textProperty().bind(webEngine.locationProperty());
		});
		searchBar.setOnAction(a -> loadWebSite(searchBar.getText()));
		searchBar.focusedProperty().addListener((observable , oldValue , newValue) -> {
			if (newValue)
				Platform.runLater(() -> searchBar.selectAll());
			
		});
		
		//Proposing sites
		new AutoCompleteTextField().bindAutoCompletion(searchBar, 15, true, WebBrowserController.WEBSITE_PROPOSALS);
		
		//goButton
		goButton.setOnAction(searchBar.getOnAction());
		
		//reloadButton
		reloadButton.setOnAction(a -> reloadWebSite());
		
		//backwardButton
		backwardButton.setOnAction(a -> goBack());
		backwardButton.disableProperty().bind(getHistory().currentIndexProperty().isEqualTo(0));
		backwardButton.setOnMouseReleased(m -> {
			if (m.getButton() == MouseButton.MIDDLE) //Create and add it next to this tab
				webBrowserController.getTabPane().getTabs().add(webBrowserController.getTabPane().getTabs().indexOf(tab) + 1,
						webBrowserController.createNewTab(getHistory().getEntries().get(getHistory().getCurrentIndex() - 1).getUrl()).getTab());
		});
		
		//forwardButton
		forwardButton.setOnAction(a -> goForward());
		forwardButton.disableProperty().bind(getHistory().currentIndexProperty().greaterThanOrEqualTo(list.sizeProperty().subtract(1)));
		forwardButton.setOnMouseReleased(m -> {
			if (m.getButton() == MouseButton.MIDDLE) //Create and add it next to this tab
				webBrowserController.getTabPane().getTabs().add(webBrowserController.getTabPane().getTabs().indexOf(tab) + 1,
						webBrowserController.createNewTab(getHistory().getEntries().get(getHistory().getCurrentIndex() + 1).getUrl()).getTab());
		});

		searchEngineComboBox.init();
		
		//Load the website
		loadWebSite(firstWebSite);
	}
	
	private Object favIconDownloader() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Returns back the main domain of the given url for example https://duckduckgo.com/?q=/favicon.ico returns <br>
	 * https://duckduckgo.com
	 * 
	 * @param urlInput
	 * @return
	 */
	private String getHostName(String urlInput) {
		try {
			URL url = new URL(urlInput);
			return url.getProtocol() + "://" + url.getHost() + "/";
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * Loads the given website , either directly if the url is a valid WebSite Url or using a SearchEngine like Google
	 * 
	 * @param webSite
	 */
	public void loadWebSite(String webSite) {
		
		//Check null or empty
		//		if (webSite == null || webSite.isEmpty())
		//			return;
		
		//Search if it is a valid WebSite url
		String load = !new UrlValidator().isValid(webSite) ? null : webSite;
		
		//Load
		try {
			webEngine.load(
					load != null ? load : searchEngineComboBox.getSelectedEngineHomeUrl() + URLEncoder.encode(searchBar.getText(), "UTF-8"));
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Loads the default website
	 */
	public void loadDefaultWebSite() {
		webEngine.load(searchEngineComboBox.getSelectedEngineHomeUrl());
	}
	
	/**
	 * Loads the current website , or if none then loads the default website
	 */
	public void reloadWebSite() {
		if (!getHistory().getEntries().isEmpty())
			webEngine.reload();
		else
			loadDefaultWebSite();
	}
	
	/**
	 * Goes Backward one Page
	 * 
	 */
	public void goBack() {
		getHistory().go(historyEntryList.size() > 1 && getHistory().getCurrentIndex() > 0 ? -1 : 0);
		//System.out.println(history.getCurrentIndex() + "," + historyEntryList.size())
	}
	
	/**
	 * Goes Forward one Page
	 * 
	 */
	public void goForward() {
		getHistory().go(historyEntryList.size() > 1 && getHistory().getCurrentIndex() < historyEntryList.size() - 1 ? 1 : 0);
		//System.out.println(history.getCurrentIndex() + "," + historyEntryList.size())
	}
	
	/**
	 * @return the webView
	 */
	public WebView getWebView() {
		return webView;
	}
	
	/**
	 * @return the tab
	 */
	public Tab getTab() {
		return tab;
	}
	
	/**
	 * @return the errorPane
	 */
	public VBox getErrorPane() {
		return errorPane;
	}
	
	/**
	 * Checks for internet connection
	 */
	void checkForInternetConnection() {
		
		//Check for internet connection
		Thread thread = new Thread(() -> {
			boolean hasInternet = InfoTool.isReachableByPing("www.google.com");
			Platform.runLater(() -> {
				errorPane.setVisible(!hasInternet);
				if (hasInternet)
					reloadWebSite();
			});
		}, "Internet Connection Tester Thread");
		thread.setDaemon(true);
		thread.start();
	}
	
	/**
	 * @return the history
	 */
	public WebHistory getHistory() {
		return history;
	}
	
	/**
	 * @param history
	 *            the history to set
	 */
	public void setHistory(WebHistory history) {
		this.history = history;
	}
	///////////////////////////// INNER CLASSES ////////////////////////////////
	public class FavIconProvider implements ChangeListener<State> {

		@Override
		public void changed(ObservableValue<? extends State> observable, State oldState, State newState) {
			if (newState == Worker.State.SUCCEEDED) {
				try {
					//Determine the full url
					String favIconFullURL = getHostName(webEngine.getLocation()) + "favicon.ico";
					//System.out.println(favIconFullURL)
					
					//Create HttpURLConnection 
					HttpURLConnection httpcon = (HttpURLConnection) new URL(favIconFullURL).openConnection();
					httpcon.addRequestProperty("User-Agent", "Mozilla/5.0");
					List<BufferedImage> image = ICODecoder.read(httpcon.getInputStream());
					
					//Set the favicon
					facIconImageView.setImage(SwingFXUtils.toFXImage(image.get(0), null));
					
				} catch (Exception ex) {
					//ex.printStackTrace()
					facIconImageView.setImage(null);
				}
			}
		}		
	}// FavIconProvider
	
	public class DownloadDetector implements ChangeListener<State> {

		@Override
		public void changed(ObservableValue<? extends State> observable, State oldState, State newState) {
			if (newState == Worker.State.CANCELLED) {
			// download detected
			String url = webEngine.getLocation();
			logger.info("download url: "+url);
//             try{
//                 Download download = new Download(webEngine.getLocation());
//                 Thread t = new Thread(download);
//                 t.start();
//             }catch(Exception ex){
//                 logger.log(Level.SEVERE, "download", ex);
//             }
			}
		}		
	}// DownloadDetector

}
