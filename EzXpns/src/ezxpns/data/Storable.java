package ezxpns.data;

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
	 * Mark the object as updated
	 */
	protected void markUpdate(){
		updated = true;
	}
}
