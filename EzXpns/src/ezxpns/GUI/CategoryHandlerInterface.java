package ezxpns.GUI;
import ezxpns.data.records.Category;
import java.util.List;

/**
 *	Interface to handle the categories between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface CategoryHandlerInterface {
	
	/**
	 * Get all user defined categories
	 * @return List of all the Categories stored
	 */
	public List<Category> getAllCategories();
	
	/**
	 * Create a new category
	 * @param newCat Object containing all the necessary data to create a new category
	 * @return true if successful, else false
	 */
	public boolean createCategory(Category newCat);
	
	/**
	 * Remove a user defined category based on the given identifier
	 * @param identifier The unique identifier for the category to be removed
	 * @return true if successful, else false
	 */
	public boolean removeCategory(int identifier);
	
	/**
	 * Remove Category
	 * @precond The given category object must contain the identifier in it
	 * @param selectedCat The selected category to be remove
	 * @return true if successful, else false
	 */
	public boolean removeCategory(Category selectedCat);
	
	/**
	 * Modify Category Method
	 * @precond The provided category object must include the identifier in it
	 * @param selectedCat The selected category to be modified
	 * @return true if successful, else false
	 */
	public boolean modifyCategory(Category selectedCat);
	
	// Targets modifier?
	// Set New Target
	// Modify Existing Target
	// Remove Target (if this is possible)
}
