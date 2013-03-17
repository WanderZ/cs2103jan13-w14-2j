package ezxpns.data.records;

import java.util.List;
import ezxpns.data.records.*;

/**
 *	Interface to handle the payment modes between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface PayMethodHandler {
	
	/**
	 * Get all defined payment modes
	 * @return List of all the stored payment modes
	 */
	public List<PaymentMethod> getAllPaymentMethod();
	
	/**
	 * To create a new user defined payment mode
	 * @return true if successful, else false
	 */
	public boolean addNewPaymentMethod(PaymentMethod paymentRef);
	
	/**
	 * To remove a user defined payment mode
	 * @return true if successful, else false
	 */
	public boolean removePaymentMethod(PaymentMethod paymentRef);
	
	/**
	 * To modify a user defined payment mode 
	 * @return true if successful, else false
	 */
	public boolean updatePaymentMethod(PaymentMethod paymentRef);
}
