package ezxpns.data.records;

import java.util.*;
import java.util.regex.Pattern;

import ezxpns.data.RecordQueryHandler;
import ezxpns.data.Storable;

import ezxpns.GUI.*;

/**
 * A java Generic to manage records and indexing them based on their attributes
 * for fast searching
 * @param <T> the type of records (expense/income) to manage
 * @author A0099621X 
 */
public class RecordManager<T extends Record>
	extends Storable
	implements
		RecordQueryHandler<T>,
		CategoryHandler<T>{
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
	
	static public final Pattern INVALIDNAME = Pattern.compile("[^a-zA-Z0-9 ()-]");
	
	
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
	private transient TreeMap<String, TreeSet<T> > recordsByName;
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
							 lastMonthSum = 0,
							 monthlySum = 0,
							 dailySum = 0,
							 yearlySum = 0;
	private transient HashMap<Long, Double> monthlySumByCategory;
	
	private transient Calendar cal = Calendar.getInstance();
	protected transient Date today, startOfYear, startOfMonth, startOfLastMonth;

	/**
	 * Get sum of amounts of all records
	 * @return sum of amount of all time
	 */
	public double getAllTimeSum() {
		return allTimeSum;
	}
	/**
	 * Get sum of amounts of records belonging to the current month
	 * @return sum of amount of this month
	 */
	public double getMonthlySum() {
		return monthlySum;
	}
	/**
	 * Get sum of amounts of records belonging to the last month
	 * @return sum of amount of last month
	 */
	public double getLastMonthSum() {
		return lastMonthSum;
	}
	
	/**
	 * Get sum of amounts of records belonging to the current day
	 * @return sum of amount of today <br />
	 * Assume the user do not use this app overnight <br />
	 * We are not getting this kind of very committed users with this kind of application <br />
	 * Seriously who will use a desktop finance manager written in java <br />
	 */
	public double getDailySum() {
		return dailySum;
	}
	/**
	 * Get sum of amounts of records belonging to the current year
	 * @return sum of amount of this year
	 */
	public double getYearlySum() {
		return yearlySum;
	}
	
	/**
	 * Get sum of amounts of records belonging to current month and the specified category
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
		recordsByName = new TreeMap<String, TreeSet<T> >();
		recordsById = new TreeMap<Long, T>();
		monthlySumByCategory = new HashMap<Long, Double>();
		cal.set(Calendar.AM_PM, Calendar.AM);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		today = cal.getTime();
		cal.set(Calendar.DAY_OF_MONTH, 1);
		startOfMonth = cal.getTime();
		System.out.println(startOfMonth);
		if(cal.get(Calendar.MONTH) == 0){
			cal.set(Calendar.MONTH, 11);
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) - 1);
			startOfLastMonth = cal.getTime();
			cal.set(Calendar.MONTH, 0);
			cal.set(Calendar.YEAR, cal.get(Calendar.YEAR) + 1);
		}else{
			cal.set(Calendar.MONTH, cal.get(Calendar.MONTH) - 1);
			startOfLastMonth = cal.getTime();
		}
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
		if(!recordsByCategory.containsKey(Category.undefined.id)){
			recordsByCategory.put(Category.undefined.id, new TreeSet<T>());
		}
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
	protected void addSums(T record){
		if(!record.date.before(today)){
			dailySum += record.amount;
		}
		if(!record.date.before(startOfMonth)){
			monthlySum += record.amount;
			monthlySumByCategory.put(record.category.getID(),
					monthlySumByCategory.get(record.category.getID()) + record.amount);
		}else if (!record.date.before(startOfLastMonth)){
			lastMonthSum += record.amount;
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
	protected void removeSums(T record){
		if(!record.date.before(today)){
			dailySum -= record.amount;
		}
		if(!record.date.before(startOfMonth)){
			monthlySum -= record.amount;
			monthlySumByCategory.put(record.category.getID(),
					monthlySumByCategory.get(record.category.getID()) - record.amount);
		}else if (!record.date.before(startOfLastMonth)){
			lastMonthSum -= record.amount;
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
		if(!record.date.before(today)){
			record.date = new Date();
		}
		if(record.category == null){
			throw new RecordUpdateException("Invalid category!");
		}
		while(recordsById.containsKey(record.id)){
			record.id = (new Date()).getTime() + ran.nextInt();
		}
		recordsById.put(record.id, record);
		recordAdded(record);
		markUpdate();
		return record;
	}
	
	protected void recordAdded(T record){
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
		addSums(record);
	}
	
	/**
	 * Remove a category
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
		//same name, don't bother
		if(newName.equals(categories.get(id).name))return;
		String err = validateCategoryName(newName);
		if(err != null)throw new CategoryUpdateException(newName);
		categories.get(id).setName(newName);
		markUpdate();
	}
	
	@Override
	public Category addNewCategory(Category toAdd){
		String err = validateCategoryName(toAdd.name);
		if(err != null)return null;
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
	 * Get a category base on id
	 * @param id
	 * @return category with id
	 */
	public Category getCategory(Long id){
		return categories.get(id);
	}
	
	
	/**
	 * Get a record by id
	 * @param id
	 * @return a record with id, or null if not found
	 */
	public T getRecordBy(long id){
		return findRecord(id);
	}
	
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
			start = new Date(Math.max(start.getTime() - 1, 0));
			allrecs = recordsByDate.descendingMap().subMap(end, start).values();
		}else{
			end = new Date(end.getTime() + 1);
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
			TreeSet<T> nrs = recordsByCategory.get(Category.undefined.id);
			for(T r : rs){
				r.category = Category.undefined;
				nrs.add(r);
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

	@Override
	public boolean containsCategoryName(String name) {
		for(Category cat : categories.values()){
			if(cat.name.equals(name))return true;
		}
		return false;
	}
	@Override
	public String validateCategoryName(String name) {
		if(name.length() < 2)return "Category name is too short";
		if(name.length() > 20)return "Category name is too long";
		if(INVALIDNAME.matcher(name).find()){
			return "Category name contains invalid character";
		}
		if(name.contains(" "))return "Category name should not contain spaces";
		if(containsCategoryName(name))return "Category name is used.";
		return null;
	}
	@Override
	public Category getCategory(long id) {
		return categories.get(id);
	}
	@Override
	public boolean addToCategory(List<T> records, Category cat) {
		// DANGER: though it will return false when fail, the state could have been changed
		// test diligently when using this function
		cat = getCategory(cat.id);
		for(T r : records){
			try {
				this.removeRecord(r.id);
			} catch (RecordUpdateException e) {
				return false;
			}
			r.category = cat;
			try {
				this.addNewRecord(r);
			} catch (RecordUpdateException e) {
				return false;
			}
		}
		return true;
	}
	@Override
	public Vector<T> getRecordsWithNamePrefix(String prefix) {
		if(prefix.length() == 0)return new Vector<T>();
		String end = prefix.substring(0, prefix.length() - 1) + (char)(prefix.charAt(prefix.length() - 1) + 1);
		Vector<T> allRecs = new Vector<T>();
		for(TreeSet<T> rs : recordsByName.subMap(prefix, end).values()){
			allRecs.addAll(rs);
		}
		return allRecs;
	}
	@Override
	public Vector<Category> getCategoryWithNamePrefix(String prefix) {
		if(prefix.length() == 0)return new Vector<Category>();
		Vector<Category> cats = new Vector<Category>();
		for(Category cat : categories.values()){
			if(cat.getName().startsWith(prefix)){
				cats.add(cat);
			}
		}
		return cats;
	}
}
