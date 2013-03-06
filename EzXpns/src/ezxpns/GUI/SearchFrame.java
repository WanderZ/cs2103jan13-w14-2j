package ezxpns.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

/**
 * The window to handle the searching and querying needs of the user
 */
@SuppressWarnings("serial")
public class SearchFrame extends JFrame {
	
	public final int WIDTH = 600;
	public final int HEIGHT = 400;
	// Time frame of the report (start and end)
	
	// 2 main panels, the top (querying time frame) and the bottom (results, content)
	private JPanel panForm, panResult;
	
	public SearchFrame() {
		super();
		this.init();
		// Layout
		this.setLayout(new BorderLayout());
				
		this.panForm = new SearchFormPanel();
		this.add(this.panForm, BorderLayout.CENTER);
		
		this.panResult = new ResultPanel();
		this.add(this.panResult, BorderLayout.SOUTH);
	}
	
	public void init() {
		this.setTitle("EzXpns - Search");
		this.setBounds(0, 0, WIDTH, HEIGHT); /*x coordinate, y coordinate, width, height*/
		this.setLocationRelativeTo(null); // Set to start in the central area of the screen
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE); 
		// this.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full screen
	    // this.setUndecorated(true); // removes the title bar
	}
}

/**
 * Panel to contain all the query results
 */
@SuppressWarnings("serial")
class ResultPanel extends JPanel {
		
	public ResultPanel() {
		super();
		
	}
	
	// Methods to receive results
	// Methods to format results
	// Methods to display results
	// Hover highlight MouseEvent
	// Paging required?
}

/**
 * Panel to contain all the search fields (Search form elements declaration and initialisation)
 */
@SuppressWarnings("serial")
class SearchFormPanel extends JPanel {
	
	public final int TOP_PAD = 30;
	public final int COL1_PAD = 15;
	public final int COL2_PAD = 120;
	public final int TEXTFIELD_SIZE = 20;
	
	private JRadioButton rbtnOut, rbtnIn;
	private ButtonGroup bgType;
	private JLabel lblName, lblCat, lblPayMode, lblType, lblDateFrom, lblDateTo;
	private JTextField txtName;
	private JComboBox cboxCat, cboxPayMode;
	private JTextField txtDateFrom, txtDateTo;
	private JButton btnSearch, btnDone;
	
