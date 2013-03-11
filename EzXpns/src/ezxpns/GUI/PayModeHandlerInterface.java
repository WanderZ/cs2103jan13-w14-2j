package ezxpns.GUI;

import java.util.List;

/**
 *	Interface to handle the payment modes between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface PayModeHandlerInterface {
	
	/**
	 * Get all defined payment modes
	 * @return List of all the stored payment modes
	 */
	public List<Object> getAllPaymentModes();
	
	/**
	 * To create a new user defined payment mode
	 * @return true if successful, else false
	 */
	public boolean createPaymentMode(Object paymentRef);
	
	/**
	 * To remove a user defined payment mode
	 * @return true if successful, else false
	 */
	public boolean removePaymentMode(Object paymentRef);
	
	/**
	 * To modify a user defined payment mode 
	 * @return true if successful, else false
	 */
	public boolean modifyPaymentMode(Object paymentRef);
}
