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
	 * Get at most max recent records matching the name
	 * @param name name of record
	 * @param max maximum number of record return, records are ordered by date in decending order
	 * @return records matching the name
	 */
	Vector<T> getRecordsBy(String name, int max);
	/**
	 * Get at most max recent records matching the category
	 * @param category category of the record
	 * @param max maximum number of records return, records are ordered by date in decending order
	 * @return records matching the name
	 */
	Vector<T> getRecordsBy(Category category, int max);
	/**
	 * Get records matching the date range, inclusive of both ends, with a specified maximum number and 
	 * whether it should be in reverse order of dates
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
