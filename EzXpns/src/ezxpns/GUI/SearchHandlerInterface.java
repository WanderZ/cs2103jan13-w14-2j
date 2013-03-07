package ezxpns.GUI;

import ezxpns.data.records.Record;

/**
 * This interface denotes the possible searches the user may do as well as assists the UI side 
 * <br />in disseminating the search request, and returning formatted, relevant search results 
 * <br />back to the UI component. 
 * Note: Below is only an impression of what may be required, but it may or may not be required 
 * <br />(not final, subject to changes)
 */
public interface SearchHandlerInterface {
	
	/**
	 * Specifying search by category
	 */
	public static final int SEARCH_BY_CAT 	= 10000000;
	
	/**
	 * Specify search by payment mode
	 */
	public static final int SEARCH_BY_PAY 	= 01000000;
	
	/**
	 * Specify search by name
	 */
	public static final int SEARCH_BY_NAME 	= 00100000;
	
	/**
	 * Specify search by starting date (filter dates after)
	 */
	public static final int SEARCH_BY_START	= 00010000;
	
	/**
	 * Specify search by ending date (filter dates before)
	 */
	public static final int SEARCH_BY_END		= 00001000;
	
	/**
	 * Specify search by records type (income)
	 */
	public static final int SEARCH_RD_IN 		= 00000000;
	
	/**
	 * Specify search by records type (expense)
	 */
	public static final int SEARCH_RD_EX		= 00000001;
	
	/**
	 * <b>NOTE: Under major construction!</b><br />
	 * Method to be invoked to search (just an impression on how search might be done - not exactly sure how this will work out yet)
	 * @param searchCode the bit operated code (see above) for each of the corresponding search 
	 * (i.e. 1111100 - everything is specified, 00000001 - nothing specified except that it is an expense)
	 */
	public java.util.List<Record> search(int searchCode);

}
