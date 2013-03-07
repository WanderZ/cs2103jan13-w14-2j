package ezxpns.GUI;

import ezxpns.data.records.Record;
import java.util.List;


/**
 *	To handle the records between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface RecordHandlerInterface {
	
	/**
	 * Get all records stored
	 * @return List of all the records stored
	 */
	public List<Record> getAllRecords();
	
	/**
	 * Retrieve a specific record based on the identifier given
	 * @return a Record object if found, else null if not found
	 */
	public Record getRecord(int identifier);
	
	/**
	 * Create a new record
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @return true if successful, else false
	 */
	public boolean createRecord(Record newRecord);
	
	/**
	 * Remove record based on an identifier
	 * @param identifier The Unique identifier for the record to be removed
	 * @return true if successful, else false
	 */
	public boolean removeRecord(int identifier);
	
	/**
	 * Remove record
	 * @precond The given record object must contain the identifier in it
	 * @param selectedRecord a record to be removed
	 * @return true if successful, else false
	 */
	public boolean removeRecord(Record selectedRecord);
	
	/**
	 * Modify Record Method
	 * @precond The given record must have the identifier in it
	 * @param selectedRecord a record to be modified into
	 * @return true if successful, else false.
	 */
	public boolean modifyRecord(Record selectedRecord);
	
	/**
	 * Basic Search Request
	 * @param request Object containing all the user entered queries
	 * @return List of relevant records
	 */
	public List<Record> searchRecord(SearchRequest request);
	
	// Probably have more for the report side 
	// (calDailyAverage, getHighestRecords (int size or limit or amount)
	// All income/expense records
	// All of a certain category? (Ambiguous - can be either here or at CategoryHandler
}