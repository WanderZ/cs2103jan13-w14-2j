package ezxpns.GUI;

import javax.swing.AbstractListModel;
import ezxpns.data.records.*;
import java.util.*;

public class CategoryModel extends AbstractListModel {
	public CategoryModel(CategoryHandlerInterface cat, Category toadd){
		this.cat = cat;
		cats = cat.getAllCategories();
		cats.add(toadd);
		this.toadd = toadd;
	}
	
	private CategoryHandlerInterface cat;
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

	public void update(){
		int s = cats.size();
		cats = new Vector<Category>();
		fireIntervalRemoved(this, 0, s);
		cats = cat.getAllCategories();
		cats.add(toadd);
		fireIntervalAdded(this, 0, cats.size());
	}
}
