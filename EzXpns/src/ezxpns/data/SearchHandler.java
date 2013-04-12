package ezxpns.data;

import ezxpns.data.records.Record;


/**
 * This interface denotes the possible searches the user may do as well as assists the UI side 
 * <br />in disseminating the search request, and returning formatted, relevant search results 
 * <br />back to the UI component. 
 * Note: Below is only an impression of what may be required, but it may or may not be required 
 * <br />(not final, subject to changes)
 */
public interface SearchHandler {
	
	public java.util.Vector<Record> search(SearchRequest req);
	
	public java.util.Vector<Record> search(String partialMatch);
}
