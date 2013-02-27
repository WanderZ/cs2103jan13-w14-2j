/**
 *	To handler the categories between the Grapical User Interface and the datastorage
 */
public interface CategoryHandler {
	
	public List<Category> getCategories();
	
	public boolean nwCategory(Category newCat);
	
	public boolean rmCategory(int identifier);
	
	public boolean rmCategory(Category selectedCat);
	
	public boolean mdCategory(Category selectedCat);
	
	// Targets modifier?
	
}