package ezxpns.GUI;

public interface MasterHandlerInterface extends 
	RecordHandlerInterface, 
	CategoryHandlerInterface, 
	PayModeHandlerInterface, 
	ConfigManagerInterface, 
	SearchHandlerInterface
{
	
	// Other stuff like write to database
	
	/**
	 * Force EzXpns to write all the current data to write into physical files.
	 */
	public void writeToFile();
	
	/**
	 * Force EzXpns to reload all the data from the saved files
	 */
	public void readFromFile();

}
