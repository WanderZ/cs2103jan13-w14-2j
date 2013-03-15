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
	
	public final int DEFAULT_WIDTH = 600;
	public final int DEFAULT_HEIGHT = 400;
	
	private SearchHandlerInterface handler;
	
	// 2 main panels, the top (querying time frame) and the bottom (results, content)
	private SearchFormPanel panForm;
	private ResultPanel panResult;
	
	public SearchFrame(SearchHandlerInterface handlerRef) {
		super();
		this.init();
		
		this.handler = handlerRef;
		
		this.setLayout(new BorderLayout());
				
		this.panForm = new SearchFormPanel();
		this.add(this.panForm, BorderLayout.CENTER);
		
		this.panResult = new ResultPanel();
		this.add(this.panResult, BorderLayout.SOUTH);
	}
	
	public void init() {
		this.setTitle("EzXpns - Search");
		this.setBounds(0, 0, DEFAULT_WIDTH, DEFAULT_HEIGHT); /*x coordinate, y coordinate, width, height*/
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

class SearchFormPanel extends JPanel {
	
	private JLabel lblName, lblTitle;
	private JTextField txtName;
	
	public SearchFormPanel() {
		
	}
	
	private JLabel getTitleLabel() {
		if(lblTitle == null) {
			lblTitle = new JLabel("Simple Search");
			lblTitle.setFont(new Font("", 0, 30));
		}
		return lblTitle;
	}
}

class SearchBtnPanel extends JPanel {
	
	private JButton btnSearch, btnCancel;
	
	public SearchBtnPanel() {
		
	}
}
