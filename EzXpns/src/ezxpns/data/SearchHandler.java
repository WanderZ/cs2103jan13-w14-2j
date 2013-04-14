package ezxpns.data;

import ezxpns.data.records.Record;
import java.util.*;


/**
 * An interface that specifies what forms of search can be performed
 */
public interface SearchHandler {
	public Vector<Record> search(SearchRequest req);
	public Vector<Record> search(String partialMatch);
}
