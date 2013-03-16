package ezxpns.GUI;

import java.awt.Font;

/**
 * To manage the configurations of the GUI - place where all the constants are stored
 * <br />(i.e. height and width of the window)
 * <br />Should also be writing or accessing the written or previously saved configurations
 */
public interface ConfigManagerInterface {
	
	public static final int DEFAULT_BTN_FONT_SIZE = 24;
	public static final Font DEFAULT_BTN_FONT = new Font("Segoe UI", 0, DEFAULT_BTN_FONT_SIZE);
	
	/**
	 * To load the configuration from the file xyz.ini (example)
	 */
	public void load();
	
	/**
	 * To save the configuration from the file xyz.ini (example)
	 */
	public void save();
	
	
	// Possibly Date formats
	// Limits for year in the entry of date fields
}