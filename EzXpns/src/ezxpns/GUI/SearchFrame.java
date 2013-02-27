import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;


/**
 * The window to handle the searching and querying needs of the user
 * @author Andrew
 *
 */
@SuppressWarnings("serial")
public class SearchFrame extends JFrame {
	
	// Time frame of the report (start and end)
	
	// 2 main panels, the top (querying timeframe) and the bottom (results, content)
	private JPanel panResult;
	
	public SearchFrame() {
		super();
		
		this.panResult = new ResultPanel();
	}
}

/**
 * Panel to contain all the query results
 * @author Andrew
 *
 */
@SuppressWarnings("serial")
class ResultPanel extends JPanel {
	
	private JButton btnDone;
	
	public ResultPanel() {
		super();
		
		this.btnDone = new JButton("Done");
		
	}
	
	// Methods to receive results
	// Methods to format results
	// Methods to display results
	// Hover highlight MouseEvent
	// Paging required?
}

/**
 * Panel to contain all the search fields (Search form elements declaration and initialization)
 * @author Andrew
 *
 */
@SuppressWarnings("serial")
class SearchFormPanel extends JPanel {
	
	
	private JRadioButton rbtnOut, rbtnIn;
	private ButtonGroup bgType;
	private JTextField txtName;
	private JComboBox cboxCat, cboxPayMode;
	private JTextField txtDateFrom, txtDateTo;
	private JButton btnSearch;
	
	public SearchFormPanel() {
		super();
		
		
		
		this.btnSearch = new JButton("Search");
		this.add(this.btnSearch);
	}
	
	// Methods to package and send requests to the middle man for querying
	// Listener methods for Search button
}