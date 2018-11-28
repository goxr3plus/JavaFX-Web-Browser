/**
 * 
 */
package main.java.com.goxr3plus.javafxwebbrowser.browser;

import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.validator.routines.UrlValidator;

import com.jfoenix.controls.JFXButton;

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
import javafx.print.PrinterJob;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
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
import javafx.stage.StageStyle;
import main.java.com.goxr3plus.javafxwebbrowser.marquee.FXMarquee;
import main.java.com.goxr3plus.javafxwebbrowser.tools.InfoTool;
import net.sf.image4j.codec.ico.ICODecoder;

/**
 * This class represents a Tab from The WebBrowser
 * 
 * @author Electron Tech!
 *
 */
public class WebBrowserTabController extends StackPane {
	
	/** The logger. */
	private final Logger logger = Logger.getLogger(getClass().getName());
	
	//------------------------------------------------------------
	
	@FXML
	private VBox errorPane;
	
	@FXML
	private JFXButton tryAgain;
	
	@FXML
	private ProgressIndicator tryAgainIndicator;
	
	@FXML
	private BorderPane borderPane;
	
	@FXML
	private JFXButton backwardButton;
	
	@FXML
	private JFXButton reloadButton;
	
	@FXML
	private JFXButton forwardButton;
	
	@FXML
	private JFXButton homeButton;
	
	@FXML
	private TextField searchBar;
	
	@FXML
	private JFXButton copyText;
	
	@FXML
	private JFXButton goButton;
	
	@FXML
	private JFXButton openInDefaultBrowser;
	
	@FXML
	private ToggleGroup searchEngineGroup;
	
	@FXML
	private CheckMenuItem movingTitleAnimation;
	
	@FXML
	private MenuItem printPage;
	
	@FXML
	private MenuItem notebookpage;
	
	@FXML
	private MenuItem findinpange;
	
	@FXML
	private MenuItem downloadPage;
	
	@FXML
	private CheckMenuItem cookieStorage;
	
	@FXML
	private WebView webView;
	
	// -------------------------------------------------------------
	
