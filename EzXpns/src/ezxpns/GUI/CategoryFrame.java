package ezxpns.GUI;

import javax.swing.JFrame;
import java.util.*;

import ezxpns.data.*;
import javax.swing.*;
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
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

/**
 * Window for users to manage their targets and categories
 */
@SuppressWarnings("serial")
public class CategoryFrame extends JFrame {
	
	private TargetManager targetMgr;
	private CategoryHandler excats, incats;
	private CategoryModel exmo, inmo;
	
	private JList exlist, inlist;
	
	private JTextField targetAmountField;
	private JTextField exnameField;
	private JTextField inNameField;
	private Category addNew;
	
	private Category curCat;
	private Target curTar;
	
	private JButton removeExBtn, changeExBtn;
	private JButton removeInBtn, changeInBtn;
	
	/**
	 * @param excats a categoryhandler for expense categories
	 * @param incats a categoryhandler for income categories
	 * @param targetMgrRef a target manager 
	 */
	public CategoryFrame(CategoryHandler excats,
			CategoryHandler incats, TargetManager targetMgrRef) {
		super(); // new JFrame();
		// interface code generated by window builder
		addNew = new Category(-1, "Add a new category...");
		exmo = new CategoryModel(excats, addNew);
		inmo = new CategoryModel(incats, addNew);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowOpened(WindowEvent arg0) {
				loadExList();
				loadInList();
				inlist.setSelectedIndex(0);
				exlist.setSelectedIndex(0);
			}
		});
		this.setSize(500, 400);
		this.setLocationRelativeTo(null);
		this.excats = excats;
		this.incats = incats;
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent arg0) {
				exlist.setSelectedIndex(0);
			}
		});
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
		exnameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent arg0) {
				exnameField.selectAll();
			}
		});
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
		targetAmountField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				targetAmountField.selectAll();
			}
		});
		GridBagConstraints gbc_targetAmountField = new GridBagConstraints();
		gbc_targetAmountField.anchor = GridBagConstraints.NORTHWEST;
		gbc_targetAmountField.insets = new Insets(0, 0, 5, 0);
		gbc_targetAmountField.gridx = 2;
		gbc_targetAmountField.gridy = 3;
		panel.add(targetAmountField, gbc_targetAmountField);
		targetAmountField.setColumns(10);
		
		changeExBtn = new JButton("Change");
		changeExBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				modifyEx();
			}
		});
		GridBagConstraints gbc_changeExBtn = new GridBagConstraints();
		gbc_changeExBtn.insets = new Insets(0, 0, 5, 5);
		gbc_changeExBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_changeExBtn.gridx = 1;
		gbc_changeExBtn.gridy = 4;
		panel.add(changeExBtn, gbc_changeExBtn);
		
		removeExBtn = new JButton("Remove");
		removeExBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeEx();
			}
		});
		GridBagConstraints gbc_removeExBtn = new GridBagConstraints();
		gbc_removeExBtn.insets = new Insets(0, 0, 5, 0);
		gbc_removeExBtn.gridx = 2;
		gbc_removeExBtn.gridy = 4;
		panel.add(removeExBtn, gbc_removeExBtn);
		
		JPanel panel_2 = new JPanel();
		panel_2.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				inlist.setSelectedIndex(0);
			}
		});
		tabbedPane.addTab("Income", null, panel_2, null);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		inlist = new JList();
		inlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				updateInDisplay((Category) inlist.getSelectedValue());
			}
		});
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
		
		inNameField = new JTextField();
		inNameField.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				inNameField.selectAll();
			}
		});
		GridBagConstraints gbc_inNameField = new GridBagConstraints();
		gbc_inNameField.anchor = GridBagConstraints.NORTHWEST;
		gbc_inNameField.insets = new Insets(0, 0, 5, 0);
		gbc_inNameField.gridx = 2;
		gbc_inNameField.gridy = 3;
		panel_3.add(inNameField, gbc_inNameField);
		inNameField.setColumns(10);
		
		changeInBtn = new JButton("Change");
		changeInBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modifyIn();
			}
		});
		GridBagConstraints gbc_changeInBtn = new GridBagConstraints();
		gbc_changeInBtn.insets = new Insets(0, 0, 5, 5);
		gbc_changeInBtn.anchor = GridBagConstraints.NORTHWEST;
		gbc_changeInBtn.gridx = 1;
		gbc_changeInBtn.gridy = 4;
		panel_3.add(changeInBtn, gbc_changeInBtn);
		
		removeInBtn = new JButton("Remove");
		removeInBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				removeIn();
			}
		});
		GridBagConstraints gbc_removeInBtn = new GridBagConstraints();
		gbc_removeInBtn.insets = new Insets(0, 0, 5, 0);
		gbc_removeInBtn.gridx = 2;
		gbc_removeInBtn.gridy = 4;
		panel_3.add(removeInBtn, gbc_removeInBtn);
		targetMgr = targetMgrRef;
		
		exlist.setModel(exmo);
		inlist.setModel(inmo);
		
	}
	
	/**
	 * load all expense category into list
	 */
	private void loadExList(){
		exmo.update();
	}
	
	/**
	 * load all income category into list
	 */
	private void loadInList(){
		inmo.update();
	}

	/**
	 * Update the right panel display with expense category data
	 * including target
	 * @param cat category to be displayed
	 */
	private void updateExDisplay(Category cat){
		curCat = cat;
		if(cat == null){
			return;
		}
		if(cat != addNew){
			exnameField.setText(cat.getName());
			curTar = targetMgr.getTarget(cat);
			if(curTar == null){
				targetAmountField.setText("No target set");
			}else{
				targetAmountField.setText("" + curTar.getTargetAmt());
			}
			changeExBtn.setText("Change");
			removeExBtn.setEnabled(true);
		}else{
			exnameField.setText("Pick a name");
			targetAmountField.setText("Set a target");
			changeExBtn.setText("Add");
			removeExBtn.setEnabled(false);
		}
	}
	
	/**
	 * Update right panel display with income category
	 * @param cat income category to be displayed
	 */
	private void updateInDisplay(Category cat){
		curCat = cat;
		if(cat == null){
			return;
		}
		if(cat != addNew){
			inNameField.setText(cat.getName());
			changeInBtn.setText("Change");
			removeInBtn.setEnabled(true);
		}else{
			inNameField.setText("Pick a name");
			changeInBtn.setText("Add");
			removeInBtn.setEnabled(false);
		}
	}
	/**
	 * @param name
	 * @return whether the name can be used as a category name
	 */
	private boolean validateName(String name){
		return !name.contains(" ") && name.length() > 2 && name.length() < 20;
	}
	
	/**
	 * @param targetString
	 * @return whether the target amount is valid
	 */
	private boolean validateTarget(String targetString){
		double d;
		try{
			d = Double.parseDouble(targetString);
			return d > 10;
		}catch(NumberFormatException e){
			return false;
		}
	}
	
	/**
	 * modify the expense category and target editing
	 */
	private void modifyEx(){
		if(validateName(exnameField.getText())){
			if(curCat == addNew){
				Category cat = excats.addNewCategory(new Category(exnameField.getText()));
				if(validateTarget(targetAmountField.getText())){
					targetMgr.setTarget(cat, Double.parseDouble(targetAmountField.getText()));
				}
				exmo.update();
				exlist.setSelectedIndex(0);
			}else{
				Category cat = excats.updateCategory(curCat.getID(), new Category(exnameField.getText()));
				if(validateTarget(targetAmountField.getText())){
					targetMgr.setTarget(cat, Double.parseDouble(targetAmountField.getText()));
				}
				exmo.update();
				exlist.setSelectedValue(cat, true);
			}
		}else{
			JOptionPane.showMessageDialog(this, "Invalid name!!!!!!", "Error!!!!!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * modify the income category editing
	 */
	private void modifyIn(){
		if(validateName(inNameField.getText())){
			if(curCat == addNew){
				Category cat = incats.addNewCategory(new Category(inNameField.getText()));
				inmo.update();
				inlist.setSelectedIndex(0);
			}else{
				Category cat = incats.updateCategory(curCat.getID(), new Category(inNameField.getText()));
				inmo.update();
				inlist.setSelectedValue(cat, true);
			}
		}else{
			JOptionPane.showMessageDialog(this, "Invalid name!!!!!!", "Error!!!!!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * Remove the expense category selected
	 */
	private void removeEx(){
		String message = "Are you sure you want to remove this category?\n" +
			    		"All records under this category will have an undefined category!";
		if(JOptionPane.showConfirmDialog(this, message, "what?!",
				JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			excats.removeCategory(curCat.getID());
			exmo.update();
			exlist.setSelectedIndex(0);
		}
	}
	/**
	 * Remove the income category selected
	 */
	private void removeIn(){
			String message = "Are you sure you want to remove this category?\n" +
			    		"All records under this category will have an undefined category!";
		if(JOptionPane.showConfirmDialog(this, message, "what?!",
				JOptionPane.WARNING_MESSAGE, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
			incats.removeCategory(curCat.getID());
			inmo.update();
			inlist.setSelectedIndex(0);
		}
	}
}

