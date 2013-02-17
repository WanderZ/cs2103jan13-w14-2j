package ezxpns.data;

import java.util.*;

/**
 * @author yyjhao
 * 
 * A java Generic to manage records
 * @param <T> the type of records (expense/income) to manage
 */
public class RecordManager<T extends Record> {
	public static class RecordUpdateException extends Exception{
		public RecordUpdateException(){
			super();
		}
		public RecordUpdateException(String message){
			super(message);
		}
	}
	
	private HashMap<Long, Category> categories;
	private HashMap<Long, Vector<T> > recordsByCategory;
	// note that the records is transient since it contains duplicate data as recordsByCategory
	private transient TreeMap<Date, Vector<T> > records;

	public RecordManager(){
		categories = new HashMap<Long, Category>();
		recordsByCategory = new HashMap<Long, Vector<T> >();
		records = new TreeMap<Date, Vector<T> >();
	}
	
	/**
	 * A state about whether the object has been updated since the last save
	 */
	private transient boolean updated = false;
	
	/**
	 * @return if the object has been modified since the last time it was saved
	 */
	public boolean isUpdated(){
		return updated;
	}
	
	/**
	 * Tells the object to reset the updated attribute
	 */
	public void saved(){
		updated = false;
	}
	
	/**
	 * Populate data structures containing duplicate data
	 * Also add the category reference to the records
	 */
	public void afterDeserialize(){
		for(Map.Entry<Long, Vector<T> > entry : recordsByCategory.entrySet()){
			Vector<T> rs = entry.getValue();
			Category cat = getCategory(entry.getKey());
			for(T record : rs){
				record.category = cat;
				if(records.containsKey(record.date)){
					records.get(record.date).add(record);
				}else{
					Vector<T> list = new Vector<T>();
					list.add(record);
					records.put(record.date, list);
				}
			}
		}
	}
	
	/**
	 * Find the reference to the record with the same id
	 * @param record
	 * @return A record with the same id as record
	 * @throws RecordUpdateException
	 */
	private T findRecord(T record) throws RecordUpdateException{
		if(!records.containsKey(record.date) ||
				!recordsByCategory.containsKey(record.category.getID())){
			throw new RecordUpdateException("Record does not exist.\n");
		}
		Vector<T> list = records.get(record.date);
		for(T r : list){
			if(r.equals(record)){
				return r;
			}
		}
		throw new RecordUpdateException("Record does not exist.\n");
	}
	
	public void updateRecord(T original, T updated) throws RecordUpdateException{
		removeRecord(original);
		addNewRecord(updated);
	}
	
	public void removeRecord(T toRemove) throws RecordUpdateException{
		T record = findRecord(toRemove);
		records.get(record.date).remove(record);
		recordsByCategory.get(record.category.getID()).remove(record);
		updated = true;
	}
	
	public void addNewRecord(T toAdd){
		T record = (T)toAdd.clone();
		if(records.containsKey(record.date)){
			records.get(record.date).add(record);
		}else{
			Vector<T> rs = new Vector<T>();
			rs.add(record);
			records.put(record.date, rs);
		}
		if(recordsByCategory.containsKey(record.category.getID())){
			recordsByCategory.get(record.category.getID()).add(record);
		}else{
			Vector<T> rs = new Vector<T>();
			rs.add(record);
			recordsByCategory.put(new Long(record.category.getID()), rs);
		}
		updated = true;
	}
	
	public void removeCategory(Category category){
		if(recordsByCategory.containsKey(category.getID())){
			Vector<T> rs = recordsByCategory.get(category.getID());
			for(Record r : rs){
				r.category = Category.undefined;
			}
			recordsByCategory.remove(category.getID());
		}
		categories.remove(category.getID());
		updated = true;
	}
	
	public void updateCategory(long id, String newName){
		categories.get(id).setName(newName);
		updated = true;
	}
	
	public void addNewCategory(Category toAdd){
		Category category = toAdd.clone();
		if(!categories.containsKey(category.getID())){
			categories.put(category.getID(), category);
		}
		updated = true;
	}
	
	public Category getCategory(Long id){
		return categories.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<T> getRecordsBy(Category category){
		return (Vector<T>) recordsByCategory.get(category.getID()).clone();
	}
	
}
