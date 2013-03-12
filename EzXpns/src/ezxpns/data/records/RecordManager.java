package ezxpns.data.records;

import java.util.*;

import ezxpns.data.RecordQueryHandler;
import ezxpns.data.Storable;

/**
 * @author yyjhao
 * 
 * A java Generic to manage records
 * @param <T> the type of records (expense/income) to manage
 */
public class RecordManager<T extends Record>
	extends Storable
	implements RecordQueryHandler<T>{
	public static class RecordUpdateException extends Exception{
		public RecordUpdateException(){
			super();
		}
		public RecordUpdateException(String message){
			super(message);
		}
	}
	
	public static class CategoryUpdateException extends Exception{
		public CategoryUpdateException(String message){
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
		categories.put(Category.undefined.getID(), Category.undefined);
		recordsByCategory = new HashMap<Long, TreeSet<T> >();
		records = new TreeMap<Date, Vector<T> >();
		recordsByName = new HashMap<String, TreeSet<T> >();
		ids = new TreeSet<Long>();
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
	 */
	private T findRecord(T record){
		if(!records.containsKey(record.date) ||
				!recordsByCategory.containsKey(record.category.getID())){
			return null;
		}
		Vector<T> list = records.get(record.date);
		for(T r : list){
			if(r.equals(record)){
				return r;
			}
		}
		return null;
	}
	
	public T updateRecord(T original, T updated) throws RecordUpdateException{
		removeRecord(original);
		return addNewRecord(updated);
	}
	
	public void removeRecord(T toRemove) throws RecordUpdateException{
		T record = findRecord(toRemove);
		if(record == null)throw new RecordUpdateException("Record does not exist.");
		records.get(record.date).remove(record);
		recordsByCategory.get(record.category.getID()).remove(record);
		recordsByName.get(record.name).remove(record);
		markUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public T addNewRecord(T toAdd) throws RecordUpdateException{
		T record = (T)toAdd.copy();
		record.category = categories.get(toAdd.category.getID());
		if(record.category == null){
			throw new RecordUpdateException("Invalid category!");
		}
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
		markUpdate();
		return record;
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
		markUpdate();
	}
	
	public void updateCategory(long id, String newName) throws CategoryUpdateException{
		if(categories.get(id) == null){
			throw new CategoryUpdateException("The category with the id does not exist!");
		}
		categories.get(id).setName(newName);
		markUpdate();
	}
	
	public void addNewCategory(Category toAdd){
		Category category = toAdd.copy();
		if(!categories.containsKey(category.getID())){
			categories.put(category.getID(), category);
		}
		markUpdate();
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
			rs.add(record);
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
			rs.add(record);
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
				rs.add(record);
			}
		}
		return rs;
	}
}
