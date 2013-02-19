package ezxpns.data;

import java.io.*;
import java.util.*;

import com.google.gson.*;

/**
 * @author yyjhao
 *
 * Manages the file IO of all the data
 */
public class StorageManager {
	public static interface StorageEventListener{
		void readFail(IOException e);
		void writeFail(IOException e);
	}
	
	private File file;
	private DataManager manager;
	private Timer timer = new Timer();
	private Gson gson = new Gson();
	private Vector<StorageEventListener> listeners = new Vector<StorageEventListener>();
	
	private final int writeInterval = 5 * 1000;
	
	/**
	 * 
	 * @param filePath The path to the file for storing data
	 * @throws IOException
	 */
	public StorageManager(String filePath) throws IOException{
		file = new File(filePath);
		
		if(!file.exists()){
			file.createNewFile();
		}
	}
	
	/*
	 * Need this to handle some exceptions during timer IO
	 */
	public void addEventListener(StorageEventListener listener){
		listeners.add(listener);
	}
	
	private synchronized void writeToFile(){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(gson.toJson(manager));
			out.close();
			manager.saved();
		}catch(IOException e){
			for(StorageEventListener listener : listeners){
				listener.writeFail(e);
			}
		}
	}
	
	public void save(){
		if(manager.isUpdated()){
			writeToFile();
			System.out.println(gson.toJson(manager));
		}
		timer.schedule(new TimerTask(){
			public void run(){
				save();
			}
		}, writeInterval);
	}
	
	public synchronized void read(){
		StringBuilder str = new StringBuilder(2048);
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while (line != null) {
			  str.append(line);
			  line = in.readLine();
			}
			in.close();
		}catch(IOException e){
			for(StorageEventListener listener : listeners){
				listener.readFail(e);
			}
		}
		manager = gson.fromJson(str.toString(), DataManager.class);
		if(manager == null){
			manager = new DataManager();
			writeToFile();
		}
		timer.schedule(new TimerTask(){
			public void run(){
				save();
			}
		}, writeInterval);
	}
	
	public DataManager getDataManager(){
		return manager;
	}
}
