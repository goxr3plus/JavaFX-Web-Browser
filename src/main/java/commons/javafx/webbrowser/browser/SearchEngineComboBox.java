package commons.javafx.webbrowser.browser;

import javafx.scene.control.ComboBox;

/**
 * Search engine combo box and its utility methods.
 * 
 * @author evg.tyurin@gmail.com
 * @author GOXR3PLUS
 *
 */
public class SearchEngineComboBox extends ComboBox<String>{
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

	/** Get the default url home page for the selected search provider
	 * @return Get the default url home page for the selected search provider
	 */
	public String getSelectedEngineHomeUrl() {
		return getSearchEngineHomeUrl(getSelectionModel().getSelectedItem());
	}

	public void init() {
		getItems().addAll("Google", "DuckDuckGo", "Bing", "Yahoo");
		getSelectionModel().select(1);		
	}

}
