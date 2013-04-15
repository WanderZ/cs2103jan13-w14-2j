package ezxpns.GUI;

import java.awt.Font;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

import ezxpns.data.records.RecordManager;

/**
 * Default Configurations for GUI Component
 * @author A0097973 Koh Zheng Kang
 */
public class Config {
	
	/**
	 * The default no target the user can set - 0
	 */
	public static final int DEFAULT_NO_TARGET = 0;
	
	/**
	 * The default maximum target the user can set - 7 mil
	 */
	public static final int DEFAULT_MAX_TARGET = 7000000;
	
	/**
	 * The default minimum target the user can set - 10
	 */
	public static final int DEFAULT_MIN_TARGET = 10;
	
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
	public static final int MIN_UI_WIDTH = 1130;
	
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
	public static final int DEFAULT_MAX_AMT_PER_RECORD = 1000000; // One million
	
	/**
	 * The preset minimum amount for each record - 1 cent
	 */
	public static final double DEFAULT_MIN_AMT_PER_RECORD = 0.01;
		
	/**
	 * The preset maximum name length 
	 */
	public static final int DEFAULT_MAX_LENGTH_NAME = 50;
	
	/**
	 * The preset maximum name length 
	 */
	public static final int DEFAULT_MAX_LENGTH_DESC = 250;
	
	/**
	 * Alphanumeric pattern matcher
	 */
	public static final Pattern ALPHANUMERIC = RecordManager.INVALIDNAME;
	
	private Config(){}
	
	public static boolean isAlphaNumeric(String text) {
		return ALPHANUMERIC.matcher(text).find();
	}
}
