package ezxpns.data.records;

import java.util.List;

/**
 * Handler for the records between the Graphical User Interface and the logic
 * @author A0097973
 * @author A0099621X
 */
public interface RecordHandler {
	
	/**
	 * Get the latest n records
	 * @param n the size of the records to retrieve
	 * @return List of Records up to the given size n
	 */
	public List<Record> getRecords(int n);
	
	/**
	 * Retrieve a specific record based on the identifier given
	 * @param identifier a primitive long value to identify the record
	 * @return a Record object if found, else null if not found
	 */
	public Record getRecord(long identifier);
	
	/** 
	 * Create a new income record along with flags for new income category
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @param newCat true for new category, otherwise false
	 * @return true if creating a new record is successful, otherwise false
	 */
	public IncomeRecord createRecord(IncomeRecord newRecord, boolean newCat);
	
	/**
	 * Create a new expense record along with flags for new expense category
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @param newCat true for new category, otherwise false
	 * @return true if successful, else false
	 */
	public ExpenseRecord createRecord(ExpenseRecord newRecord, boolean newCat);
	
	/**
	 * Remove record based on an identifier
	 * @param identifier The Unique identifier for the record to be removed
	 * @return true if successful, otherwise false
	 */
	public boolean removeRecord(long identifier);
	
	/**
	 * Modify an expense record
	 * @param selectedRecord a record to be modified into
	 * @return true if successful, otherwise false.
	 */
	public boolean modifyRecord(long id, ExpenseRecord selectedRecord, boolean newCat, boolean newPay);
	
	/**
	 * Modify an income record
	 * @param selectedRecord a record to be modified into
	 * @return true if successful, else false.
	 */
	public boolean modifyRecord(long id, IncomeRecord selectedRecord, boolean newCat);
	
	/**
	 * Return the latest expense record matching the name, or null
	 * @param name the name to match find the match with
	 * @return ExpenseRecord of the given name if found, otherwise null
	 */
	public ExpenseRecord lastExpenseRecord(String name);
	
	/**
	 * Return the latest expense record matching the name, or null
	 * @param name the name to match find the match with
	 * @return IncomeRecord of the given name if found, otherwise null
	 */
	public IncomeRecord lastIncomeRecord(String name);
}