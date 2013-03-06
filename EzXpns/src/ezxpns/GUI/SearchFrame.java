package ezxpns.GUI;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * The window to handle the searching and querying needs of the user
 */
@SuppressWarnings("serial")
public class SearchFrame extends JFrame {
	
	// Time frame of the report (start and end)
	
	// 2 main panels, the top (querying timeframe) and the bottom (results, content)
	private JPanel panForm, panResult;
	
	public SearchFrame() {
		super();
		
		this.panForm = new SearchFormPanel();
		this.add(this.panForm);
		
		this.panResult = new ResultPanel();
		this.add(this.panResult);
	}
	
	@Override
	public void closeFrame() { this.dispose(); }
}

/**
 * Panel to contain all the query results
 */
@SuppressWarnings("serial")
class ResultPanel extends JPanel {
	
	private JButton btnDone;
	
	public ResultPanel() {
		super();
		// this.btnDone.addActionListener(/*ActionListener module here*/);
		this.btnDone = new JButton("Close Window");
		
	}
	
	// Methods to receive results
	// Methods to format results
	// Methods to display results
	// Hover highlight MouseEvent
	// Paging required?
}

/**
 * Panel to contain all the search fields (Search form elements declaration and initialization)
 */
@SuppressWarnings("serial")
class SearchFormPanel extends JPanel {
	
	private JRadioButton rbtnOut, rbtnIn;
	private ButtonGroup bgType;
	private JLabel lblName, lblCat, lblPayMode, lblType, lblDateFrom, lblDateTo;
	private JTextField txtName;
	private JComboBox cboxCat, cboxPayMode;
	private JTextField txtDateFrom, txtDateTo;
	private JButton btnSearch;
	
	public SearchFormPanel() {
		super();
		
		// Layout here
		
		this.lblType = new JLabel("Type");
		this.bgType = new ButtonGroup();
		this.rbtnIn = new JRadioButton("Income");
		this.rbtnOut = new JRadioButton("Expenses");
		this.rbtnOut.setSelected(true);
		this.bgType.add(rbtnOut);
		this.bgType.add(rbtnIn);
		
		this.lblName = new JLabel("Name");
		this.txtName = new JTextField("", 20);
		
		this.lblCat = new JLabel("Category");
		this.loadCategories();
		
		this.lblPayMode = new JLabel("Payment Mode");
		this.loadPaymentModes();
		
		this.lblDateFrom = new JLabel("Starting From");
		this.txtDateFrom = new JTextField("", 20);
		
		this.lblDateTo = new JLabel("Ending at");
		this.txtDateTo = new JTextField("", 20);
		
		this.btnSearch = new JButton("Search");
		this.add(this.btnSearch);
	}
	
	
	private void loadCategories() {
		// Retrieve user-defined categories here
		this.cboxCat = new JComboBox(getCategories());
	}
	
	private String[] getCategories() {
		return new String[] {"Food", "Transport"};
	}
	
	private void loadPaymentModes() {
		// Retrieve all defined payment modes here
		this.cboxPayMode = new JComboBox(getPaymentModes());
	}
	
	private String[] getPaymentModes() {
		return new String[] {"Cash", "Cheque"};
	}
	
	// Methods to package and send requests to the middle man for querying
	// Listener methods for Search button
}