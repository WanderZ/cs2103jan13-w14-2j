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
	}
}
