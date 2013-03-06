package ezxpns.GUI;

/**
 * To manage the configurations of the GUI - place where all the constants are stored
 * <br />(i.e. height and width of the window)
 * <br />Should also be writing or accessing the written or previously saved configurations
 */
public interface ConfigManagerInterface {
	
	/**
	 * To load the configuration from the file xyz.ini (example)
	 */
	public void load();
	
	/**
	 * To save the configuration from the file xyz.ini (example)
	 */
	public void save();
	
}