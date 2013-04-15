package ezxpns.GUI;

import ezxpns.data.*;
import javax.swing.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import ezxpns.data.records.*;

import javax.swing.AbstractAction;
import javax.swing.ListSelectionModel;
import javax.swing.JTabbedPane;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.List;

/**
 * Panel for users to manage user-defined Categories as well as tagged Targets
 * <br /> Generated by WindowBuilder
 * @author A0099621X
 */
@SuppressWarnings("serial")
public class CategoryTargetPanel extends JPanel {
	public static final int MAX_CATS = 20;
	
	private TargetManager targetMgr;
	private CategoryHandler<ExpenseRecord> excats;
	private CategoryHandler<IncomeRecord> incats;
	private CategoryModel exmo, inmo;
	
	private JList exlist, inlist;
	
	private JTextField targetAmountField;
	private JTextField exnameField;
	private JTextField inNameField;
	private Category addNew;
	private Category curExCat, curInCat;
	private Target curTar;
	
	private JButton removeExBtn, changeExBtn;
	private JButton removeInBtn, changeInBtn;
	
	private UpdateNotifyee notifyee;
	
	/**
	 * @param excats a CategoryHandler for expense categories
	 * @param incats a CategoryHandler for income categories
	 * @param targetMgrRef a TargetManager 
	 */
	public CategoryTargetPanel(
			CategoryHandler<ExpenseRecord> excats,
			CategoryHandler<IncomeRecord> incats,
			TargetManager targetMgrRef,
			UpdateNotifyee notifyee) {
		// interface code generated by window builder
		super(new BorderLayout());
		addNew = new Category(-1, "Add a new category...");
		exmo = new CategoryModel(excats, addNew);
		inmo = new CategoryModel(incats, addNew);
		this.notifyee = notifyee;
		
		this.excats = excats;
		this.incats = incats;
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		this.add(tabbedPane, BorderLayout.CENTER);
		
		JPanel panExCats = new JPanel();
		tabbedPane.addTab(
				"Expense", 
				null, 
				panExCats, 
				null);
		
		panExCats.setLayout(new BorderLayout(0, 0));
		
		exlist = new JList();
		exlist.setFont(Config.TEXT_FONT);
		exlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				updateExDisplay((Category) exlist.getSelectedValue());
			}
		});
		panExCats.add(new JScrollPane(exlist), BorderLayout.CENTER);
		exlist.setMinimumSize(new Dimension(150, 500));
		exlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		JPanel panel = new JPanel();
		panExCats.add(panel, BorderLayout.EAST);
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
		
		JPanel panInCats = new JPanel();
		panInCats.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentShown(ComponentEvent e) {
				inlist.setSelectedIndex(0);
			}
		});
		tabbedPane.addTab(
				"Income", 
				null, 
				panInCats, 
				null);
		panInCats.setLayout(new BorderLayout(0, 0));
		
		inlist = new JList();
		inlist.setFont(Config.TEXT_FONT);
		inlist.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				updateInDisplay((Category) inlist.getSelectedValue());
			}
		});
		inlist.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		inlist.setMinimumSize(new Dimension(150, 500));
		panInCats.add(new JScrollPane(inlist));
		
		JPanel panOptions = new JPanel();
		panInCats.add(panOptions, BorderLayout.EAST);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{40, 0, 134, 0};
		gbl_panel_3.rowHeights = new int[]{29, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, Double.MIN_VALUE};
		panOptions.setLayout(gbl_panel_3);
		
		JLabel lblName_1 = new JLabel("Name:");
		GridBagConstraints gbc_lblName_1 = new GridBagConstraints();
		gbc_lblName_1.anchor = GridBagConstraints.WEST;
		gbc_lblName_1.insets = new Insets(0, 0, 5, 5);
		gbc_lblName_1.gridx = 1;
		gbc_lblName_1.gridy = 3;
		panOptions.add(lblName_1, gbc_lblName_1);
		
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
		panOptions.add(inNameField, gbc_inNameField);
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
		panOptions.add(changeInBtn, gbc_changeInBtn);
		
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
		panOptions.add(removeInBtn, gbc_removeInBtn);
		targetMgr = targetMgrRef;
		
		exlist.setModel(exmo);
		inlist.setModel(inmo);
		
		reload();
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
		curExCat = cat;
		if(cat == null){
			exnameField.setText("");
			targetAmountField.setText("");
			exnameField.setEnabled(false);
			targetAmountField.setEnabled(false);
			removeExBtn.setEnabled(false);
			changeExBtn.setEnabled(false);
			return;
		}
		if(cat != addNew){
			exnameField.setText(cat.getName());
			curTar = targetMgr.getTarget(cat);
			exnameField.setEnabled(true);
			targetAmountField.setEnabled(true);
			if(curTar == null){
				targetAmountField.setText("No target set");
			}else{
				targetAmountField.setText("" + curTar.getTargetAmt());
			}
			changeExBtn.setText("Change");
			changeExBtn.setEnabled(true);
			removeExBtn.setEnabled(true);
		}else{
			exnameField.setText("Pick a name");
			targetAmountField.setText("Set a target");
			changeExBtn.setText("Add");
			exnameField.setEnabled(true);
			targetAmountField.setEnabled(true);
			changeExBtn.setEnabled(true);
			removeExBtn.setEnabled(false);
		}
	}
	
	/**
	 * Update right panel display with income category
	 * @param cat income category to be displayed
	 */
	private void updateInDisplay(Category cat){
		curInCat = cat;
		if(cat == null){
			inNameField.setText("");
			inNameField.setEnabled(false);
			removeInBtn.setEnabled(false);
			changeInBtn.setEnabled(false);
			return;
		}
		if(cat != addNew){
			inNameField.setText(cat.getName());
			inNameField.setEnabled(true);
			removeInBtn.setEnabled(true);
			changeInBtn.setText("Change");
			removeInBtn.setEnabled(true);
			changeInBtn.setEnabled(true);
		}else{
			inNameField.setText("Pick a name");
			changeInBtn.setText("Add");
			inNameField.setEnabled(true);
			removeInBtn.setEnabled(true);
			removeInBtn.setEnabled(false);
			changeInBtn.setEnabled(true);
		}
	}
	
	/**
	 * @param targetString
	 * @return whether the target amount is valid
	 */
	private String validateTarget(String targetString){
		if(targetString.equals("No target set")) {return "";}
		double d;
		try {
			d = Double.parseDouble(targetString);
			if(d == Config.DEFAULT_NO_TARGET) return null;
			if(d < Config.DEFAULT_MIN_TARGET) return "Target too small!";
			if(d > Config.DEFAULT_MAX_TARGET) return "Target is too big!";
		} catch(NumberFormatException e){
			return "Invalid amount entered";
		}
		return null;
	}
	
	/**
	 * Modifies the expense category and target editing
	 */
	private void modifyEx() {
		String err = null;
		String newName = exnameField.getText();
		if(curExCat == addNew || !curExCat.getName().equals(newName)) {
			err = excats.validateCategoryName(newName);
		}
		if(err == null) {
			if(curExCat == addNew) {
				Category cat = excats.addNewCategory(new Category(newName));
				String targetErr = validateTarget(targetAmountField.getText());
				if(targetErr == null) {
					if(Double.parseDouble(targetAmountField.getText()) != 0) {
						targetMgr.setTarget(cat, Double.parseDouble(targetAmountField.getText()));
					}
					notifyee.addUndoAction(getUndoNewCat(cat.getID(), excats), "Creating new category");
				} 
				else if(targetErr.equals("")) {
					notifyee.addUndoAction(getUndoNewCat(cat.getID(), excats), "Creating new category");
				}
				else {
					UINotify.createErrMsg(this, targetErr);
//					JOptionPane.showMessageDialog(this, targetErr, "Error!!!!!", JOptionPane.ERROR_MESSAGE);
				}
				exmo.update();
				exlist.setSelectedValue(cat, true);
			}
			else {
				Category original = curExCat.copy();
				Target tar = targetMgr.getTarget(curExCat);
				double targetAmt = 0;
				if(tar != null) {
					targetAmt = tar.getTargetAmt();
				}
				
				Category cat = excats.updateCategory(curExCat.getID(), new Category(exnameField.getText()));
				String targetErr = validateTarget(targetAmountField.getText());
				
				if(targetErr == null) {
					double d = Double.parseDouble(targetAmountField.getText());
					if(d == 0) {
						targetMgr.removeCategoryTarget(cat.getID());
					} 
					else {
						targetMgr.setTarget(cat, d);
					}
				}
				else if(!targetErr.equals("")) {
					UINotify.createErrMsg(this, targetErr);
//					JOptionPane.showMessageDialog(this, targetErr, "Error!!!!!", JOptionPane.ERROR_MESSAGE);
					return;
				}
				boolean isChanged;
				isChanged = !cat.getName().equals(original.getName()); // Name is not the same
				if(!isChanged) {
					Target oriTarget = targetMgr.getTarget(original);
					if(oriTarget!=null) { // Original Target is not the same as the new target
						isChanged = oriTarget.getTargetAmt() != targetAmt;
					}
					else { // New Target is not zero
						isChanged = targetAmt != 0;
					}
				}
				if(isChanged) notifyee.addUndoAction(getUndoModifyExCat(cat.getID(), original, targetAmt), "Modify category");
				exmo.update();
				exlist.setSelectedValue(cat, true);
			}
			notifyee.updateAll();
		}
		else {
			UINotify.createErrMsg(this, err);
//			JOptionPane.showMessageDialog(this, err, "Error!!!!!", JOptionPane.ERROR_MESSAGE);
		}
	}
	
	/**
	 * modify the income category editing
	 */
	private void modifyIn(){
		String err = null;
		String newName = inNameField.getText();
		if(curInCat == addNew || !curInCat.getName().equals(newName)){
			err = incats.validateCategoryName(newName);
		}
		if(err == null){
			if(curInCat == addNew){
				Category cat = incats.addNewCategory(new Category(newName));
				notifyee.addUndoAction(getUndoNewCat(cat.getID(), incats), "Create new category");
				inmo.update();
				inlist.setSelectedValue(cat, true);
			}else{
				Category original = curInCat.copy();
				Category cat = incats.updateCategory(curInCat.getID(), new Category(inNameField.getText()));
				inmo.update();
				inlist.setSelectedValue(cat, true);
				notifyee.addUndoAction(getUndoModifyInCat(cat.getID(), original), "Modify category");
			}
			notifyee.updateAll();
		} else{
			JOptionPane.showMessageDialog(this, err, "Error!!!!!", JOptionPane.ERROR_MESSAGE);
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
			Category original = curExCat.copy();
			double targetAmt = 0;
			if(targetMgr.getTarget(curExCat)!= null){
				targetAmt = targetMgr.getTarget(curExCat).getTargetAmt();
			}
			List<ExpenseRecord> recs = excats.getRecordsBy(curExCat, -1);
			excats.removeCategory(curExCat.getID());
			exmo.update();
			exlist.setSelectedIndex(0);
			notifyee.addUndoAction(getUndoRemoveExCat(recs, original, targetAmt), "Removing Category");
			notifyee.updateAll();
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
			List<IncomeRecord> oriRecords = incats.getRecordsBy(curInCat, -1);
			Category cat = curInCat.copy();
			incats.removeCategory(curInCat.getID());
			inmo.update();
			inlist.setSelectedIndex(0);
			notifyee.updateAll();
			notifyee.addUndoAction(getUndoRemoveInCat(oriRecords, cat.copy()), "Removing Category");
		}
	}
	
	private AbstractAction getUndoNewCat(final long id, final CategoryHandler cats){
		return new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				cats.removeCategory(id);
			}
			
		};
	}
	
	private AbstractAction getUndoModifyInCat(final long id, final Category original){
		return new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				incats.updateCategory(id, original);
			}
			
		};
	}
	
	private AbstractAction getUndoModifyExCat(
			final long id,
			final Category original,
			final double originalTargetAmt){
		return new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Category cat = excats.updateCategory(id, original);
				if(originalTargetAmt == 0){
					targetMgr.removeCategoryTarget(id);
				}else{
					targetMgr.setTarget(cat, originalTargetAmt);
				}
			}
			
		};
	}
	
	private AbstractAction getUndoRemoveInCat(final List<IncomeRecord> recs, final Category original){
		// deleting a category moves all records in it to undefined
		// we need to undo this as well
		return new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Category cat = incats.addNewCategory(original);
				incats.addToCategory(recs, cat);
			}
		};
	}
	
	private AbstractAction getUndoRemoveExCat(
			final List<ExpenseRecord> recs,
			final Category original,
			final double targetAmt){
		// deleting a category moves all records in it to undefined
		// we need to undo this as well
		return new AbstractAction(){

			@Override
			public void actionPerformed(ActionEvent e) {
				Category cat = excats.addNewCategory(original);
				excats.addToCategory(recs, cat);
				if(targetAmt != 0){
					targetMgr.setTarget(cat, targetAmt);
				}
			}
		};
	}
	
	/**
	 * Reload the panel, called if relevant data is updated
	 */
	public void reload() {
		loadExList();
		loadInList();
		inlist.setSelectedIndex(0);
		exlist.setSelectedIndex(0);
		this.validate();
	}
}	

