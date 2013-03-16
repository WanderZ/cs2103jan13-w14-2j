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

import ezxpns.data.records.Category;
import ezxpns.data.records.Record;

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

	@Override
	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == panBtns.getSearchBtn()) {
			// User invoked search
			
			this.remove(panResult);
			String sReq = panForm.getNameField().getText().trim();
			if(sReq.equals("")) {
				sReq = panForm.getCatField().getText().trim();
				currReq = new SearchRequest(new Category(sReq));
			}
			else {
				currReq = new SearchRequest(sReq);
			}
			List<Record> results = handler.search(currReq);
			System.out.println("results found: " + results.size());
			this.panResult = new ResultPanel(results);
			this.add(this.panResult, BorderLayout.SOUTH);
			this.validate(); // Force repaint doesn't seem to work here
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
	
	private MultiRecDisplay resultDisplay;
	
	public ResultPanel() {super();}
	
	public ResultPanel(List<Record> results) {
		super(new BorderLayout(5, 5));
		
		resultDisplay = new MultiRecDisplay(results);
		
		this.add(resultDisplay, BorderLayout.CENTER);
	}
}

@SuppressWarnings("serial")
class SearchFormPanel extends JPanel {
	
	private JLabel lblName, lblTitle, lblCat;
	private JTextField txtName, txtCat;
	
	public SearchFormPanel() {
		super();
		this.setLayout(new BorderLayout());
		JPanel panForm = new JPanel();
		panForm.setLayout(new FlowLayout());
		
		panForm.add(this.getNameLabel());
		panForm.add(this.getNameField());
		panForm.add(this.getCatLabel());
		panForm.add(this.getCatField());
		
		this.add(getTitleLabel(), BorderLayout.NORTH);
		this.add(panForm, BorderLayout.CENTER);
	}
	
	private JLabel getTitleLabel() {
		if(lblTitle == null) {
			lblTitle = new JLabel("  Simple Search");
			lblTitle.setFont(new Font("Segoe UI", 0, 30)); // #Font
		}
		return lblTitle;
	}
	
	private JLabel getNameLabel() {
		if(lblName == null) {
			lblName = new JLabel("            Name");
			lblName.setFont(new Font("Segoe UI", 0, 18)); // #Font
		}
		return lblName;
	}
	
	public JTextField getNameField() {
		if(txtName == null) {
			txtName = new JTextField("", 15);
			txtName.setFont(new Font("Segoe UI", 0, 18)); // #Font
		}
		return txtName;
	}
	
	private JLabel getCatLabel() {
		if(lblCat == null) {
			lblCat = new JLabel("        Category");
			lblCat.setFont(new Font("Segoe UI", 0, 18)); // #Font
		}
		return lblCat;
	}
	
	public JTextField getCatField() {
		if(txtCat == null) {
			txtCat = new JTextField("", 15);
			txtCat.setFont(new Font("Segoe UI", 0, 18)); // #Font
		}
		return txtCat;
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
		
		
		this.add(this.getSearchBtn());
		this.add(this.getCancelBtn());
		this.add(Box.createVerticalGlue());
		
		this.btnSearch.addActionListener(listener);
		this.btnCancel.addActionListener(listener);
	}
	
	public JButton getSearchBtn() {
		if(btnSearch == null) {
			btnSearch = new JButton("Find it!");
			btnSearch.setBackground(Color.LIGHT_GRAY);
			btnSearch.setForeground(Color.BLACK);
			btnSearch.addMouseListener(new MouseAdapter() {
				// Hover 
				// Hover off
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
				// Hover
				// Hover off
			});
		}
		return btnCancel;
	}
}
