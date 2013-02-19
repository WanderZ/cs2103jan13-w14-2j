package ezxpns.data;

public interface Storable {
	/**
	 * @return is the object has been updated and needs to be stored again
	 */
	public boolean isUpdated();
	
	/**
	 * Tells the object that it has been stored
	 */
	public void saved();
}
