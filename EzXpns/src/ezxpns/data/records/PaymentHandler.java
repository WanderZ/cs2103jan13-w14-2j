package ezxpns.data.records;

import java.util.List;
import ezxpns.data.records.*;

/**
 *	Interface to handle the payment modes between the Graphical User Interface and the data storage (upon GUI Exit)
 */
public interface PaymentHandler {
	
	/**
	 * Get all defined payment modes
	 * @return List of all the stored payment modes
	 */
	public List<PaymentMethod> getAllPaymentMethod();
	
	/**
	 * To create a new user defined payment mode
	 * @return true if successful, else false
	 */
	public PaymentMethod addNewPaymentMethod(PaymentMethod paymentRef);
	
	/**
	 * To remove a user defined payment mode
	 * @return true if successful, else false
	 */
	public boolean removePaymentMethod(long id);
	
	/**
	 * To modify a user defined payment mode 
	 * @return true if successful, else false
	 */
	public PaymentMethod updatePaymentMethod(long id, PaymentMethod paymentRef);
	
	/**
	 * Validate if the name is acceptable
	 * Returns an error string if not or null if yes
	 * note: don't check if the name is not changed as it will return the name is being used
	 */
	public String validatePaymentMethodName(String name);
}