/*
 *  This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.

   Also(warning!):
 
  1)You are not allowed to sell this product to third party.
  2)You can't change license and made it like you are the owner,author etc.
  3)All redistributions of source code files must contain all copyright
     notices that are currently in this file, and this list of conditions without
     modification.
 */



package application;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;

/**
 * @author GOXR3PLUS
 *
 */
public class WebBrowserController extends BorderPane {

    /** The logger. */
    private final Logger logger = Logger.getLogger(getClass().getName());

    //------------------------------------------------------------

    @FXML
    private TabPane tabPane;

    @FXML
    private JFXButton addTab;

    // -------------------------------------------------------------

    /**
     * Constructor
     */
    public WebBrowserController() {
	// ------------------------------------FXMLLOADER ----------------------------------------
	FXMLLoader loader = new FXMLLoader(getClass().getResource("WebBrowserController.fxml"));
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

	//tabPane
	tabPane.getTabs().clear();
	createNewTab("coz");

	//addTab
	addTab.setOnAction(a -> {

	    //Check tabs number
	    if (tabPane.getTabs().size() >= 8) {
		JFXDialog dialog = new JFXDialog();
		//Show Message
		Alert alert = new Alert(AlertType.WARNING,
			"Currently only 8 tabs are allowed , for performance reasons... \n\n If you can hack it without decompiling the code i will give you 5$ dollars via paypal ;)");
		alert.showAndWait();

		return;
	    }

	    //Create
	    createNewTab();
	});

    }

    /**
     * Creates a new tab for the webbrowser ->Directing to a specific website [[SuppressWarningsSpartan]]
     * 
     * @param webSite
     */
    public void createNewTab(String... webSite) {
	//Add new Tab
	Tab tab = new Tab("");
	WebBrowserTabController webBrowserTab = new WebBrowserTabController(tab,webSite.length==0?null:webSite[0]);
	tab.setOnCloseRequest(c -> {

	    //Check the tabs number
	    if (tabPane.getTabs().size() == 1)
		createNewTab();

	    // Delete cache for navigate back
	    webBrowserTab.webEngine.load("about:blank");

	    //Delete cookies  Experimental!!! 
	    //java.net.CookieHandler.setDefault(new java.net.CookieManager())

	});

	//Add the tab
	tabPane.getTabs().add(tab);
	//System.out.println(Arrays.asList(webSite))

    }

}
