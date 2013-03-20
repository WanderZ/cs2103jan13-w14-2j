package ezxpns.data.records;

import ezxpns.data.records.*;
import java.util.List;


/**
 *	To handle the records between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface RecordHandler {
	
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
	 * Create a new income record
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @return true if successful, else false
	 */
	public boolean createRecord(IncomeRecord newRecord);
	
	/**
	 * Create a new expense record
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @return true if successful, else false
	 */
	public boolean createRecord(ExpenseRecord newRecord);
	
	/**
	 * Remove record based on an identifier
	 * @param identifier The Unique identifier for the record to be removed
	 * @return true if successful, else false
	 */
	public boolean removeRecord(long identifier);

	
	/**
	 * Modify an expense record
	 * @precond The given record must have the identifier in it
	 * @param selectedRecord a record to be modified into
	 * @return true if successful, else false.
	 */
	public boolean modifyRecord(long id, ExpenseRecord selectedRecord);
	
	/**
	 * Modify an income record
	 * @precond The given record must have the identifier in it
	 * @param selectedRecord a record to be modified into
	 * @return true if successful, else false.
	 */
	public boolean modifyRecord(long id, IncomeRecord selectedRecord);
	
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