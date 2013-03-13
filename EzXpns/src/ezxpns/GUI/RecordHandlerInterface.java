package ezxpns.GUI;

import ezxpns.data.records.*;
import java.util.List;


/**
 *	To handle the records between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface RecordHandlerInterface {
	
	/**
	 * Get some records stored
	 * @return List of maximum n records
	 */
	public List<Record> getRecords(int n);
	
	/**
	 * Retrieve a specific record based on the identifier given
	 * @return a Record object if found, else null if not found
	 */
	public Record getRecord(long identifier);
	
	/**
	 * Create a new record
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @return true if successful, else false
	 */
	public boolean createRecord(IncomeRecord newRecord);
	public boolean createRecord(ExpenseRecord newRecord);
	
	/**
	 * Remove record based on an identifier
	 * @param identifier The Unique identifier for the record to be removed
	 * @return true if successful, else false
	 */
	public boolean removeRecord(long identifier);
	
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
	public boolean modifyRecord(ExpenseRecord selectedRecord);
	
	/**
	 * Modify Record Method
	 * @precond The given record must have the identifier in it
	 * @param selectedRecord a record to be modified into
	 * @return true if successful, else false.
	 */
	public boolean modifyRecord(IncomeRecord selectedRecord);
	
	/**
	 * Return the latest expense record matching the name, or null
	 * @param name the name to match find the match with
	 */
	public ExpenseRecord lastExpenseRecord(String name);
	
	/**
	 * Return the latest expense record matching the name, or null
	 * @param name the name to match find the match with
	 */
	public IncomeRecord lastIncomeRecord(String name);
}