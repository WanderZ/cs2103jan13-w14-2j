package ezxpns.data;

/**
 * Implements basic function to inspect whether data is updated
 * @author yyjhao
 *
 */
public abstract class Storable {
	private transient boolean updated;
	/**
	 * @return if the object has been updated and needs to be stored again
	 */
	public boolean isUpdated(){
		return updated;
	}
	
	/**
	 * Tells the object that it has been stored
	 */
	public void saved(){
		updated = false;
	}
	
	/**
	 * Mark the object as updated so that the storage manager
	 * will attempt to save the data
	 */
	protected void markUpdate(){
		updated = true;
	}
	
	/**
	 * Optional method to populate transient attributes after deserializing data
	 * from json.
	 */
	public void afterDeserialize(){ }
}
