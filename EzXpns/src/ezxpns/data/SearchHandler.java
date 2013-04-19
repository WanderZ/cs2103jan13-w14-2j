package ezxpns.data;

import ezxpns.data.records.Record;
import java.util.*;


/**
 * An interface that specifies what forms of search can be performed
 * @author A0099621X
 * @author A0097973H
 */
public interface SearchHandler {
	/**
	 * Search by SearchRequest
	 * @param req SearchRequest Object reference to search by
	 * @return Vector of records, if any, otherwise null
	 */
	public Vector<Record> search(SearchRequest req);
	
	/**
	 * Search by partial match 
	 * @param partialMatch String to match partially.
	 * @return Vector of records, if any, otherwise null
	 */
	public Vector<Record> search(String partialMatch);
}
