/*
 * 
 */
package main.java.com.goxr3plus.javafxwebbrowser.tools;

import java.io.File;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.scene.image.Image;
import main.java.com.goxr3plus.javafxwebbrowser.application.Main;

/**
 * Provides useful methods for retrieving informations.
 *
 * @author GOXR3PLUS
 */
public final class InfoTool {
	
	/** Logger */
	public static final Logger logger = Logger.getLogger(InfoTool.class.getName());
	
	/** WebSite url */
	public static final String WEBSITE = "http://goxr3plus.co.nf";
	
	/** XR3Player Tutorials */
	public static final String TUTORIALS = "https://www.youtube.com/playlist?list=PL-xqaiRUr_iRKDkpFWPfSRFmJvHSr1VJI";
	
	/** The Constant images. */
	public static final String IMAGES = "/image/";
	
	/** The Constant styLes. */
	public static final String STYLES = "/style/";
	
	/** The Constant applicationCss. */
	public static final String APPLICATIONCSS = "application.css";
	
	/** The Constant fxmls. */
	public static final String FXMLS = "/fxml/";
	
	// --------------------------------------------------------------------------------------------------------------
	
	/**
	 * Private Constructor , we don't want instances of this class
	 */
	private InfoTool() {
	}
	
	/**
	 * Returns the absolute path of the current directory in which the given class file is.
	 * 
	 * @param classs
	 *            * @return The absolute path of the current directory in which the class file is. <b>[it ends with File.Separator!!]</b>
	 * @author GOXR3PLUS[StackOverFlow user] + bachden [StackOverFlow user]
	 */
	public static final String getBasePathForClass(Class<?> classs) {
		
		// Local variables
		File file;
		String basePath = "";
		boolean failed = false;
		
		// Let's give a first try
		try {
			file = new File(classs.getProtectionDomain().getCodeSource().getLocation().toURI().getPath());
			
			basePath = ( file.isFile() || file.getPath().endsWith(".jar") || file.getPath().endsWith(".zip") ) ? file.getParent() : file.getPath();
		} catch (URISyntaxException ex) {
			failed = true;
			Logger.getLogger(classs.getName()).log(Level.WARNING, "Cannot firgue out base path for class with way (1): ", ex);
		}
		
		// The above failed?
		if (failed)
			try {
				file = new File(classs.getClassLoader().getResource("").toURI().getPath());
				basePath = file.getAbsolutePath();
				
				// the below is for testing purposes...
				// starts with File.separator?
				// String l = local.replaceFirst("[" + File.separator +
				// "/\\\\]", "")
			} catch (URISyntaxException ex) {
				Logger.getLogger(classs.getName()).log(Level.WARNING, "Cannot firgue out base path for class with way (2): ", ex);
			}
		
		// fix to run inside Eclipse
		if (basePath.endsWith(File.separator + "lib") || basePath.endsWith(File.separator + "bin") || basePath.endsWith("bin" + File.separator)
				|| basePath.endsWith("lib" + File.separator)) {
			basePath = basePath.substring(0, basePath.length() - 4);
		}
		// fix to run inside NetBeans
		if (basePath.endsWith(File.separator + "build" + File.separator + "classes")) {
			basePath = basePath.substring(0, basePath.length() - 14);
		}
		// end fix
		if (!basePath.endsWith(File.separator))
			basePath += File.separator;
		
		return basePath;
	}
	
	/**
	 * Checks if a web site is reachable using ping command.
	 *
	 * @param host
	 *            the host
	 * @return <b> true </b> if Connected on Internet,<b> false </b> if not.
	 */
	public static boolean isReachableByPing(String host) {
		try {
			
			// Start a new Process
			Process process = Runtime.getRuntime().exec("ping -" + ( System.getProperty("os.name").toLowerCase().startsWith("windows") ? "n" : "c" ) + " 1 " + host);
			
			//Wait for it to finish
			process.waitFor();
			
			//Check the return value
			return process.exitValue() == 0;
			
		} catch (Exception ex) {
			Logger.getLogger(Main.class.getName()).log(Level.INFO, null, ex);
			return false;
		}
	}
	
	/**
	 * Use this method to retrieve an image from the resources of the application.
	 *
	 * @param imageName
	 *            the image name
	 * @return Returns an image which is already into the resources folder of the application
	 */
	public static Image getImageFromResourcesFolder(String imageName) {
		return new Image(InfoTool.class.getResourceAsStream(IMAGES + imageName));
	}
	
	/**
	 * Gets the file size edited in format "x MiB , y KiB"
	 *
	 * @param bytes
	 *            File size in bytes
	 * @return <b> a String representing the file size in MB and kB </b>
	 */
	public static String getFileSizeEdited(long bytes) {
		
		//Find it	
		int kilobytes = (int) ( bytes / 1024 ) , megabytes = kilobytes / 1024;
		if (kilobytes < 1024)
			return kilobytes + " KiB";
		else if (kilobytes > 1024)
			return megabytes + " MiB + " + ( kilobytes - ( megabytes * 1024 ) ) + " KiB";
		
		return "error";
		
	}
	
}
