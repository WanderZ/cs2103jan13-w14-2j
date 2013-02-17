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
	private File file;
	private DataManager manager;
	private Timer timer = new Timer();
	private Gson gson = new Gson();
	
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
	
	private void writeToFile(){
		try{
			BufferedWriter out = new BufferedWriter(new FileWriter(file));
			out.write(gson.toJson(manager));
			out.close();
			manager.saved();
		}catch(Exception e){
			// TODO tell the main logic about it.
		}
	}
	
	public void save(){
		if(manager.isUpdated()){
			writeToFile();
		}
		System.out.println(gson.toJson(manager));
		timer.schedule(new TimerTask(){
			public void run(){
				save();
			}
		}, 5 * 1000);
	}
	
	public void read(){
		StringBuilder str = new StringBuilder(2048);
		try{
			BufferedReader in = new BufferedReader(new FileReader(file));
			String line = in.readLine();
			while (line != null) {
			  str.append(line);
			  line = in.readLine();
			}
		}catch(Exception e){
			//TODO tell the main logic about it
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
		}, 5 * 1000);
	}
	
	public DataManager getDataManager(){
		return manager;
	}
}
