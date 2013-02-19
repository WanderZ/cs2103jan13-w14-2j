package ezxpns.data;

import java.util.*;

/**
 * @author yyjhao
 * 
 * A java Generic to manage records
 * @param <T> the type of records (expense/income) to manage
 */
public class RecordManager<T extends Record> implements Storable {
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
	
	// used to make sure no id is repeated
	private transient TreeSet<Long> ids;

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
	
	public T updateRecord(T original, T updated) throws RecordUpdateException{
		removeRecord(original);
		return addNewRecord(updated);
	}
	
	public void removeRecord(T toRemove) throws RecordUpdateException{
		T record = findRecord(toRemove);
		records.get(record.date).remove(record);
		recordsByCategory.get(record.category.getID()).remove(record);
		updated = true;
	}
	
	@SuppressWarnings("unchecked")
	public T addNewRecord(T toAdd){
		T record = (T)toAdd.copy();
		if(ids.contains(record.id)){
			record.id = (new Date()).getTime();
		}
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
		return (T)record.copy();
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
		Category category = toAdd.copy();
		if(!categories.containsKey(category.getID())){
			categories.put(category.getID(), category);
		}
		updated = true;
	}
	
	public Category getCategory(Long id){
		return categories.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public Vector<T> getRecordsBy(Category category, int max){
		Vector<T> rs = new Vector<T>();
		Vector<T> toget = recordsByCategory.get(category.getID());
		if(max > toget.size() || max == -1){
			max = toget.size();
		}
		for(int i = 0; i < max; i++){
			rs.add((T)toget.get(i).copy());
		}
		return rs;
	}
	
	public Vector<T> getRecordsBy(String name, int max){
		return null;
		
	}
	
	public Vector<T> getRecordsBy(Date start, Date end, int max){
		return null;
	}
	
	public T getRecordBy(long id){
		return null;
	}
}
