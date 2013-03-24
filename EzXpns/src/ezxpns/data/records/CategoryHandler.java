package ezxpns.data.records;
import java.util.List;

/**
 *	Interface to handle the categories between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface CategoryHandler {
	/**
	 * Get all user defined categories
	 * @return List of all the Categories stored
	 */
	public List<Category> getAllCategories();
	
	/**
	 * Create a new category, note that the new category will be a copied with perhaps different id
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
	 * Validate if the name is acceptable
	 * Returns an error string if not or null if yes
	 */
	public String validateCategoryName(String name);
	
}
