package ezxpns.data.records;

import java.util.List;

/**
 *	To handle the records between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface RecordHandler {
	
	/**
	 * Get some records stored
	 * @param n the size of the records to retrieve
	 * @return List of Records up to the given size n
	 */
	public List<Record> getRecords(int n);
	
	/**
	 * Retrieve a list of IncomeRecords
	 * @return
	 */
	// public List<IncomeRecord> getIncomeRecords();
	
	// public List<ExpenseRecord> getExpenseRecords();
	
	/**
	 * Retrieve a specific record based on the identifier given
	 * @param identifier a primitive long value to identify the record
	 * @return a Record object if found, else null if not found
	 */
	public Record getRecord(long identifier);
	
	/** 
	 * Create a new income record along with a new category
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @param newCat true for new category, otherwise false
	 * @return true if creating a new record is successful, otherwise false
	 */
	public IncomeRecord createRecord(IncomeRecord newRecord, boolean newCat);
	
	/**
	 * Create a new expense record along with flags for new expense category and new payment method
	 * @param newRecord Object containing all the necessary data to create a new record
	 * @param newCat true for new category, otherwise false
	 * @param newPay true for new payment method, otherwise false
	 * @return true if successful, else false
	 */
	public ExpenseRecord createRecord(ExpenseRecord newRecord, boolean newCat, boolean newPay);
	
	/**
	 * Remove record based on an identifier
	 * @param identifier The Unique identifier for the record to be removed
	 * @return true if successful, otherwise false
	 */
	public boolean removeRecord(long identifier);
	
	/**
	 * Modify an expense record
	 * @precond The given record must have the identifier in it
	 * @param selectedRecord a record to be modified into
	 * @return true if successful, othewise false.
	 */
	public boolean modifyRecord(long id, ExpenseRecord selectedRecord, boolean newCat, boolean newPay);
	
	/**
	 * Modify an income record
	 * @precond The given record must have the identifier in it
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