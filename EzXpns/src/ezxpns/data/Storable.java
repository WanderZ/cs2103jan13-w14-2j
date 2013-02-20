package ezxpns.data;

public interface Storable {
	/**
	 * @return if the object has been updated and needs to be stored again
	 */
	public boolean isUpdated();
	
	/**
	 * Tells the object that it has been stored
	 */
	public void saved();
}
