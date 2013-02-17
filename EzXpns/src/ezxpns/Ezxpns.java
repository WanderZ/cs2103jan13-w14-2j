package ezxpns;

import ezxpns.data.*;
import ezxpns.GUI.*;
import java.awt.EventQueue;
import java.util.*;

import ezxpns.GUI.MainWindow;


public class Ezxpns {
	private StorageManager store;
	private DataManager data;
	
	public Ezxpns(){
		try{
			store = new StorageManager("data.json");
			store.read();
			data = store.getDataManager();
		}catch(Exception e){
			System.out.println(e.toString());
			System.exit(1);
		}
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
//		Category cat = new Category(20, "food");
//		data.expenses().addNewCategory(cat);
//		ExpenseRecord rec = new ExpenseRecord(100, "apple", "nice!", new Date(),
//				cat, ExpenseType.NEED);
//		data.expenses().addNewRecord(rec);
		//do ui stuff
	}
}
