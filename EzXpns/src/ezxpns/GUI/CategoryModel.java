package ezxpns.GUI;

import javax.swing.AbstractListModel;
import ezxpns.data.records.*;

import java.util.*;

/**
 * A model used to display list of category in category frame
 * @author yyjhao
 */
@SuppressWarnings("serial")
public class CategoryModel extends AbstractListModel {
	/**
	 * @param cat the data source of category
	 * @param toadd a list item that upon selected, let the user add new category
	 */
	public CategoryModel(CategoryHandler cat, Category toadd){
		this.cat = cat;
		cats = cat.getAllCategories();
		cats.add(toadd);
		this.toadd = toadd;
	}
	
	private CategoryHandler cat;
	private List<Category> cats;
	private Category toadd;
	
	@Override
	public Object getElementAt(int arg0) {
		return cats.get(arg0);
	}

	@Override
	public int getSize() {
		return cats.size();
	}

	/**
	 * Refresh the whole list
	 */
	public void update(){
		int s = cats.size();
		cats = new Vector<Category>();
		fireIntervalRemoved(this, 0, s);
		cats = cat.getAllCategories();
		cats.add(toadd);
		fireIntervalAdded(this, 0, cats.size());
	}
}
