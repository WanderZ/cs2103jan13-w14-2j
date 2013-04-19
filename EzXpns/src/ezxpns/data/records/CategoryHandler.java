package ezxpns.data.records;
import java.util.List;
import java.util.Vector;

/**
 *	Interface to handle the categories between the Graphical User Interface
 *  @author A0099621X
 *  @author A0097973H
 */
public interface CategoryHandler<T extends Record> {
	/**
	 * Get all user defined categories
	 * @return List of all the Categories stored
	 */
	public List<Category> getAllCategories();
	
	/**
	 * Get a category that has the id
	 */
	public Category getCategory(long id);
	
	/**
	 * Create a new category, note that the new category will be a copy with perhaps different id
	 * @param newCat Object containing all the necessary data to create a new category
	 * @return the created category, or null if failed
	 */
	public Category addNewCategory(Category newCat);

	/**
	 * Create a new category with the name
	 * @param name
	 * @return the created category, or null if failed
	 */
	public Category addNewCategory(String name);
	
	/**
	 * Remove a user defined category based on the given identifier
	 * @param identifier The unique identifier for the category to be removed
	 * @return true if successful, else false
	 */
	public boolean removeCategory(long identifier);
	
	/**
	 * Modify Category Method
	 * @param selectedCat The selected category to be modified
	 * @return the modified category, or null if failed
	 */
	public Category updateCategory(long identifier, Category selectedCat);
	
	/**
	 * Check if the name is in used
	 */
	public boolean containsCategoryName(String name);
	
	/**
	 * Validates if the name is acceptable
	 * @return an error string if unacceptable, otherwise null
	 */
	public String validateCategoryName(String name);
	
	/**
	 * Gets Record by the specified Category
	 * @param category category of the record
	 * @param max maximum number of records return, records are ordered by date in decending order
	 * @return records matching the name
	 */
	public Vector<T> getRecordsBy(Category category, int max);
	
	/**
	 * Adds a list of records to a category
	 * 
	 */
	public boolean addToCategory(List<T> records, Category cat);
	
	
	/**
	 * Get a list of categories that has a prefix to its name
	 */
	public Vector<Category> getCategoryWithNamePrefix(String prefix);
}