	public SearchFormPanel() {
		super();
		
		SpringLayout loForm = new SpringLayout(); // Layout here
		this.setLayout(loForm);
			
		lblType = new JLabel("Type");
		bgType = new ButtonGroup();
		rbtnIn = new JRadioButton("Income");
		rbtnOut = new JRadioButton("Expenses");
		rbtnOut.setSelected(true);
		bgType.add(rbtnOut);
		bgType.add(rbtnIn);
		loForm.putConstraint(SpringLayout.WEST, lblType, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.NORTH, lblType, TOP_PAD, SpringLayout.NORTH, this);
		
		loForm.putConstraint(SpringLayout.WEST, rbtnOut, COL2_PAD, SpringLayout.WEST, lblType);
		loForm.putConstraint(SpringLayout.NORTH, rbtnOut, TOP_PAD, SpringLayout.NORTH, this);
		
		loForm.putConstraint(SpringLayout.WEST, rbtnIn, COL2_PAD, SpringLayout.WEST, rbtnOut);
		loForm.putConstraint(SpringLayout.NORTH, rbtnIn, TOP_PAD, SpringLayout.NORTH, this);
		this.add(lblType);
		this.add(rbtnIn);
		this.add(rbtnOut);
				
		lblName = new JLabel("Name");
		txtName = new JTextField("", TEXTFIELD_SIZE);
		loForm.putConstraint(SpringLayout.WEST, lblName, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtName, COL2_PAD, SpringLayout.WEST, lblName);
		loForm.putConstraint(SpringLayout.NORTH, lblName, TOP_PAD, SpringLayout.NORTH, lblType);
		loForm.putConstraint(SpringLayout.NORTH, txtName, TOP_PAD, SpringLayout.NORTH, rbtnOut);
		this.add(lblName);
		this.add(txtName);
		
		lblCat = new JLabel("Category");
		this.add(lblCat);
		this.add(getCboxCat());
		loForm.putConstraint(SpringLayout.WEST, lblCat, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxCat, COL2_PAD, SpringLayout.WEST, lblCat);
		loForm.putConstraint(SpringLayout.NORTH, lblCat, TOP_PAD, SpringLayout.NORTH, lblName);
		loForm.putConstraint(SpringLayout.NORTH, cboxCat, TOP_PAD, SpringLayout.NORTH, txtName);
		
		this.lblPayMode = new JLabel("Payment Mode");
		this.add(lblPayMode);
		this.add(getPayMode());
		loForm.putConstraint(SpringLayout.WEST, lblPayMode, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, cboxPayMode, COL2_PAD, SpringLayout.WEST, lblPayMode);
		loForm.putConstraint(SpringLayout.NORTH, lblPayMode, TOP_PAD, SpringLayout.NORTH, lblCat);
		loForm.putConstraint(SpringLayout.NORTH, cboxPayMode, TOP_PAD, SpringLayout.NORTH, cboxCat);
		
		this.lblDateFrom = new JLabel("Starting From");
		this.txtDateFrom = new JTextField("", TEXTFIELD_SIZE);
		loForm.putConstraint(SpringLayout.WEST, lblDateFrom, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDateFrom, COL2_PAD, SpringLayout.WEST, lblDateFrom);
		loForm.putConstraint(SpringLayout.NORTH, lblDateFrom, TOP_PAD, SpringLayout.NORTH, lblPayMode);
		loForm.putConstraint(SpringLayout.NORTH, txtDateFrom, TOP_PAD, SpringLayout.NORTH, cboxPayMode);
		this.add(lblDateFrom);
		this.add(txtDateFrom);
		
		this.lblDateTo = new JLabel("Ending at");
		this.txtDateTo = new JTextField("", TEXTFIELD_SIZE);
		loForm.putConstraint(SpringLayout.WEST, lblDateTo, COL1_PAD, SpringLayout.WEST, this);
		loForm.putConstraint(SpringLayout.WEST, txtDateTo, COL2_PAD, SpringLayout.WEST, lblDateTo);
		loForm.putConstraint(SpringLayout.NORTH, lblDateTo, TOP_PAD, SpringLayout.NORTH, lblDateFrom);
		loForm.putConstraint(SpringLayout.NORTH, txtDateTo, TOP_PAD, SpringLayout.NORTH, txtDateFrom);
		this.add(lblDateTo);
		this.add(txtDateTo);
		
		this.btnSearch = new JButton("Find it!");
		loForm.putConstraint(SpringLayout.WEST, btnSearch, COL2_PAD, SpringLayout.EAST, txtDateFrom);
		loForm.putConstraint(SpringLayout.NORTH, btnSearch, TOP_PAD, SpringLayout.NORTH, txtDateFrom);
		this.add(this.btnSearch);
		
		btnDone = new JButton("Close Window");
		btnDone.setBorderPainted(false);
		btnDone.setContentAreaFilled(false);
		btnDone.setForeground(Color.DARK_GRAY);
		
		btnDone.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setForeground(Color.BLUE);
				/* drawing the underline for the font. */
				Font btnFont = btn.getFont();
				Map attribute = btnFont.getAttributes();
				attribute.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
				btn.setFont(btnFont.deriveFont(attribute));
			}
			
			public void mouseExited(MouseEvent mEvent) {
				JButton btn = (JButton) mEvent.getSource();
				btn.setForeground(Color.DARK_GRAY);
				// Error - unable to remove the underline.
			}
		});
		loForm.putConstraint(SpringLayout.WEST, btnDone, COL2_PAD, SpringLayout.EAST, txtDateTo);
		loForm.putConstraint(SpringLayout.NORTH, btnDone, TOP_PAD, SpringLayout.NORTH, txtDateTo);
		this.add(this.btnDone);
	}
	
	private JComboBox getCboxCat() {
		// Retrieve user-defined categories here
		if(this.cboxCat==null) {
			this.cboxCat = new JComboBox(getCategories());
		}
		return this.cboxCat;
	}
	
	private String[] getCategories() {
		return new String[] {"Food", "Transport"};
	}
	
	private JComboBox getPayMode() {
		// Retrieve all defined payment modes here
		if(cboxPayMode == null) {
			this.cboxPayMode = new JComboBox(getPaymentModes());
		}
		return cboxPayMode;
	}
	
	private String[] getPaymentModes() {
		return new String[] {"Cash", "Cheque"};
	}
		
	// Methods to package and send requests to the middle man for querying
	// Listener methods for Search button
}
