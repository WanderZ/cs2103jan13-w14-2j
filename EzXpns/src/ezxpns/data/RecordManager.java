package ezxpns.data;

import java.util.*;

/**
 * @author yyjhao
 * 
 * A java Generic to manage records
 * @param <T> the type of records (expense/income) to manage
 */
public class RecordManager<T extends Record> implements
	Storable,
	RecordQueryHandler<T>{
	public static class RecordUpdateException extends Exception{
		public RecordUpdateException(){
			super();
		}
		public RecordUpdateException(String message){
			super(message);
		}
	}
	
	private HashMap<Long, Category> categories;
	private HashMap<Long, TreeSet<T> > recordsByCategory;
	// note that the records is transient since it contains duplicate data as recordsByCategory
	private transient TreeMap<Date, Vector<T> > records;
	private transient HashMap<String, TreeSet<T> > recordsByName;
	
	// used to make sure no id is repeated
	private transient TreeSet<Long> ids;

	public RecordManager(){
		categories = new HashMap<Long, Category>();
		recordsByCategory = new HashMap<Long, TreeSet<T> >();
		records = new TreeMap<Date, Vector<T> >();
		recordsByName = new HashMap<String, TreeSet<T> >();
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
		for(Map.Entry<Long, TreeSet<T> > entry : recordsByCategory.entrySet()){
			TreeSet<T> rs = entry.getValue();
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
				if(recordsByName.containsKey(record.name)){
					recordsByName.get(record.name).add(record);
				}else{
					TreeSet<T> set = new TreeSet<T>();
					set.add(record);
					recordsByName.put(record.name, set);
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
		recordsByName.get(record.name).remove(record);
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
			TreeSet<T> rs = new TreeSet<T>();
			rs.add(record);
			recordsByCategory.put(new Long(record.category.getID()), rs);
		}
		if(recordsByName.containsKey(record.name)){
			recordsByName.get(record.name).add(record);
		}else{
			TreeSet<T> set = new TreeSet<T>();
			set.add(record);
			recordsByName.put(record.name, set);
		}
		updated = true;
		return (T)record.copy();
	}
	
	public void removeCategory(Category category){
		if(recordsByCategory.containsKey(category.getID())){
			TreeSet<T> rs = recordsByCategory.get(category.getID());
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
		if(!recordsByCategory.containsKey(category.getID())){
			return rs;
		}
		TreeSet<T> toget = recordsByCategory.get(category.getID());
		if(max > toget.size() || max == -1){
			max = toget.size();
		}
		for(T record : toget){
			if(max == 0)return rs;
			max--;
			rs.add((T)record.copy());
		}
		return rs;
	}
	
	public Vector<T> getRecordsBy(String name, int max){
		Vector<T> rs = new Vector<T>();
		if(!recordsByName.containsKey(name)){
			return rs;
		}
		TreeSet<T> set = recordsByName.get(name);
		if(max > set.size() || max == -1){
			max = set.size();
		}
		for(T record : set){
			if(max == 0)return rs;
			rs.add((T)record.copy());
			max--;
		}
		return rs;
	}
	
	public Vector<T> getRecordsBy(Date start, Date end, int max, boolean reverse){
		Vector<T> rs = new Vector<T>();
		Collection<Vector<T> > allrecs;
		if(reverse){
			start = new Date(Math.max(start.getTime() - 24 * 60 * 1000, 0));
			allrecs = records.descendingMap().subMap(end, start).values();
		}else{
			end = new Date(end.getTime() + 24 * 60 * 1000);
			allrecs = records.subMap(start, end).values();
		}
		for(Vector<T> rrs : allrecs){
			for(T record : rrs){
				if(max == 0)return rs;
				max--;
				rs.add((T)record.copy());
			}
		}
		return rs;
	}
}
