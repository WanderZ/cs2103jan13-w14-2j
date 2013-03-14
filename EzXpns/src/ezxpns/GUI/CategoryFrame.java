package ezxpns.GUI;

import javax.swing.JFrame;

import ezxpns.data.TargetManager;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.AbstractListModel;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.BoxLayout;
import java.awt.FlowLayout;
import javax.swing.JSplitPane;
import javax.swing.JLabel;
import java.awt.GridLayout;
import java.awt.CardLayout;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Window for users to manage their targets and / or categories [?]
 */
@SuppressWarnings("serial")
public class CategoryFrame extends JFrame {
	
	private TargetManager targetMgr;
	private CategoryHandlerInterface excats, incats;
	
	JList exlist, inlist;
	private JTextField textField;
	private JTextField textField_1;
	
	public CategoryFrame(CategoryHandlerInterface excats,
			CategoryHandlerInterface incats, TargetManager targetMgrRef) {
		super(); // new JFrame();
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		this.excats = excats;
		this.incats = incats;
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Expense", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		inlist = new JList();
		panel_1.add(inlist, BorderLayout.CENTER);
		inlist.setMinimumSize(new Dimension(150, 500));
		inlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{34, 0, 98, 0};
		gbl_panel.rowHeights = new int[]{29, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 2;
		panel.add(lblName, gbc_lblName);
		
		textField_1 = new JTextField();
		GridBagConstraints gbc_textField_1 = new GridBagConstraints();
		gbc_textField_1.insets = new Insets(0, 0, 5, 0);
		gbc_textField_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_textField_1.gridx = 2;
		gbc_textField_1.gridy = 2;
		panel.add(textField_1, gbc_textField_1);
		textField_1.setColumns(10);
		
		JLabel lblTargetAmount = new JLabel("Target Amount:");
		GridBagConstraints gbc_lblTargetAmount = new GridBagConstraints();
		gbc_lblTargetAmount.anchor = GridBagConstraints.WEST;
		gbc_lblTargetAmount.insets = new Insets(0, 0, 5, 5);
		gbc_lblTargetAmount.gridx = 1;
		gbc_lblTargetAmount.gridy = 3;
		panel.add(lblTargetAmount, gbc_lblTargetAmount);
		
		textField = new JTextField();
		GridBagConstraints gbc_textField = new GridBagConstraints();
		gbc_textField.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField.insets = new Insets(0, 0, 5, 0);
		gbc_textField.gridx = 2;
		gbc_textField.gridy = 3;
		panel.add(textField, gbc_textField);
		textField.setColumns(10);
		
		JButton btnChange = new JButton("Change");
		GridBagConstraints gbc_btnChange = new GridBagConstraints();
		gbc_btnChange.insets = new Insets(0, 0, 0, 5);
		gbc_btnChange.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnChange.gridx = 1;
		gbc_btnChange.gridy = 4;
		panel.add(btnChange, gbc_btnChange);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Income", null, panel_2, null);
		targetMgr = targetMgrRef;
		
		loadList();
		
	}
	
	private void loadList(){
		inlist.setListData(incats.getAllCategories().toArray());
	}

}
