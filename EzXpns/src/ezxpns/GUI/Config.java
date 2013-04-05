package ezxpns.GUI;

import java.awt.Font;
import java.text.DecimalFormat;

/**
 * Default Configurations for GUI Component
 */
public class Config {
	
	/**
	 * The default width for menu button
	 */
	public static final int DEFAULT_MENU_WIDTH = 134;
	
	/**
	 * The default height for menu button
	 */
	public static final int DEFAULT_MENU_HEIGHT = 30;
	
	/**
	 * The default width for the GUI window 
	 */
	public static final int DEFAULT_UI_WIDTH = 1280;
	
	/**
	 * The default height for the GUI window
	 */
	public static final int DEFAULT_UI_HEIGHT = 720;
	
	/**
	 * The minimum width for the GUI window
	 */
	public static final int MIN_UI_WIDTH = 1100;
	
	/**
	 * The minimum height for the GUI window
	 */
	public static final int MIN_UI_HEIGHT = 650;
	
	/**
	 * The default width for the dialog window 
	 */
	public static final int DEFAULT_DIALOG_WIDTH = 600;
	
	/**
	 * The default height for the dialog height
	 */
	public static final int DEFAULT_DIALOG_HEIGHT = 400; 
	
	/**
	 * The preset font for title - size set at 24
	 */
	public static final Font TITLE_FONT = new Font("Segoe UI", 0, 24);
	
	/**
	 * The preset font for menu buttons - size set at 20
	 */
	public static final Font MENU_FONT = new Font("Segoe UI", 0, 16);
	
	/**
	 * The preset font for normal text - size set at 14
	 */
	public static final Font TEXT_FONT = new Font("Segoe UI", 0, 14);
	
	/**
	 * The preset money format for currency $###,###,##0.00
	 */
	public static final DecimalFormat MONEY_FORMAT = new DecimalFormat("$###,###,##0.00");
	
	/**
	 * The preset maximum amount for each record - One million
	 */
	public static final int DEFAULT_MAX_PER_RECORD = 1000000000; // One million
	
	private Config(){}
}