package ezxpns.data.records;

import java.util.*;

import ezxpns.data.RecordQueryHandler;
import ezxpns.data.Storable;

import ezxpns.GUI.*;

/**
 * A java Generic to manage records
 * @param <T> the type of records (expense/income) to manage
 * @author yyjhao 
 */
public class RecordManager<T extends Record>
	extends Storable
	implements
		RecordQueryHandler<T>,
		CategoryHandler{
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
	
	
	/**
	 * all categories
	 */
	private HashMap<Long, Category> categories;
	/**
	 * The only non-transient record data structure,
	 * this is the part that gets serilize into json
	 * <br /> Key is id of category
	 */
	private HashMap<Long, TreeSet<T> > recordsByCategory;
	private transient TreeMap<Date, Vector<T> > recordsByDate;
	private transient HashMap<String, TreeSet<T> > recordsByName;
	protected transient TreeMap<Long, T> recordsById;
	
	/**
	 * A random generator used to re-generate id when the supplied
	 * id to new record is repeated
	 */
	protected transient Random ran = new Random();
	
	/**
	 * aggregate info
	 */
	private transient double allTimeSum = 0,
							 monthlySum = 0,
							 dailySum = 0,
							 yearlySum = 0;
	private transient HashMap<Long, Double> monthlySumByCategory;
	
	private transient Calendar cal = Calendar.getInstance();
	private transient Date today, startOfYear, startOfMonth;

	/**
	 * @return sum of amount of all time
	 */
	public double getAllTimeSum() {
		return allTimeSum;
	}
	/**
	 * @return sum of amount of this month
	 */
	public double getMonthlySum() {
		return monthlySum;
	}
	/**
	 * @return sum of amount of today
	 * Assume the user do not use this app overnight <br />
	 * We are not getting this kind of very committed users with this kind of application <br />
	 * Seriously who will use a desktop finance manager written in java <br />
	 * Don't feel bad screwing up this kind of user if he is so keen to use it overnight
	 */
	public double getDailySum() {
		return dailySum;
	}
	/**
	 * @return sum of amount of this year
	 */
	public double getYearlySum() {
		return yearlySum;
	}
	
	/**
	 * @param cat category
	 * @return sum of amount of a category this month
	 */
	public double getMonthlySum(Category cat){
		return monthlySumByCategory.get(cat.getID());
	}
	public RecordManager(){
		categories = new HashMap<Long, Category>();
		categories.put(Category.undefined.getID(), Category.undefined);
		recordsByCategory = new HashMap<Long, TreeSet<T> >();
		recordsByDate = new TreeMap<Date, Vector<T> >();
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
	
	@Override
	public void afterDeserialize(){
		for(long c : categories.keySet()){
			monthlySumByCategory.put(c, 0.0);
		}
		categories.remove(0);
		categories.put(0l, Category.undefined);
		ran = new Random();
		for(Map.Entry<Long, TreeSet<T> > entry : recordsByCategory.entrySet()){
			TreeSet<T> rs = entry.getValue();
			Category cat = getCategory(entry.getKey());
			for(T record : rs){
				record.category = cat;
				if(recordsByDate.containsKey(record.date)){
					recordsByDate.get(record.date).add(record);
				}else{
					Vector<T> list = new Vector<T>();
					list.add(record);
					recordsByDate.put(record.date, list);
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
	
	/**
	 * To be called after adding a record, adds up sums with the
	 * amount accordingly
	 * @param record the record added
	 */
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
	
	/**
	 * To be called after removing a record, minus sum with
	 * the amount accordingly
	 * @param record record removed
	 */
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
	
	
	/**
	 * Update a record. Note that the record stored is not
	 * guaranteed to be the same as updated, since they can
	 * have different ids
	 * @param id
	 * @param updated
	 * @return the changed record
	 * @throws RecordUpdateException
	 */
	public T updateRecord(long id, T updated) throws RecordUpdateException{
		removeRecord(id);
		updated.id = id;
		return addNewRecord(updated);
	}
	
	/**
	 * Remove a record
	 * @param id id of record to be removed
	 * @throws RecordUpdateException
	 */
	public void removeRecord(long id) throws RecordUpdateException{
		T record = findRecord(id);
		if(record == null){
			throw new RecordUpdateException("Record does not exist.");
		}
		recordsById.remove(id);
		recordRemoved(record);
		markUpdate();
	}
	
	protected void recordRemoved(T record){
		removeSums(record);
		recordsByDate.get(record.date).remove(record);
		recordsByCategory.get(record.category.getID()).remove(record);
		recordsByName.get(record.name).remove(record);
	}
	
	/**
	 * Add a new record, returns the record added. Note
	 * that the added record can have different id from the record
	 * supplied. It is also going to be of different reference
	 * @param toAdd
	 * @return
	 * @throws RecordUpdateException
	 */
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
		if(recordsByDate.containsKey(record.date)){
			recordsByDate.get(record.date).add(record);
		}else{
			Vector<T> rs = new Vector<T>();
			rs.add(record);
			recordsByDate.put(record.date, rs);
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
	
	protected void recordAdded(T Record){
		
	}
	
	/**
	 * @param category
	 * @return
	 */
	public boolean removeCategory(Category category){
		return removeCategory(category.getID());
	}
	
	/**
	 * @param id
	 * @param newName
	 * @throws CategoryUpdateException
	 */
	public void updateCategory(long id, String newName) throws CategoryUpdateException{
		if(categories.get(id) == null){
			throw new CategoryUpdateException("The category with the id does not exist!");
		}
		categories.get(id).setName(newName);
		markUpdate();
	}
	
	@Override
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
	
	
	/**
	 * @param id
	 * @return category with id
	 */
	public Category getCategory(Long id){
		return categories.get(id);
	}
	
	
	/**
	 * @param id
	 * @return a record with id, or null if not found
	 */
	public T getRecordBy(long id){
		T r = findRecord(id);
		if(r == null) return r;
		else return (T)r.copy();
	}
	
	@SuppressWarnings("unchecked")
	@Override
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
	
	@Override
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
	
	@Override
	public Vector<T> getRecordsBy(Date start, Date end, int max, boolean reverse){
		Vector<T> rs = new Vector<T>();
		Collection<Vector<T> > allrecs;
		if(reverse){
			start = new Date(Math.max(start.getTime() - 24 * 60 * 1000, 0));
			allrecs = recordsByDate.descendingMap().subMap(end, start).values();
		}else{
			end = new Date(end.getTime() + 24 * 60 * 1000);
			allrecs = recordsByDate.subMap(start, end).values();
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
	@Override
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
		Vector<Category> vec = new Vector<Category>(categories.values());
		vec.remove(Category.undefined);
		return vec;
	}
	@Override
	public Category addNewCategory(String catName) {
		return addNewCategory(new Category((new Date()).getTime(), catName));
	}
	@Override
	public Category updateCategory(long identifier, Category selectedCat) {
		try {
			this.updateCategory(identifier, selectedCat.getName());
			return getCategory(identifier);
		} catch (CategoryUpdateException e) {
			return null;
		}
	}
	
	public Vector<Category> getCategoriesBy(String name){
		Vector<Category> re = new Vector<Category>();
		for(Category c: categories.values()){
			if(c.getName().equals(name)){
				re.add(c);
			}
		}
		return re;
	}
	
	public Vector<T> getRecordsByCategory(String name){
		Vector<Category> cs = getCategoriesBy(name);
		Vector<T> rs = new Vector<T>();
		for(Category c : cs){
			rs.addAll(getRecordsBy(c, -1));
		}
		return rs;
	}
}
