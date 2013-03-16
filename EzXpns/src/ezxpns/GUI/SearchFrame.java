package ezxpns.GUI;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.util.List;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * The window to handle the searching and querying needs of the user
 */
@SuppressWarnings("serial")
public class SearchFrame extends JFrame implements ActionListener {
	
	public final int DEFAULT_WIDTH = 600;
	public final int DEFAULT_HEIGHT = 400;
	
	private SearchHandlerInterface handler;
	
	// 2 main panels, the top (querying time frame) and the bottom (results, content)
	private SearchFormPanel panForm;
	private SearchBtnPanel panBtns;
	private ResultPanel panResult;
	
	private SearchRequest currReq;
	
	/**
	 * 
	 * @param handlerRef
	 */
	public SearchFrame(SearchHandlerInterface handlerRef) {
		super();
		this.init();
		
		this.handler = handlerRef;
		
		this.setLayout(new BorderLayout());
		
		JPanel panCtrls = new JPanel();
		panCtrls.setLayout(new BorderLayout());
		
		panForm = new SearchFormPanel();
		panCtrls.add(panForm, BorderLayout.CENTER);
		
		panBtns = new SearchBtnPanel(this);
		panCtrls.add(panBtns, BorderLayout.EAST);
		
		this.add(panCtrls, BorderLayout.CENTER);
		
		// this.panResult = new ResultPanel();
		// this.add(this.panResult, BorderLayout.SOUTH);
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

	@Override
	public void actionPerformed(ActionEvent event) {
		// TODO Auto-generated method stub
		if(event.getSource() == panBtns.getSearchBtn()) {
			// User invoked search
			currReq = new SearchRequest(panForm.getNameField().getText().trim());
			List results = handler.search(currReq);
			return;
		}
		
		if(event.getSource() == panBtns.getCancelBtn()) {
			// User invoke cancel
			this.dispose();
		}
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
		super();
		this.setLayout(new BorderLayout());
		JPanel panForm = new JPanel();
		panForm.setLayout(new FlowLayout());
		
		panForm.add(this.getNameLabel());
		panForm.add(this.getNameField());
		
		this.add(getTitleLabel(), BorderLayout.NORTH);
		this.add(panForm, BorderLayout.CENTER);
		
		// 3 main parts
		// 1. Title of the place - technically its on the top of the screen
		// 2. The form for searching - this one would be the simple form first.
		// 3. Buttons to support searching. (not here, outside)
	}
	
	private JLabel getTitleLabel() {
		if(lblTitle == null) {
			lblTitle = new JLabel("Simple Search");
			lblTitle.setFont(new Font("Segoe UI", 0, 30)); // #Font
		}
		return lblTitle;
	}
	
	private JLabel getNameLabel() {
		if(lblTitle == null) {
			lblTitle = new JLabel("Name");
			lblTitle.setFont(new Font("Segoe UI", 0, 18)); // #Font
		}
		return lblTitle;
	}
	
	public JTextField getNameField() {
		if(txtName == null) {
			txtName = new JTextField("", 25);
		}
		return txtName;
	}
}

/**
 * Panel to containing the buttons for functionality
 */
@SuppressWarnings("serial")
class SearchBtnPanel extends JPanel {
	
	private JButton btnSearch, btnCancel;
	
	public SearchBtnPanel(ActionListener listener) {
		super();
		BoxLayout loBtn = new BoxLayout(this, BoxLayout.Y_AXIS);
		this.setLayout(loBtn);
		
		// Layout to make it fill the width completely, and glue to the base
		
		this.add(Box.createVerticalGlue());
		this.add(this.getSearchBtn());
		this.add(this.getCancelBtn());
		
		this.btnSearch.addActionListener(listener);
		this.btnCancel.addActionListener(listener);
	}
	
	public JButton getSearchBtn() {
		if(btnSearch == null) {
			btnSearch = new JButton("Find it!");
			btnSearch.setBackground(Color.DARK_GRAY);
			btnSearch.setForeground(Color.LIGHT_GRAY);
			btnSearch.addMouseListener(new MouseAdapter() {
				
			});
		}
		return btnSearch;
	}
	
	public JButton getCancelBtn() {
		if(btnCancel == null) {
			btnCancel = new JButton("Abondon Everything");
			btnCancel.setContentAreaFilled(false);
			btnCancel.setFocusPainted(false);
			btnCancel.setBorderPainted(false);
			
			btnCancel.addMouseListener(new MouseAdapter() {
				
			});
		}
		return btnCancel;
	}
}