	/** The engine. */
	WebEngine browser;
	
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
		browser = webView.getEngine();
		browser.getLoadWorker().exceptionProperty().addListener(error -> {
			//System.out.println("WebEngine exception occured" + error.toString())
			checkForInternetConnection();
		});
		//		com.sun.javafx.webkit.WebConsoleListener
		//				.setDefaultListener((webView , message , lineNumber , sourceId) -> System.out.println("Console: [" + sourceId + ":" + lineNumber + "] " + message));
		//		
		//Add listener to the WebEngine
		browser.getLoadWorker().stateProperty().addListener(new FavIconProvider());
		browser.getLoadWorker().stateProperty().addListener(new DownloadDetector());
		browser.getLoadWorker().stateProperty().addListener((observable , oldState , newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				
				//Check for error pane
				errorPane.setVisible(false);
				
			} else if (newState == Worker.State.FAILED) {
				
				//Check for error pane
				errorPane.setVisible(true);
			}
		});
		
		browser.setOnError(error -> {
			//System.out.println("WebEngine error occured")
			checkForInternetConnection();
		});
		
		//handle pop up windows
		browser.setCreatePopupHandler(l -> webBrowserController.createAndAddNewTab().getWebView().getEngine());
		//System.out.println(webEngine.getUserAgent())
		//webEngine.setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:58.0) Gecko/20100101 Firefox/58.0");
		//System.out.println(webEngine.getUserAgent());
		
		//History
		setHistory(browser.getHistory());
		historyEntryList = getHistory().getEntries();
		SimpleListProperty<Entry> list = new SimpleListProperty<>(historyEntryList);
		
		//-------------------TAB------------------------
		tab.setTooltip(new Tooltip(""));
		tab.getTooltip().textProperty().bind(browser.titleProperty());
		
		// Graphic
		StackPane stack = new StackPane();
		
		// indicator
		ProgressBar indicator = new ProgressBar();
		indicator.progressProperty().bind(browser.getLoadWorker().progressProperty());
		indicator.visibleProperty().bind(browser.getLoadWorker().runningProperty());
		indicator.setMaxSize(30, 11);
		
		// label
		Label label = new Label();
		label.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		label.setAlignment(Pos.CENTER);
		label.setStyle("-fx-background-color:#202020; -fx-font-weight:bold; -fx-text-fill: white; -fx-font-size:10;");
		label.textProperty().bind(Bindings.max(0, indicator.progressProperty()).multiply(100).asString("%.00f %%"));
		//label.textProperty().bind(Bindings.max(0, indicator.progressProperty()).multiply(100.00).asString("%.02f %%"))
		// text.visibleProperty().bind(library.getSmartController().inputService.runningProperty())
		
		FXMarquee marquee = new FXMarquee();
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
		facIconImageView.setFitWidth(25);
		facIconImageView.setFitHeight(25);
		facIconImageView.setSmooth(true);
		
		//iconLabel
		Label iconLabel = new Label();
		iconLabel.setGraphic(facIconImageView);
		iconLabel.setStyle("-fx-background-color:#202020");
		iconLabel.visibleProperty().bind(indicator.visibleProperty().not());
		iconLabel.managedProperty().bind(facIconImageView.imageProperty().isNotNull().and(indicator.visibleProperty().not()));
		
		//X Button
		JFXButton closeButton = new JFXButton("X");
		int maxSize = 25;
		closeButton.setMinSize(maxSize, maxSize);
		closeButton.setPrefSize(maxSize, maxSize);
		closeButton.setMaxSize(maxSize, maxSize);
		closeButton.setStyle("-fx-background-radius:0; -fx-font-size:8px");
		closeButton.setOnAction(a -> this.webBrowserController.removeTab(tab));
		
		// HBOX
		HBox hBox = new HBox();
		hBox.setOnMouseClicked(m -> {
			if (m.getButton() == MouseButton.MIDDLE)
				this.webBrowserController.removeTab(tab);
		});
		hBox.getChildren().addAll(iconLabel, stack, marquee, closeButton);
		tab.setGraphic(hBox);
		
		//ContextMenu
		tab.setContextMenu(new WebBrowserTabContextMenu(this, webBrowserController));
		
		//-------------------Items------------------------
		
		//searchBar
		browser.getLoadWorker().runningProperty().addListener((observable , oldValue , newValue) -> {
			//if (list.size() > 0)
			//	System.out.println(getHistory().getEntries().get(getHistory().getCurrentIndex()).getUrl());
			
			if (!newValue) // if !running
				searchBar.textProperty().unbind();
			else
				searchBar.textProperty().bind(browser.locationProperty());
		});
		searchBar.setOnAction(a -> loadWebSite(searchBar.getText()));
		searchBar.focusedProperty().addListener((observable , oldValue , newValue) -> {
			if (newValue)
				Platform.runLater(() -> searchBar.selectAll());
			
		});
		
		/**
		 * new way of creating the object and pass information through it
		 */
		
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
		
		//movingTitleAnimation
		movingTitleAnimation.selectedProperty().addListener((observable , oldValue , newValue) -> {
			marquee.checkAnimationValidity(newValue);
		});
		movingTitleAnimation.setSelected(WebBrowserController.MOVING_TITLES_ENABLED);
		
		//Load the website
		loadWebSite(firstWebSite);
		
		/**
		 * NEW - IMPLEMENTSSTION GOES HERE
		 */
		
		/*
		 * printing web page implementation
		 */
		printPage.setOnAction((e) -> {
			PrinterJob job = PrinterJob.createPrinterJob();
			if (job != null) {
				browser.print(job);
				job.endJob();
			}
		});
		
		/*
		 * cookie storage managment
		 */
		cookieStorage.selectedProperty().addListener((observable , oldvalue , newvalue) -> {
			if (newvalue) {
				CookieManager manager = new CookieManager();
				manager.setCookiePolicy(CookiePolicy.ACCEPT_ORIGINAL_SERVER);
				CookieHandler.setDefault(manager);
				
				//it can save cookie on disk upon exit of application for next startup you can retrieve
				CookieStore store = manager.getCookieStore();
				try {
					URI uriadd = new URI(getHistory().getEntries().get(getHistory().getCurrentIndex()).getUrl());
					store.add(uriadd, new HttpCookie("name", "value"));
					
					/*
					 * System.out.println("host"+uriadd.getHost()); System.out.println("spliting"+uriadd.toString().split("/"));
					 * System.out.println("path"+uriadd.getPath()); System.out.println(""+uriadd.getRawPath());
					 * System.out.println(""+uriadd.getRawPath().replace('/','.')); String new_ur
					 * =uriadd.getHost()+uriadd.getRawPath().replaceAll("/",".")+"html"; System.out.println(new_ur);
					 */
				} catch (URISyntaxException e1) {
					
					e1.printStackTrace();
				}
				
				//get cookie implementation
				try {
					
					URI getcookie = new URI(getHistory().getEntries().get(getHistory().getCurrentIndex()).getUrl());
					store.get(getcookie);
				} catch (URISyntaxException e1) {
					
					e1.printStackTrace();
				}
				
			} else {
				CookieManager manager = new CookieManager();
				manager.setCookiePolicy(CookiePolicy.ACCEPT_NONE);
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.initStyle(StageStyle.UTILITY);
				alert.setTitle("COOKIES STATUS");
				alert.setHeaderText(null);
				alert.setContentText("Browser DISABLED COOKIES NO TRACKING");
				
				alert.showAndWait();
			}
		});
		
		/*
		 * Download web page implementation
		 */
		
		downloadPage.setOnAction( ( printpage -> {
			try {
				URI u = new URI(getHistory().getEntries().get(getHistory().getCurrentIndex()).getUrl());
				//Authenticator.setDefault(new DialogAuthenticator());
				
				String new_ur = u.getHost() + u.getRawPath().replaceAll("/", ".") + "html";
				FileOutputStream fos = new FileOutputStream("D:\\" + new_ur + ".html", true);
				
				InputStream in = u.toURL().openStream();
				int c;
				while ( ( c = in.read() ) != -1) {
					fos.write(c);
					
				}
				
				in.close();
				fos.close();
			} catch (IOException | URISyntaxException e) {
				System.out.println("exception occured" + e.getMessage());
			}
		} ));
		
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
	 * Return the Search Url for the Search Provider For example for `Google` returns `https://www.google.com/search?q=`
	 * 
	 * @param searchProvider
	 * @return The Search Engine Url
	 */
	public String getSearchEngineHomeUrl(String searchProvider) {
		//Find
		switch (searchProvider.toLowerCase()) {
			case "bing":
				return "http://www.bing.com";
			case "duckduckgo":
				return "https://duckduckgo.com";
			case "yahoo":
				return "https://search.yahoo.com";
			default: //then google
				return "https://www.google.com";
		}
	}
	
	/**
	 * Get the default url home page for the selected search provider
	 * 
	 * @return Get the default url home page for the selected search provider
	 */
	public String getSelectedEngineHomeUrl() {
		return getSearchEngineHomeUrl( ( (RadioMenuItem) searchEngineGroup.getSelectedToggle() ).getText());
	}
	
	/**
	 * Loads the given website , either directly if the url is a valid WebSite Url or using a SearchEngine like Google
	 * 
	 * @param webSite
	 */
	private void loadWebSite(String webSite) {
		
		//Search if it is a valid WebSite url
		String load = !new UrlValidator().isValid(webSite) ? null : webSite;
		
		//Load
		try {
			
			//First Part
			String finalWebsiteFristPart = ( load != null ) ? load : getSelectedEngineHomeUrl();
			
			//Second Part
			String finalWebsiteSecondPart = "";
			if (searchBar.getText().isEmpty())
				finalWebsiteSecondPart = "";
			else {
				switch ( ( (RadioMenuItem) searchEngineGroup.getSelectedToggle() ).getText()) {
					case "bing":
					case "duckduckgo":
						finalWebsiteSecondPart = "//?q=" + URLEncoder.encode(searchBar.getText(), "UTF-8");
						break;
					case "yahoo": //I need to find a solution for this
						finalWebsiteSecondPart = "//?q=" + URLEncoder.encode(searchBar.getText(), "UTF-8");
						break;
					default: //then google
						finalWebsiteSecondPart = "//search?q=" + URLEncoder.encode(searchBar.getText(), "UTF-8");
						break;
				}
				
			}
			
			//Load it 
			browser.load(finalWebsiteFristPart + finalWebsiteSecondPart);
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		
	}
	
	/**
	 * Loads the default website
	 */
	public void loadDefaultWebSite() {
		browser.load(getSelectedEngineHomeUrl());
	}
	
	/**
	 * Loads the current website , or if none then loads the default website
	 */
	public void reloadWebSite() {
		if (!getHistory().getEntries().isEmpty())
			browser.reload();
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
		
		//tryAgainIndicator
		tryAgainIndicator.setVisible(true);
		
		//Check for Internet connection
		Thread thread = new Thread(() -> {
			boolean hasInternet = InfoTool.isReachableByPing("www.google.com");
			Platform.runLater(() -> {
				
				//Visibility of error pane
				errorPane.setVisible(!hasInternet);
				
				//Visibility of Try Again Indicator
				tryAgainIndicator.setVisible(false);
				
				//Reload the Website if it has Internet
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
	
	/**
	 * Determines if the tab title will have a moving animation or not
	 * 
	 * @param value
	 */
	public void setMovingTitleEnabled(boolean value) {
		movingTitleAnimation.setSelected(value);
	}
	
	///////////////////////////// INNER CLASSES ////////////////////////////////
	public class FavIconProvider implements ChangeListener<State> {
		
		@Override
		public void changed(ObservableValue<? extends State> observable , State oldState , State newState) {
			if (newState == Worker.State.SUCCEEDED) {
				try {
					if ("about:blank".equals(browser.getLocation()))
						return;
					
					//Determine the full url
					String favIconFullURL = getHostName(browser.getLocation()) + "favicon.ico";
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
		public void changed(ObservableValue<? extends State> observable , State oldState , State newState) {
			if (newState == Worker.State.CANCELLED) {
				// download detected
				String url = browser.getLocation();
				logger.info("download url: " + url);
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
