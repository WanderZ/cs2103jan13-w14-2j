package ezxpns.GUI;

import javax.swing.JFrame;

import ezxpns.data.*;
import javax.swing.JList;
import java.awt.BorderLayout;
import java.awt.Dimension;
import ezxpns.data.records.*;

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
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;

/**
 * Window for users to manage their targets and / or categories [?]
 */
@SuppressWarnings("serial")
public class CategoryFrame extends JFrame {
	
	private TargetManager targetMgr;
	private CategoryHandlerInterface excats, incats;
	
	JList exlist, inlist;
	private JTextField targetAmountField;
	private JTextField exnameField;
	private JTextField textField_2;
	
	private Category curCat;
	private Target curTar;
	
	public CategoryFrame(CategoryHandlerInterface excats,
			CategoryHandlerInterface incats, TargetManager targetMgrRef) {
		super(); // new JFrame();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				loadExList();
				loadInList();
			}
		});
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		this.excats = excats;
		this.incats = incats;
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("Expense", null, panel_1, null);
		panel_1.setLayout(new BorderLayout(0, 0));
		
		exlist = new JList();
		exlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				updateExDisplay((Category) exlist.getSelectedValue());
			}
		});
		panel_1.add(exlist, BorderLayout.CENTER);
		exlist.setMinimumSize(new Dimension(150, 500));
		exlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panel = new JPanel();
		panel_1.add(panel, BorderLayout.EAST);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]{34, 0, 98, 0};
		gbl_panel.rowHeights = new int[]{29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel.columnWeights = new double[]{0.0, 0.0, 1.0, Double.MIN_VALUE};
		gbl_panel.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel.setLayout(gbl_panel);
		
		JLabel lblName = new JLabel("Name:");
		lblName.setHorizontalAlignment(SwingConstants.LEFT);
		GridBagConstraints gbc_lblName = new GridBagConstraints();
		gbc_lblName.anchor = GridBagConstraints.EAST;
		gbc_lblName.insets = new Insets(0, 0, 5, 5);
		gbc_lblName.gridx = 1;
		gbc_lblName.gridy = 2;
		panel.add(lblName, gbc_lblName);
		
		exnameField = new JTextField();
		GridBagConstraints gbc_exnameField = new GridBagConstraints();
		gbc_exnameField.insets = new Insets(0, 0, 5, 0);
		gbc_exnameField.fill = GridBagConstraints.HORIZONTAL;
		gbc_exnameField.gridx = 2;
		gbc_exnameField.gridy = 2;
		panel.add(exnameField, gbc_exnameField);
		exnameField.setColumns(10);
		
		JLabel lblTargetAmount = new JLabel("Target Amount:");
		GridBagConstraints gbc_lblTargetAmount = new GridBagConstraints();
		gbc_lblTargetAmount.anchor = GridBagConstraints.WEST;
		gbc_lblTargetAmount.insets = new Insets(0, 0, 5, 5);
		gbc_lblTargetAmount.gridx = 1;
		gbc_lblTargetAmount.gridy = 3;
		panel.add(lblTargetAmount, gbc_lblTargetAmount);
		
		targetAmountField = new JTextField();
		GridBagConstraints gbc_targetAmountField = new GridBagConstraints();
		gbc_targetAmountField.anchor = GridBagConstraints.NORTHWEST;
		gbc_targetAmountField.insets = new Insets(0, 0, 5, 0);
		gbc_targetAmountField.gridx = 2;
		gbc_targetAmountField.gridy = 3;
		panel.add(targetAmountField, gbc_targetAmountField);
		targetAmountField.setColumns(10);
		
		JButton changeExBtn = new JButton("Change");
		GridBagConstraints gbc_changeExBtn = new GridBagConstraints();
		gbc_changeExBtn.insets = new Insets(0, 0, 5, 5);
		gbc_changeExBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_changeExBtn.gridx = 1;
		gbc_changeExBtn.gridy = 4;
		panel.add(changeExBtn, gbc_changeExBtn);
		
		JButton btnRemove_1 = new JButton("Remove");
		GridBagConstraints gbc_btnRemove_1 = new GridBagConstraints();
		gbc_btnRemove_1.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove_1.gridx = 2;
		gbc_btnRemove_1.gridy = 4;
		panel.add(btnRemove_1, gbc_btnRemove_1);
		
		JButton btnNew_1 = new JButton("New");
		GridBagConstraints gbc_btnNew_1 = new GridBagConstraints();
		gbc_btnNew_1.gridx = 2;
		gbc_btnNew_1.gridy = 9;
		panel.add(btnNew_1, gbc_btnNew_1);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("Income", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		inlist = new JList();
		inlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inlist.setMinimumSize(new Dimension(150, 500));
		panel_2.add(inlist);
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.EAST);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{40, 0, 134, 0};
		gbl_panel_3.rowHeights = new int[]{29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JLabel lblName_1 = new JLabel("Name:");
		GridBagConstraints gbc_lblName_1 = new GridBagConstraints();
		gbc_lblName_1.anchor = GridBagConstraints.WEST;
		gbc_lblName_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblName_1.gridx = 1;
		gbc_lblName_1.gridy = 3;
		panel_3.add(lblName_1, gbc_lblName_1);
		
		textField_2 = new JTextField();
		GridBagConstraints gbc_textField_2 = new GridBagConstraints();
		gbc_textField_2.anchor = GridBagConstraints.NORTHWEST;
		gbc_textField_2.insets = new Insets(0, 0, 5, 0);
		gbc_textField_2.gridx = 2;
		gbc_textField_2.gridy = 3;
		panel_3.add(textField_2, gbc_textField_2);
		textField_2.setColumns(10);
		
		JButton btnChange_1 = new JButton("Change");
		GridBagConstraints gbc_btnChange_1 = new GridBagConstraints();
		gbc_btnChange_1.insets = new Insets(0, 0, 5, 5);
		gbc_btnChange_1.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnChange_1.gridx = 1;
		gbc_btnChange_1.gridy = 4;
		panel_3.add(btnChange_1, gbc_btnChange_1);
		
		JButton btnRemove = new JButton("Remove");
		GridBagConstraints gbc_btnRemove = new GridBagConstraints();
		gbc_btnRemove.insets = new Insets(0, 0, 5, 0);
		gbc_btnRemove.gridx = 2;
		gbc_btnRemove.gridy = 4;
		panel_3.add(btnRemove, gbc_btnRemove);
		
		JButton btnNew = new JButton("New");
		GridBagConstraints gbc_btnNew = new GridBagConstraints();
		gbc_btnNew.gridx = 2;
		gbc_btnNew.gridy = 9;
		panel_3.add(btnNew, gbc_btnNew);
		targetMgr = targetMgrRef;
		
	}
	
	private void loadExList(){
		exlist.setListData(excats.getAllCategories().toArray());
	}
	
	private void loadInList(){
		inlist.setListData(incats.getAllCategories().toArray());
	}

	private void updateExDisplay(Category cat){
		curCat = cat;
		exnameField.setText(cat.getName());
		curTar = targetMgr.getTarget(cat);
		if(curTar == null){
			targetAmountField.setText("No target set");
		}else{
			targetAmountField.setText("" + curTar.getTarget());
		}
	}
}
