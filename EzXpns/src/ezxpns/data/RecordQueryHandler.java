package ezxpns.data;

import java.util.*;

import ezxpns.data.records.*;

/**
 * An interface that handles primitive search query for records
 * @author A0099621X
 * @param <T> A Record type
 */
public interface RecordQueryHandler<T extends Record> {
	
	/**
	 * Search for records matching the name
	 * @param name name of record
	 * @param max maximum number of record return, records are ordered by date in decending order
	 * @return records matching the name
	 */
	Vector<T> getRecordsBy(String name, int max);
	/**
	 * Search for records matching the category
	 * @param category category of the record
	 * @param max maximum number of records return, records are ordered by date in decending order
	 * @return records matching the name
	 */
	Vector<T> getRecordsBy(Category category, int max);
	/**
	 * Search for records in the date range, inclusive of both ends
	 * @param start start of date
	 * @param end end of date
	 * @param max maximum number of records return
	 * @param reverse reverse the ordering of the records
	 * @return records in the date range
	 */
	Vector<T> getRecordsBy(Date start, Date end, int max, boolean reverse);
	
	/**
	 * Get records with name starting with the string
	 */
	Vector<T> getRecordsWithNamePrefix(String prefix);
}
