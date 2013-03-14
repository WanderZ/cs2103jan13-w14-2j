package ezxpns.data.records;

import java.util.*;

import ezxpns.data.RecordQueryHandler;
import ezxpns.data.Storable;

import ezxpns.GUI.*;

/**
 * @author yyjhao
 * 
 * A java Generic to manage records
 * @param <T> the type of records (expense/income) to manage
 */
public class RecordManager<T extends Record>
	extends Storable
	implements
		RecordQueryHandler<T>,
		CategoryHandlerInterface{
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
	protected transient TreeMap<Long, T> recordsById;
	protected transient Random ran = new Random();
	
	private transient double allTimeSum = 0,
							 monthlySum = 0,
							 dailySum = 0,
							 yearlySum = 0;
	private transient HashMap<Long, Double> monthlySumByCategory;
	
	private transient Calendar cal = Calendar.getInstance();
	private transient Date today, startOfYear, startOfMonth;

	public double getAllTimeSum() {
		return allTimeSum;
	}
	public double getMonthlySum() {
		return monthlySum;
	}
	public double getDailySum() {
		return dailySum;
	}
	public double getYearlySum() {
		return yearlySum;
	}
	
	public double getMonthlySum(Category cat){
		return monthlySumByCategory.get(cat.getID());
	}
	public RecordManager(){
		categories = new HashMap<Long, Category>();
		categories.put(Category.undefined.getID(), Category.undefined);
		recordsByCategory = new HashMap<Long, TreeSet<T> >();
		records = new TreeMap<Date, Vector<T> >();
		recordsByName = new HashMap<String, TreeSet<T> >();
		recordsById = new TreeMap<Long, T>();
		monthlySumByCategory = new HashMap<Long, Double>();
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		today = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 0);
		startOfMonth = cal.getTime();
		cal.set(Calendar.MONTH, 0);
		startOfYear = cal.getTime();
	}
	/**
	 * Populate data structures containing duplicate data
	 * Also add the category reference to the records
	 */
	public void afterDeserialize(){
		for(long c : categories.keySet()){
			monthlySumByCategory.put(c, 0.0);
		}
		ran = new Random();
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
				recordsById.put(record.id, record);
				addSums(record);
			}
		}
	}
	
	private void addSums(Record record){
		if(record.date.after(today)){
			dailySum += record.amount;
		}
		if(record.date.after(startOfMonth)){
			monthlySum += record.amount;
			monthlySumByCategory.put(record.category.getID(),
					monthlySumByCategory.get(record.category.getID()) + record.amount);
		}
		if(record.date.after(startOfYear)){
			yearlySum += record.amount;
		}
		allTimeSum += record.amount;
	}
	
	private void removeSums(Record record){
		if(record.date.after(today)){
			dailySum -= record.amount;
		}
		if(record.date.after(startOfMonth)){
			monthlySum -= record.amount;
			monthlySumByCategory.put(record.category.getID(),
					monthlySumByCategory.get(record.category.getID()) - record.amount);
		}
		if(record.date.after(startOfYear)){
			yearlySum -= record.amount;
		}
		allTimeSum -= record.amount;
	}
	
	/**
	 * Find the reference to the record with the same id
	 * @param record
	 * @return A record with the same id as record
	 */
	private T findRecord(long id){
		return recordsById.get(id);
	}
	
	public T updateRecord(long id, T updated) throws RecordUpdateException{
		removeRecord(id);
		updated.id = id;
		return addNewRecord(updated);
	}
	
	public void removeRecord(long id) throws RecordUpdateException{
		T record = findRecord(id);
		System.out.println(id);
		if(record == null){
			throw new RecordUpdateException("Record does not exist.");
		}
		removeSums(record);
		records.get(record.date).remove(record);
		recordsByCategory.get(record.category.getID()).remove(record);
		recordsByName.get(record.name).remove(record);
		recordsById.remove(id);
		markUpdate();
	}
	
	@SuppressWarnings("unchecked")
	public T addNewRecord(T toAdd) throws RecordUpdateException{
		T record = (T)toAdd.copy();
		record.category = categories.get(toAdd.category.getID());
		if(record.category == null){
			throw new RecordUpdateException("Invalid category!");
		}
		while(recordsById.containsKey(record.id)){
			record.id = (new Date()).getTime() + ran.nextInt();
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
		recordsById.put(record.id, record);
		addSums(record);
		markUpdate();
		return record;
	}
	
	public boolean removeCategory(Category category){
		return removeCategory(category.getID());
	}
	
	public void updateCategory(long id, String newName) throws CategoryUpdateException{
		if(categories.get(id) == null){
			throw new CategoryUpdateException("The category with the id does not exist!");
		}
		categories.get(id).setName(newName);
		markUpdate();
	}
	
	public Category addNewCategory(Category toAdd){
		Category category = toAdd.copy();
		while(categories.containsKey(category.getID())){
			category.id = (new Date()).getTime() + ran.nextInt();
		}
		categories.put(category.getID(), category);
		monthlySumByCategory.put(category.getID(), 0.0);
		markUpdate();
		return category;
	}
	
	public Category getCategory(Long id){
		return categories.get(id);
	}
	
	public T getRecordBy(long id){
		T r = findRecord(id);
		if(r == null) return r;
		else return (T)r.copy();
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
	public boolean removeCategory(long identifier) {
		if(recordsByCategory.containsKey(identifier)){
			TreeSet<T> rs = recordsByCategory.get(identifier);
			for(Record r : rs){
				r.category = Category.undefined;
			}
			recordsByCategory.remove(identifier);
		}
		if(categories.containsKey(identifier)){
			categories.remove(identifier);
			monthlySumByCategory.remove(identifier);
			markUpdate();
			return true;
		}else{
			return false;
		}
	}
	@Override
	public List<Category> getAllCategories() {
		return new Vector<Category>(categories.values());
	}
	@Override
	public Category addNewCategory(String catName) {
		return addNewCategory(new Category((new Date()).getTime(), catName));
	}
	@Override
	public boolean updateCategory(long identifier, Category selectedCat) {
		try {
			this.updateCategory(identifier, selectedCat.getName());
			return true;
		} catch (CategoryUpdateException e) {
			return false;
		}
	}
}
