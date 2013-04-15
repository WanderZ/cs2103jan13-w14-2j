package ezxpns.data;

import java.io.*;
import java.util.*;

import com.google.gson.*;

/**
 * Manages storage of all persistent data
 * @author A0099621X
 *
 */
public class StorageManager {
	/**
	 * An event listener that will be notified when
	 * storage manager fails to do file IO
	 */
	public static interface StorageEventListener{
		/**
		 * A read IO failure has occurred with the exception supplied
		 * @param e
		 */
		void readFail(IOException e);
		/**
		 * A write IO failure has occurred with the exception supplied
		 * @param e
		 */
		void writeFail(IOException e);
	}
	
	/**
	 * The file to read/write
	 */
	private File file;
	/**
	 * The data to be store. All other data should be under this
	 */
	private DataManager manager;
	/**
	 * A timer to schedule checking and saving of data
	 */
	private Timer timer = new Timer();
//	private Gson gson = new Gson();
	/**
	 * Gson is used to serialize and deserialize data into json
	 */
	private Gson gson = new GsonBuilder().setPrettyPrinting().create(); // for testing purpose, actually doesn't matter much
	/**
	 * All the listeners
	 */
	private Vector<StorageEventListener> listeners = new Vector<StorageEventListener>();
	
	/**
	 * The interval for checking then writing to file <br />
	 * Possible to be moved into constructor
	 */
	private static final int WRITE_INTERVAL = 5 * 1000;
	
	/**
	 * Constructs a storage manager that will write and read from the file
	 * specified by the file path
	 * @param filePath The path to the file for storing data
	 * @throws IOException
	 */
	public StorageManager(String filePath) throws IOException{
		file = new File(filePath);
		
		if(!file.exists()){
			file.createNewFile();
		}
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run(){
				save();
			}
		});
	}
	
	/**
	 * Adds a event listener for IO exceptions
	 */
	public void addEventListener(StorageEventListener listener){
		listeners.add(listener);
	}
	
	/**
	 * Serialize data and write it to the file <br />
	 * Notifies if it fails <br />
	 * Note that this method may be called by the timer
	 */
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
	
	/**
	 * Attempt to save the data. First check if there's update, if yes, writeToFile();
	 */
	private void save(){
		if(manager.isUpdated()){
			writeToFile();
			System.out.println("stored");
		}
		timer.schedule(new TimerTask(){
			public void run(){
				save();
			}
		}, WRITE_INTERVAL);
	}
	
	/**
	 * Deserialize all data from the json file
	 * and start the timer that will attempt to save for each interval
	 */
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
			manager.afterDeserialize();
			writeToFile();
		}else{
			manager.afterDeserialize();
		}
		timer.schedule(new TimerTask(){
			public void run(){
				save();
			}
		}, WRITE_INTERVAL);
	}
	
	/**
	 * Get the data manager that this storage manager is managing
	 * @return the data maanger
	 */
	public DataManager getDataManager(){
		return manager;
	}
}
